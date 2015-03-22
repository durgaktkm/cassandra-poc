package co.mimosa.cassandra.parser;


import co.mimosa.cassandra.model.Metrics;
import co.mimosa.cassandra.model.RawMetrics;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ramdurga on 3/18/15.
 */
@Component
public class PhystatsParser {
    @Autowired
    ObjectMapper objectMapper;
    public JsonNode parseJson(String event) throws IOException {
        String eventJsonString = event.replaceAll("\n", "#");
        return objectMapper.readTree(eventJsonString).get("event").get("mimosaContent").get("content").get("values").get("phystats");
    }



    public List<Map<String,Double>> getRawMetrics(String serialNumber,String events) throws IOException {

        List<Map<String,Double>> rawMetricsList = new ArrayList<>();

        Map<String,Double> resultMap = null;
        String[] topSplits = events.split("##");
        //System.out.println(topSplits[0]);
        String[] topNames = topSplits[0].trim().split("\\s+");
        String[] values = topSplits[1].trim().split("#");
        for(String s:values){
            if(!s.trim().isEmpty()) {
                String[] valueSplits = s.trim().split(":");
                String[] actualValues = valueSplits[1].trim().split("\\s+");
                resultMap = new HashMap<>();
                resultMap.put("timeStamp", Double.parseDouble(valueSplits[0].trim()));
                for (int i = 0; i < topNames.length; i++) {

                    if (actualValues[i].equalsIgnoreCase("nan")) {
                        resultMap.put(topNames[i], Double.NaN);

                    } else {
                        resultMap.put(topNames[i], Double.parseDouble(actualValues[i]));

                    }

                }
                rawMetricsList.add(resultMap);
            }
        }
        return rawMetricsList;
    }
    public List<Map<String,Double>> getRawMetricsForHardCodedTimeStamps(String serialNumber,String events,long startTime) throws IOException {

        List<Map<String,Double>> rawMetricsList = new ArrayList<>();
        RawMetrics rawMetrics = null;
        Metrics metrics = null;
        Map<String,Double> resultMap = null;
        String[] topSplits = events.split("##");
        //System.out.println(topSplits[0]);
        String[] topNames = topSplits[0].trim().split("\\s+");
        String[] values = topSplits[1].trim().split("#");
        for(String s:values){
            String[] valueSplits = s.split(":");
            String[] actualValues = valueSplits[1].trim().split("\\s+");
            resultMap = new HashMap<>();

            for(int i =0;i <topNames.length; i++) {

                if (actualValues[i].equalsIgnoreCase("nan")) {
                    resultMap.put(topNames[i], Double.NaN);

                } else {
                    resultMap.put(topNames[i], Double.parseDouble(actualValues[i]));

                }

            }
            rawMetricsList.add(resultMap);

        }
        return rawMetricsList;
    }
}
