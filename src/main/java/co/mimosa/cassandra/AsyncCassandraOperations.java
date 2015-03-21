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
import java.util.Map;
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
//        List<RawMetrics> rawMetricsList = parser.getRawMetricsForHardCodedTimeStamps(serialNumber, events, timeStamp);
//        String cqlIngest = "insert into raw_metrics(serialNumber,eventTime,metricName,key,value)values(?,?,?,?,?)";
//        List<List<?>> metricsToSave = new ArrayList<List<?>>();
//        List<Object> intermediateObject = null;
//        for (RawMetrics rawMetrics : rawMetricsList) {
//            intermediateObject= new ArrayList<>();
//            intermediateObject.add(rawMetrics.getMetrics().getSerialNumber());
//            intermediateObject.add(rawMetrics.getMetrics().getEventTime());
//            intermediateObject.add("Phystats");
//
//            intermediateObject.add(rawMetrics.getKey());
//            intermediateObject.add(rawMetrics.getValue());
//            metricsToSave.add(intermediateObject);
//
//        }
//        operations.ingest(cqlIngest,metricsToSave);
//        System.out.println("Done running...");
//        result = true;
        return new AsyncResult<>(result);
    }
    public Future<Boolean> saveItToCassandra(String serialNumber, List<Map<String,Double>> rawMetricsList)
            throws InterruptedException, IOException {
        Session session = operations.getSession();

        PreparedStatement ps = session.prepare("insert into raw_metrics(serialNumber,eventTime,metricName,rxpkts,rxbytes,rxgain,crc,noise,noise2,txpkts,txbytes,txdefers,txtouts,txretries,txfails,sp_err,lp_err,txrate,txstreams,txmcs,rxrate,rxstreams,rxmcs,evm_0,evm_1,evm_2,evm_3,rssi_0,rssi_1,rssi_2,rssi_3,temp,gps_gear,gps_sats,per,bw)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) using TTL 300");
        BatchStatement batch = new BatchStatement();
        for (Map<String,Double> rawMetrics : rawMetricsList) {
            batch.add(ps.bind(serialNumber,rawMetrics.get("timeStamp").longValue(), "Phystats",rawMetrics.get("rxpkts"),rawMetrics.get("rxbytes"),rawMetrics.get("rxgain"),rawMetrics.get("crc"),rawMetrics.get("noise"),rawMetrics.get("noise2"),rawMetrics.get("txpkts"),rawMetrics.get("txbytes"),rawMetrics.get("txdefers"),rawMetrics.get("txtouts"),rawMetrics.get("txretries"),rawMetrics.get("txfails"),rawMetrics.get("sp_err"),rawMetrics.get("lp_err"),rawMetrics.get("txrate"),rawMetrics.get("txstreams"),rawMetrics.get("txmcs"),rawMetrics.get("rxrate"),rawMetrics.get("rxstreams"),rawMetrics.get("rxmcs"),rawMetrics.get("evm_0"),rawMetrics.get("evm_1"),rawMetrics.get("evm_2"),rawMetrics.get("evm_3"),rawMetrics.get("rssi_0"),rawMetrics.get("rssi_1"),rawMetrics.get("rssi_2"),rawMetrics.get("rssi_3"),rawMetrics.get("temp"),rawMetrics.get("gps_gear"),rawMetrics.get("gps_sats"),rawMetrics.get("per"),rawMetrics.get("bw")));

        }

        ResultSetFuture resultSetFuture = session.executeAsync(batch);
        System.out.println("Done running...");
        return new AsyncResult<>(true);
    }
    @Async
    public Future<Boolean> sendAsyncBatch(String serialNumber, String events, long timeStamp)
            throws InterruptedException, IOException {
        boolean result = false;
        List<Map<String,Double>> rawMetricsList = parser.getRawMetricsForHardCodedTimeStamps(serialNumber, events, timeStamp);
        Session session = operations.getSession();

        PreparedStatement ps = session.prepare("insert into raw_metrics(serialNumber,eventTime,metricName,rxpkts,rxbytes,rxgain,crc,noise,noise2,txpkts,txbytes,txdefers,txtouts,txretries,txfails,sp_err,lp_err,txrate,txstreams,txmcs,rxrate,rxstreams,rxmcs,evm_0,evm_1,evm_2,evm_3,rssi_0,rssi_1,rssi_2,rssi_3,temp,gps_gear,gps_sats,per,bw)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) using TTL 300");
        BatchStatement batch = new BatchStatement();
        for (Map<String,Double> rawMetrics : rawMetricsList) {
            batch.add(ps.bind(serialNumber,timeStamp+=10, "Phystats",rawMetrics.get("rxpkts"),rawMetrics.get("rxbytes"),rawMetrics.get("rxgain"),rawMetrics.get("crc"),rawMetrics.get("noise"),rawMetrics.get("noise2"),rawMetrics.get("txpkts"),rawMetrics.get("txbytes"),rawMetrics.get("txdefers"),rawMetrics.get("txtouts"),rawMetrics.get("txretries"),rawMetrics.get("txfails"),rawMetrics.get("sp_err"),rawMetrics.get("lp_err"),rawMetrics.get("txrate"),rawMetrics.get("txstreams"),rawMetrics.get("txmcs"),rawMetrics.get("rxrate"),rawMetrics.get("rxstreams"),rawMetrics.get("rxmcs"),rawMetrics.get("evm_0"),rawMetrics.get("evm_1"),rawMetrics.get("evm_2"),rawMetrics.get("evm_3"),rawMetrics.get("rssi_0"),rawMetrics.get("rssi_1"),rawMetrics.get("rssi_2"),rawMetrics.get("rssi_3"),rawMetrics.get("temp"),rawMetrics.get("gps_gear"),rawMetrics.get("gps_sats"),rawMetrics.get("per"),rawMetrics.get("bw")));

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
