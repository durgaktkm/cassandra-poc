import co.mimosa.cassandra.AsyncCassandraOperations;
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
import java.util.concurrent.ExecutionException;
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
        List<Future<Boolean>> resultList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for(int i=0;i<5000;i++) {
            Future<Boolean> booleanFuture = asyncCassandraOperations.sendAsyncBatch("XXX_" + i, events, epochStartTime += 500);

            resultList.add(booleanFuture);
        }

        for(Future<Boolean> result:resultList){
            result.get();
        }

        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime);
        System.out.println("Hurray");
    }
}
