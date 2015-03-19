package co.mimosa.cassandra.parser;


import co.mimosa.cassandra.model.Metrics;
import co.mimosa.cassandra.model.RawMetrics;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ramdurga on 3/18/15.
 */
public class PhystatsParser {
    public JsonNode parseJson(String event) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String eventJsonString = event.replaceAll("\n", "#");
        return objectMapper.readTree(eventJsonString).get("event").get("mimosaContent").get("content").get("values").get("phystats");
    }
    public List<RawMetrics> getRawMetrics(String serialNumber,String events) throws IOException {

        List<RawMetrics> rawMetricsList = new ArrayList<>();
        RawMetrics rawMetrics = null;
        Metrics metrics = null;
        Map<String,Double> resultMap = null;
        String[] topSplits = events.split("##");
        System.out.println(topSplits[0]);
        String[] topNames = topSplits[0].trim().split("\\s+");
        String[] values = topSplits[1].trim().split("#");
        for(String s:values){
            String[] valueSplits = s.split(":");
            String[] actualValues = valueSplits[1].trim().split("\\s+");
            resultMap = new HashMap<>();

            metrics = new Metrics(serialNumber,Long.parseLong(valueSplits[0]));

            for(int i =0;i <topNames.length; i++) {
                rawMetrics = new RawMetrics();
                rawMetrics.setMetrics(metrics);
                if (actualValues[i].equalsIgnoreCase("nan")) {
                    resultMap.put(topNames[i], Double.NaN);
                    rawMetrics.setKey(topNames[i]);
                    rawMetrics.setValue(Double.NaN);
                } else {
                    resultMap.put(topNames[i], Double.parseDouble(actualValues[i]));
                    rawMetrics.setKey(topNames[i]);
                    rawMetrics.setValue(Double.parseDouble(actualValues[i]));
                }
                rawMetricsList.add(rawMetrics);
            }


        }
        return rawMetricsList;
    }
    public List<RawMetrics> getRawMetricsForHardCodedTimeStamps(String serialNumber,String events,long startTime) throws IOException {

        List<RawMetrics> rawMetricsList = new ArrayList<>();
        RawMetrics rawMetrics = null;
        Metrics metrics = null;
        Map<String,Double> resultMap = null;
        String[] topSplits = events.split("##");
       // System.out.println(topSplits[0]);
        String[] topNames = topSplits[0].trim().split("\\s+");
        String[] values = topSplits[1].trim().split("#");
        for(String s:values){
            String[] valueSplits = s.split(":");
            String[] actualValues = valueSplits[1].trim().split("\\s+");
            resultMap = new HashMap<>();

            metrics = new Metrics(serialNumber,Long.parseLong(valueSplits[0]));

            for(int i =0;i <topNames.length; i++) {
                rawMetrics = new RawMetrics();
                rawMetrics.setMetrics(metrics);
                if (actualValues[i].equalsIgnoreCase("nan")) {
                    resultMap.put(topNames[i], Double.NaN);
                    rawMetrics.setKey(topNames[i]);
                    rawMetrics.setValue(Double.NaN);
                } else {
                    resultMap.put(topNames[i], Double.parseDouble(actualValues[i]));
                    rawMetrics.setKey(topNames[i]);
                    rawMetrics.setValue(Double.parseDouble(actualValues[i]));
                }
                rawMetricsList.add(rawMetrics);
            }


        }
        return rawMetricsList;
    }
}
