package co.mimosa.cassandra.parser;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.codehaus.jackson.JsonNode;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ramdurga on 3/18/15.
 */
public class ParserTest {
    @Test
    public void testParser() throws IOException {
        URL url = Resources.getResource("Phystats.json");
        String text = Resources.toString(url, Charsets.UTF_8);
        //System.out.println(text);
        PhystatsParser parser = new PhystatsParser();
        String events = parser.parseJson(text).asText();
        Map<Long,Map<String,Double>> resultList = new HashMap();
        Map<String,Double> resultMap = null;
        System.out.printf("Hurray");
        String[] topSplits = events.split("##");
        System.out.println(topSplits[0]);
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

            resultList.put(Long.parseLong(valueSplits[0]), resultMap);

        }
        System.out.println("huuuu");

    }
}
