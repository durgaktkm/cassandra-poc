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
    private String metricName;
    @Column
    private Double rxpkts;
    @Column
    private Double rxbytes;
    @Column
    private Double rxgain;
    @Column
    private Double crc;
    @Column
    private Double noise;
    @Column
    private Double noise2;
    @Column
    private Double txpkts;
    @Column
    private Double txbytes;
    @Column
    private Double txdefers;
    @Column
    private Double txtouts;
    @Column
    private Double txretries;
    @Column
    private Double txfails;
    @Column
    private Double sp_err;
    @Column
    private Double lp_err;
    @Column
    private Double txrate;
    @Column
    private Double txstreams;
    @Column
    private Double txmcs;
    @Column
    private Double rxrate;
    @Column
    private Double rxstreams;
    @Column
    private Double rxmcs;
    @Column
    private Double evm_0;
    @Column
    private Double em_1;
    @Column
    private Double evm_2;
    @Column
    private Double evm_3;
    @Column
    private Double rssi_0;
    @Column
    private Double rssi_1;
    @Column
    private Double rssi_2;
    @Column
    private Double rssi_3;
    @Column
    private Double temp;
    @Column
    private Double gps_gear;
    @Column
    private Double gps_sats;
    @Column
    private Double per;
    @Column
    private Double bw;


    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    public String getMetricName() {
        return metricName;
    }

    public Double getRxpkts() {
        return rxpkts;
    }

    public void setRxpkts(Double rxpkts) {
        this.rxpkts = rxpkts;
    }

    public Double getRxbytes() {
        return rxbytes;
    }

    public void setRxbytes(Double rxbytes) {
        this.rxbytes = rxbytes;
    }

    public Double getRxgain() {
        return rxgain;
    }

    public void setRxgain(Double rxgain) {
        this.rxgain = rxgain;
    }

    public Double getCrc() {
        return crc;
    }

    public void setCrc(Double crc) {
        this.crc = crc;
    }

    public Double getNoise() {
        return noise;
    }

    public void setNoise(Double noise) {
        this.noise = noise;
    }

    public Double getNoise2() {
        return noise2;
    }

    public void setNoise2(Double noise2) {
        this.noise2 = noise2;
    }

    public Double getTxpkts() {
        return txpkts;
    }

    public void setTxpkts(Double txpkts) {
        this.txpkts = txpkts;
    }

    public Double getTxbytes() {
        return txbytes;
    }

    public void setTxbytes(Double txbytes) {
        this.txbytes = txbytes;
    }

    public Double getTxdefers() {
        return txdefers;
    }

    public void setTxdefers(Double txdefers) {
        this.txdefers = txdefers;
    }

    public Double getTxtouts() {
        return txtouts;
    }

    public void setTxtouts(Double txtouts) {
        this.txtouts = txtouts;
    }

    public Double getTxretries() {
        return txretries;
    }

    public void setTxretries(Double txretries) {
        this.txretries = txretries;
    }

    public Double getTxfails() {
        return txfails;
    }

    public void setTxfails(Double txfails) {
        this.txfails = txfails;
    }

    public Double getSp_err() {
        return sp_err;
    }

    public void setSp_err(Double sp_err) {
        this.sp_err = sp_err;
    }

    public Double getLp_err() {
        return lp_err;
    }

    public void setLp_err(Double lp_err) {
        this.lp_err = lp_err;
    }

    public Double getTxrate() {
        return txrate;
    }

    public void setTxrate(Double txrate) {
        this.txrate = txrate;
    }

    public Double getTxstreams() {
        return txstreams;
    }

    public void setTxstreams(Double txstreams) {
        this.txstreams = txstreams;
    }

    public Double getTxmcs() {
        return txmcs;
    }

    public void setTxmcs(Double txmcs) {
        this.txmcs = txmcs;
    }

    public Double getRxrate() {
        return rxrate;
    }

    public void setRxrate(Double rxrate) {
        this.rxrate = rxrate;
    }

    public Double getRxstreams() {
        return rxstreams;
    }

    public void setRxstreams(Double rxstreams) {
        this.rxstreams = rxstreams;
    }

    public Double getRxmcs() {
        return rxmcs;
    }

    public void setRxmcs(Double rxmcs) {
        this.rxmcs = rxmcs;
    }

    public Double getEvm_0() {
        return evm_0;
    }

    public void setEvm_0(Double evm_0) {
        this.evm_0 = evm_0;
    }

    public Double getEm_1() {
        return em_1;
    }

    public void setEm_1(Double em_1) {
        this.em_1 = em_1;
    }

    public Double getEvm_2() {
        return evm_2;
    }

    public void setEvm_2(Double evm_2) {
        this.evm_2 = evm_2;
    }

    public Double getEvm_3() {
        return evm_3;
    }

    public void setEvm_3(Double evm_3) {
        this.evm_3 = evm_3;
    }

    public Double getRssi_0() {
        return rssi_0;
    }

    public void setRssi_0(Double rssi_0) {
        this.rssi_0 = rssi_0;
    }

    public Double getRssi_1() {
        return rssi_1;
    }

    public void setRssi_1(Double rssi_1) {
        this.rssi_1 = rssi_1;
    }

    public Double getRssi_2() {
        return rssi_2;
    }

    public void setRssi_2(Double rssi_2) {
        this.rssi_2 = rssi_2;
    }

    public Double getRssi_3() {
        return rssi_3;
    }

    public void setRssi_3(Double rssi_3) {
        this.rssi_3 = rssi_3;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getGps_gear() {
        return gps_gear;
    }

    public void setGps_gear(Double gps_gear) {
        this.gps_gear = gps_gear;
    }

    public Double getGps_sats() {
        return gps_sats;
    }

    public void setGps_sats(Double gps_sats) {
        this.gps_sats = gps_sats;
    }

    public Double getPer() {
        return per;
    }

    public void setPer(Double per) {
        this.per = per;
    }

    public Double getBw() {
        return bw;
    }

    public void setBw(Double bw) {
        this.bw = bw;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }


}
