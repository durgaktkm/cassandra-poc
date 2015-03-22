package co.mimosa.cassandra.config;

import co.mimosa.cassandra.AsyncCassandraOperations;
import co.mimosa.cassandra.kafka.KafkaEventAnalyzer;
import co.mimosa.cassandra.parser.PhystatsParser;
import co.mimosa.cassandra.repository.RawMetricsRepository;
import co.mimosa.kafka.config.CoreConsumerConfig;
import co.mimosa.kafka.consumer.KafkaMultiThreadedConsumer;
import co.mimosa.kafka.producer.MimosaProducer;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.config.java.AbstractSessionConfiguration;
import org.springframework.cassandra.core.CqlOperations;
import org.springframework.cassandra.core.CqlTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.convert.CassandraConverter;
import org.springframework.data.cassandra.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Created by ramdurga on 3/18/15.
 */
@Configuration
@PropertySource(value = { "classpath:cassandra.properties","file:cassandra_override.properties"}, ignoreResourceNotFound = true )
@EnableCassandraRepositories(basePackages = { "co.mimosa.cassandra" })
//@EnableJpaRepositories("co.mimosa.cassandra.repository")
//@EnableJpaRepositories
@EnableAsync
public class NMSKafkaConfig {
    @Autowired
    private Environment env;

    protected String getKeyspaceName() {
        //return "NMSCassandraDB";
        return "pocdb";
    }
    @Bean
    public CassandraClusterFactoryBean cluster() {

        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(env.getProperty("cassandra.contactpoints"));
        cluster.setPort(Integer.parseInt(env.getProperty("cassandra.port")));
        cluster.setPoolingOptions(new PoolingOptions().setMaxConnectionsPerHost(HostDistance.LOCAL, Integer.parseInt(env.getProperty("cassandra.poolSize"))));
        return cluster;
    }

    @Bean
    public CassandraMappingContext mappingContext() {
        return new BasicCassandraMappingContext();
    }

    @Bean
    public CassandraConverter converter() {
        return new MappingCassandraConverter(mappingContext());
    }

    @Bean
    public CassandraSessionFactoryBean session() throws Exception {

        CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
        session.setCluster(cluster().getObject());
        session.setKeyspaceName(env.getProperty("cassandra.keyspace"));
        session.setConverter(converter());
        session.setSchemaAction(SchemaAction.CREATE);
        return session;
    }
    @Bean
    public CqlOperations cqlTemplate() throws Exception {
        return new CqlTemplate(session().getObject());
    }
    @Bean
    public CassandraOperations cassandraOperations() throws Exception {
        return new CassandraTemplate(session().getObject());
    }



    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("MyExecutor-");
        executor.initialize();
        return executor;
    }
    @Bean
    public S3ClientOptions s3ClientOptions(){
        S3ClientOptions s3ClientOptions = new S3ClientOptions();
        s3ClientOptions.setPathStyleAccess(true);
        return s3ClientOptions;
    }

    @Bean
    public AmazonS3Client amazonS3Client(){
        AmazonS3Client amazonS3Client = new AmazonS3Client();
        amazonS3Client.setEndpoint(env.getProperty("amazon.s3.endPoint"));
        amazonS3Client.setS3ClientOptions(s3ClientOptions());
        return amazonS3Client;
    }
    @Bean
    public ObjectMapper objectMapper(){
       return new ObjectMapper();
    }
    @Bean
    public PhystatsParser phystatsParser(){
        return new PhystatsParser();
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        int poolSize = Integer.parseInt(env.getProperty("kafka.core.pool.size.deviceData"));
        threadPoolTaskExecutor.setCorePoolSize(poolSize);
        threadPoolTaskExecutor.setMaxPoolSize(poolSize);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        return threadPoolTaskExecutor;
    }

    @Bean
    public MimosaProducer mimosaProducer(){
        MimosaProducer mimosaProducer = new MimosaProducer(env.getProperty("kafka.broker.ids"));
        return mimosaProducer;
    }
    @Bean
    public CoreConsumerConfig coreConsumerConfig(){
        CoreConsumerConfig coreConsumerConfig = new CoreConsumerConfig();
        coreConsumerConfig.setAutoCommitEnable(env.getProperty("autoCommitEnable"));
        coreConsumerConfig.setAutoCommitIntervalMs(env.getProperty("autoCommitIntervalMs"));
        coreConsumerConfig.setAutoOffsetReset(env.getProperty("autoOffsetReset"));
        coreConsumerConfig.setFetchMessageMaxBytes(env.getProperty("fetchMessageMaxBytes"));
        coreConsumerConfig.setExcludeInternalTopics(env.getProperty("excludeInternalTopics"));
        coreConsumerConfig.setFetchMinBytes(env.getProperty("fetchMinBytes"));
        coreConsumerConfig.setFetchWaitMaxMs(env.getProperty("fetchWaitMaxMs"));
        coreConsumerConfig.setConsumerTimeoutMs(env.getProperty("consumerTimeoutMs"));
        coreConsumerConfig.setSocketTimeoutMs(env.getProperty("socketTimeoutMs"));
        coreConsumerConfig.setSocketReceiveBufferBytes(env.getProperty("socketReceiveBufferBytes"));
        coreConsumerConfig.setOffsetsChannelBackoffMs(env.getProperty("offsetsChannelBackoffMs"));
        coreConsumerConfig.setOffsetsChannelSocketTimeoutMs(env.getProperty("offsetsChannelSocketTimeoutMs"));
        coreConsumerConfig.setOffsetsCommitMaxRetries(env.getProperty("offsetsCommitMaxRetries"));
        coreConsumerConfig.setOffsetsStorage(env.getProperty("offsetsStorage"));
        coreConsumerConfig.setPartitionAssignmentStrategy(env.getProperty("partitionAssignmentStrategy"));
        coreConsumerConfig.setQueuedMaxMessageChunks(env.getProperty("queuedMaxMessageChunks"));
        coreConsumerConfig.setRebalanceBackoffMs(env.getProperty("rebalanceBackoffMs"));
        coreConsumerConfig.setRebalanceMaxRetries(env.getProperty("rebalanceMaxRetries"));
        coreConsumerConfig.setRefreshLeaderBackoffMs(env.getProperty("refreshLeaderBackoffMs"));
        coreConsumerConfig.setSocketReceiveBufferBytes(env.getProperty("socketReceiveBufferBytes"));
        coreConsumerConfig.setZookeeperConnectionTimeoutMs(env.getProperty("zookeeperConnectionTimeoutMs"));
        coreConsumerConfig.setZookeeperSessionTimeOutMs(env.getProperty("zookeeperSessionTimeOutMs"));
        coreConsumerConfig.setZookeeperSyncTimeMs(env.getProperty("zookeeperSyncTimeMs"));
        return coreConsumerConfig;
    }
    @Bean
    public AsyncCassandraOperations asyncCassandraOperations(){
        return new AsyncCassandraOperations();
    }
    @Bean
    public KafkaEventAnalyzer kafkaEventAnalyzer(){
        KafkaEventAnalyzer kafkaEventAnalyzer = new KafkaEventAnalyzer(env.getProperty("NEWLINE_REPLACEMENT"),env.getProperty("DIR_SEPARATOR"),objectMapper(),mimosaProducer(),env.getProperty("errorTopic"),phystatsParser(),asyncCassandraOperations());
        return kafkaEventAnalyzer;
    }
    @Bean
    public KafkaMultiThreadedConsumer kafkaMultiThreadedConsumer(){
        KafkaMultiThreadedConsumer kafkaMultiThreadedConsumer = new KafkaMultiThreadedConsumer();
        kafkaMultiThreadedConsumer.setTopic(env.getProperty("topic"));
        kafkaMultiThreadedConsumer.setCoreConsumerConfig(coreConsumerConfig());
        kafkaMultiThreadedConsumer.setEventAnalyzer(kafkaEventAnalyzer());
        kafkaMultiThreadedConsumer.setExecutorService(taskExecutor());
        kafkaMultiThreadedConsumer.setGroupId(env.getProperty("groupId"));
        kafkaMultiThreadedConsumer.setPhase(Integer.parseInt(env.getProperty("phase")));
        kafkaMultiThreadedConsumer.setNumThreads(env.getProperty("kafka.core.pool.size.deviceData"));
        kafkaMultiThreadedConsumer.setZookeeperConnection(env.getProperty("zookeeperConnection"));
        return kafkaMultiThreadedConsumer;
    }


}