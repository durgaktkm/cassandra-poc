package co.mimosa.cassandra.model;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ramdurga on 3/18/15.
 */
@PrimaryKeyClass
public class Metrics implements Serializable{
    @PrimaryKeyColumn(name = "serialNumber", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String serialNumber;
    @PrimaryKeyColumn(name = "event_time", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private long eventTime;

    public Metrics(String serialNumber, long eventTime) {
        this.serialNumber = serialNumber;
        this.eventTime = eventTime;
    }

    public Metrics() {
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }
}
