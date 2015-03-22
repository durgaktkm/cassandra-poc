package co.mimosa.cassandra.parser;

import co.mimosa.kafka.valueobjects.GateWayData;
import com.amazonaws.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.codehaus.jackson.JsonNode;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ramdurga on 3/21/15.
 */
public class LoadTestPhystats {
    public static void main(String[] args) throws IOException {
        URL url = Resources.getResource("testPhystats.json");
        String eventJsonString = Resources.toString(url, Charsets.UTF_8);
           Pattern EVENT_TYPE_PATTERN = Pattern.compile("(?<=\"eventType\":\")\\w+");
        eventJsonString = eventJsonString.replaceAll("\n", "#");
        int eventDataEllipsisSize = (eventJsonString.length() > 65 ? 65 : eventJsonString.length());
        List<Map<String, Double>> rawMetrics= null;
    ObjectMapper objectMapper = new ObjectMapper();
        PhystatsParser phystatsParser = new PhystatsParser();
           String eventType = getMatchingString(EVENT_TYPE_PATTERN, eventJsonString);
            if(eventType.equalsIgnoreCase("Phystats")) {
                String event = objectMapper.readTree(eventJsonString).get("event").get("mimosaContent").get("content").get("values").get("phystats").asText();
                if(!StringUtils.isNullOrEmpty(event)){
                    rawMetrics = phystatsParser.getRawMetrics("123456", event);
                }else{
                    System.out.println("Json node is null, there is issue with input data");
                }
            }
        System.out.println("finally");
    }
    static String getMatchingString(Pattern pattern, String jsonString) {
        Matcher matcher = pattern.matcher(jsonString);
        if(matcher.find())
            return matcher.group();
        return null;
    }
}
