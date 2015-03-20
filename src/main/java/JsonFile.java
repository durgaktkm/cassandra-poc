import co.mimosa.cassandra.config.NMSBackendConfig;
import co.mimosa.cassandra.model.RawMetrics;
import co.mimosa.cassandra.parser.PhystatsParser;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.cassandra.core.CassandraOperations;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramdurga on 3/18/15.
 */
public class JsonFile {
    public static void main(String[] args) throws IOException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(NMSBackendConfig.class);
        CassandraOperations operations= ctx.getBean(CassandraOperations.class);
        PhystatsParser phystatsParser = ctx.getBean(PhystatsParser.class);
        URL url = Resources.getResource("Phystats_2.json");
        String text = Resources.toString(url, Charsets.UTF_8);
        //System.out.println(text);
        String events = phystatsParser.parseJson(text).asText();
        List<RawMetrics> rawMetricsList = phystatsParser.getRawMetrics("xx123", events);
        String cqlIngest = "insert into raw_metrics(serialNumber,eventTime,metricName,key,value)values(?,?,?,?,?)";
        List<List<?>> metricsToSave = new ArrayList<List<?>>();
        List<Object> intermediateObject = null;
        for(RawMetrics rawMetrics:rawMetricsList){
            intermediateObject= new ArrayList<>();
            intermediateObject.add(rawMetrics.getMetrics().getSerialNumber());
            intermediateObject.add(rawMetrics.getMetrics().getEventTime());
            intermediateObject.add("Phystats");

            intermediateObject.add(rawMetrics.getKey());
            intermediateObject.add(rawMetrics.getValue());
            metricsToSave.add(intermediateObject);
        }
        operations.ingest(cqlIngest,metricsToSave);
        System.out.println("Hurray");
    }
}
