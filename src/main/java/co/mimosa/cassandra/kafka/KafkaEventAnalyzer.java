package co.mimosa.cassandra.kafka;

import co.mimosa.cassandra.AsyncCassandraOperations;
import co.mimosa.cassandra.parser.PhystatsParser;
import co.mimosa.kafka.callable.IEventAnalyzer;
import co.mimosa.kafka.producer.MimosaProducer;
import co.mimosa.kafka.valueobjects.GateWayData;
import com.amazonaws.util.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ramdurga on 3/20/15.
 */
public class KafkaEventAnalyzer implements IEventAnalyzer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaEventAnalyzer.class);

    PhystatsParser phystatsParser;
    private AsyncCassandraOperations asyncCassandraOperations;
    private static final Pattern EVENT_TYPE_PATTERN = Pattern.compile("(?<=\"eventType\":\")\\w+");
    private final String newLineReplacement;
    private final String dirSeparator;
    private final ObjectMapper objectMapper;
    private final MimosaProducer producer;
    private final String errorTopic;

    public KafkaEventAnalyzer(String newLineReplacement, String dirSeparator,ObjectMapper objectMapper,MimosaProducer producer,String errorTopic, PhystatsParser phystatsParser,AsyncCassandraOperations asyncCassandraOperations){

        this.newLineReplacement = newLineReplacement;
        this.dirSeparator = dirSeparator;
        this.objectMapper = objectMapper;
        this.producer = producer;
        this.errorTopic = errorTopic;
        this.phystatsParser = phystatsParser;
        this.asyncCassandraOperations = asyncCassandraOperations;
    }
    @Override
    public Boolean analyze(String serialNumber, GateWayData gateWayData) {
        if(!gateWayData.isFileData()) {
            String eventJsonString= gateWayData.getContents();
            String eventType;
            eventJsonString = eventJsonString.replaceAll("\n", newLineReplacement);
            int eventDataEllipsisSize = (eventJsonString.length() > 65 ? 65 : eventJsonString.length());
            logger.debug("Hash replaced content={}", eventJsonString.substring(0, eventDataEllipsisSize).trim() + "...");
            List<Map<String, Double>> rawMetrics= null;

            try {
                eventType = getMatchingString(EVENT_TYPE_PATTERN, eventJsonString);
                if(eventType.equalsIgnoreCase("Phystats")){
                    String event = objectMapper.readTree(eventJsonString).get("event").get("mimosaContent").get("content").get("values").get("phystats").asText();
                    if(!StringUtils.isNullOrEmpty(event)){
                        rawMetrics = phystatsParser.getRawMetrics(serialNumber, event);
                    }else{
                        logger.error("Json node is null, there is issue with input data");
                    }
                }
            } catch (Exception e) {

                    logger.error("Parsing error , data issue: "+e.getMessage());
                return false;

            }

            try {
                asyncCassandraOperations.saveItToCassandra(serialNumber,rawMetrics);

            } catch (Exception e) {
                logger.error("Error while Saving to Cassandra" + e.getMessage());
                return false;
            }
        }else{
            logger.error("It is file data, shouldn't be here for POC");
        }
        return true;
    }
    String getMatchingString(Pattern pattern, String jsonString) {
        Matcher matcher = pattern.matcher(jsonString);
        if(matcher.find())
            return matcher.group();
        return null;
    }
}
