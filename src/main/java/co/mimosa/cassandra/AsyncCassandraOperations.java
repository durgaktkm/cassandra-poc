package co.mimosa.cassandra;

import co.mimosa.cassandra.model.RawMetrics;
import co.mimosa.cassandra.parser.PhystatsParser;
import com.datastax.driver.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by ramdurga on 3/18/15.
 */
public class AsyncCassandraOperations {
    @Autowired
    PhystatsParser parser;
    @Autowired
    CassandraOperations operations;
    @Autowired
    CassandraClusterFactoryBean cluster;

    @Async
    public Future<Boolean> sendAsync(String serialNumber, String events, long timeStamp)
            throws InterruptedException, IOException {
        boolean result = false;
        List<RawMetrics> rawMetricsList = parser.getRawMetricsForHardCodedTimeStamps(serialNumber, events, timeStamp);
        String cqlIngest = "insert into raw_metrics(serialNumber,event_time,data)values(?,?,?)";
        List<List<?>> metricsToSave = new ArrayList<List<?>>();
        List<Object> intermediateObject = null;
        for (RawMetrics rawMetrics : rawMetricsList) {
            intermediateObject = new ArrayList<>();
            intermediateObject.add(rawMetrics.getMetrics().getSerialNumber());
            intermediateObject.add(rawMetrics.getMetrics().getEventTime());
           // intermediateObject.add(rawMetrics.getData());
            metricsToSave.add(intermediateObject);
            result = true;



            // Wrap the result in an AsyncResult

        }
        operations.ingest(cqlIngest,metricsToSave);
        System.out.println("Done running...");
        return new AsyncResult<>(result);
    }
    @Async
    public Future<Boolean> sendAsyncBatch(String serialNumber, String events, long timeStamp)
            throws InterruptedException, IOException {
        boolean result = false;
        List<RawMetrics> rawMetricsList = parser.getRawMetricsForHardCodedTimeStamps(serialNumber, events, timeStamp);
        Session session = operations.getSession();

        PreparedStatement ps = session.prepare("insert into raw_metrics(serialNumber,event_time,data)values(?,?,?)");
        BatchStatement batch = new BatchStatement();
        for (RawMetrics rawMetrics : rawMetricsList) {
          //  batch.add(ps.bind(rawMetrics.getMetrics().getSerialNumber(), rawMetrics.getMetrics().getEventTime(), rawMetrics.getData()));

        }


        ResultSetFuture resultSetFuture = session.executeAsync(batch);
        result =true;
        System.out.println("Done running...");
        return new AsyncResult<>(result);
    }


}
