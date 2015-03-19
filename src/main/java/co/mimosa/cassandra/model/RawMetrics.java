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
    private Map<String,Double> data;

    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    public Map<String, Double> getData() {
        return data;
    }

    public void setData(Map<String, Double> data) {
        this.data = data;
    }
}
