import co.mimosa.cassandra.config.NMSBackendConfig;
import co.mimosa.cassandra.model.Metrics;
import co.mimosa.cassandra.model.RawMetrics;
import co.mimosa.cassandra.repository.RawMetricsRepository;
import org.springframework.cassandra.core.CqlOperations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.cassandra.core.CassandraOperations;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ramdurga on 3/18/15.
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(NMSBackendConfig.class);
       // CqlOperations cqlOperations = ctx.getBean(CqlOperations.class);
        CassandraOperations operations= ctx.getBean(CassandraOperations.class);
        //RawMetricsRepository repository = ctx.getBean(RawMetricsRepository.class);
        System.out.println("successfull");
//        RawMetrics rawMetrics = new RawMetrics();
//        Metrics m1 = new Metrics("123456",1426708580);
//        rawMetrics.setMetrics(m1);
//        Map<String,Double> dataMap = new HashMap<>();
//        dataMap.put("rx",30.23);
//        dataMap.put("tx", 60.53);
//        rawMetrics.setData(dataMap);
//
//        operations.insert(rawMetrics);
       // repository.save(rawMetrics);
        System.out.println("Hurray");
    }
}
