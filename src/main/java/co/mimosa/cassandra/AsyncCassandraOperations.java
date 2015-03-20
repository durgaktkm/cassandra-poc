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
import java.util.concurrent.ExecutionException;
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
        String cqlIngest = "insert into raw_metrics(serialNumber,eventTime,metricName,key,value)values(?,?,?,?,?)";
        List<List<?>> metricsToSave = new ArrayList<List<?>>();
        List<Object> intermediateObject = null;
        for (RawMetrics rawMetrics : rawMetricsList) {
            intermediateObject= new ArrayList<>();
            intermediateObject.add(rawMetrics.getMetrics().getSerialNumber());
            intermediateObject.add(rawMetrics.getMetrics().getEventTime());
            intermediateObject.add("Phystats");

            intermediateObject.add(rawMetrics.getKey());
            intermediateObject.add(rawMetrics.getValue());
            metricsToSave.add(intermediateObject);

        }
        operations.ingest(cqlIngest,metricsToSave);
        System.out.println("Done running...");
        result = true;
        return new AsyncResult<>(result);
    }
    @Async
    public Future<Boolean> sendAsyncBatch(String serialNumber, String events, long timeStamp)
            throws InterruptedException, IOException {
        boolean result = false;
        List<RawMetrics> rawMetricsList = parser.getRawMetricsForHardCodedTimeStamps(serialNumber, events, timeStamp);
        Session session = operations.getSession();

        PreparedStatement ps = session.prepare("insert into raw_metrics(serialNumber,eventTime,metricName,key,value)values(?,?,?,?,?)");
        BatchStatement batch = new BatchStatement();
        for (RawMetrics rawMetrics : rawMetricsList) {
            batch.add(ps.bind(rawMetrics.getMetrics().getSerialNumber(), rawMetrics.getMetrics().getEventTime(), "Phystats",rawMetrics.getKey(),rawMetrics.getValue()));

        }

        ResultSetFuture resultSetFuture = session.executeAsync(batch);
        result =true;
        System.out.println("Done running...");
        return new AsyncResult<>(result);
    }
    @Async
    public Future<Boolean> callBatchAsync(int batchSize,int serialNumber, String events, long timeStamp) throws IOException, InterruptedException, ExecutionException {
        List<Future<Boolean>> resultList = new ArrayList<>();
        for(int i=0;i<batchSize;i++) {
            Future<Boolean> booleanFuture = sendAsyncBatch("XXX_" + serialNumber+"_"+i, events, timeStamp += 500);
            resultList.add(booleanFuture);
        }

        for(Future<Boolean> result:resultList){
            result.get();
        }
        return new AsyncResult<>(true);
    }


}
