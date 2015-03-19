package co.mimosa.cassandra.model;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.Map;

/**
 * Created by ramdurga on 3/18/15.
 */

@Table(value = "raw_metrics")
public class RawMetrics {

    @PrimaryKey
    private Metrics metrics;
    @Column
    private String metricName ;
    @Column
    private String key;
    @Column
    private Double value;

    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
