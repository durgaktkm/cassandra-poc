package co.mimosa.cassandra.kafka;

import co.mimosa.kafka.callable.IEventAnalyzer;
import co.mimosa.kafka.producer.MimosaProducer;
import co.mimosa.kafka.valueobjects.GateWayData;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * Created by ramdurga on 3/20/15.
 */
public class KafkaEventAnalyzer implements IEventAnalyzer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaEventAnalyzer.class);
    private static final Pattern EVENT_TYPE_PATTERN = Pattern.compile("(?<=\"eventType\":\")\\w+");
    private final String newLineReplacement;
    private final String dirSeparator;
    private final ObjectMapper objectMapper;
    private final MimosaProducer producer;
    private final String errorTopic;

    public KafkaEventAnalyzer(String newLineReplacement, String dirSeparator,ObjectMapper objectMapper,MimosaProducer producer,String errorTopic){

        this.newLineReplacement = newLineReplacement;
        this.dirSeparator = dirSeparator;
        this.objectMapper = objectMapper;
        this.producer = producer;
        this.errorTopic = errorTopic;
    }
    @Override
    public Boolean analyze(String serialNumber, GateWayData gateWayData) {
        return null;
    }
}
