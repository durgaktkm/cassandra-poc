package co.mimosa.producer;

import co.mimosa.kafka.producer.MimosaProducer;
import co.mimosa.kafka.valueobjects.GateWayData;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;

/**
 * Created by ramdurga on 3/20/15.
 */
public class SendPhyStats {
    public static void main(String[] args) throws IOException {
        MimosaProducer producer = new MimosaProducer("localhost:9092");
        URL url = Resources.getResource("Phystats.json");
        String text = Resources.toString(url, Charsets.UTF_8);
        GateWayData gateWayData = new GateWayData(System.currentTimeMillis(),false,null,false,text);
        producer.sendDataToKafka("deviceData","1234567",gateWayData);
        System.out.println("Hurray again");
    }
}
