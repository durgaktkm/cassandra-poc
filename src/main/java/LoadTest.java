import co.mimosa.cassandra.AsyncCassandraOperations;
import co.mimosa.cassandra.config.NMSBackendConfig;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by ramdurga on 3/18/15.
 */
public class LoadTest {
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(NMSBackendConfig.class);
        CassandraOperations operations= ctx.getBean(CassandraOperations.class);
        PhystatsParser phystatsParser = ctx.getBean(PhystatsParser.class);
        AsyncCassandraOperations asyncCassandraOperations = ctx.getBean(AsyncCassandraOperations.class);
        URL url = Resources.getResource("Phystats.json");
        String text = Resources.toString(url, Charsets.UTF_8);
        //System.out.println(text);
        long epochStartTime = 1395201980;
        String events = phystatsParser.parseJson(text).asText();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<Boolean>> resultList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        List<Future<?>> submitList = new ArrayList<>();
        int serialNumber =1000000;
        for(int i =0;i<10;i++){
            Future<?> submit = executorService.submit(new MyRunner( serialNumber , asyncCassandraOperations, 10, 1000, events, epochStartTime + 5000));
            submitList.add(submit);
            serialNumber +=serialNumber;

        }
        for(Future<?> future:submitList){
            future.get();
        }
//        for(int i=0;i<1;i++) {
//            Future<Boolean> booleanFuture = asyncCassandraOperations.sendAsyncBatch("XXX_" + i, events, epochStartTime += 500);
//
//            resultList.add(booleanFuture);
//        }
//
//        for(Future<Boolean> result:resultList){
//            result.get();
//        }
//
        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime);
        System.out.println("Hurray");
    }
}
  class MyRunner implements Runnable{

    private int serialNumberToStart ;
    private AsyncCassandraOperations asyncCassandraOperations;
    private int numOfTimes;
    private int batchSize;
    private String event;
    private long timeStampToStart;

    MyRunner( int serialNumberToStart, AsyncCassandraOperations asyncCassandraOperations, int numOfTimes, int batchSize, String event, long timeStampToStart){


        this.serialNumberToStart = serialNumberToStart;
        this.asyncCassandraOperations = asyncCassandraOperations;
        this.numOfTimes = numOfTimes;
        this.batchSize = batchSize;
        this.event = event;
        this.timeStampToStart = timeStampToStart;
    }
    @Override
    public void run(){
        for(int i=0;i<numOfTimes;i++){
            try {
                Future<Boolean> booleanFuture = asyncCassandraOperations.callBatchAsync(batchSize, serialNumberToStart+i, event, timeStampToStart);
                booleanFuture.get();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
