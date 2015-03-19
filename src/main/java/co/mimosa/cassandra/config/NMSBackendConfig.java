package co.mimosa.cassandra.config;

import co.mimosa.cassandra.parser.PhystatsParser;
import co.mimosa.cassandra.repository.RawMetricsRepository;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Map;

/**
 * Created by ramdurga on 3/18/15.
 */
@Configuration
@PropertySource(value = { "classpath:cassandra.properties" })
@EnableCassandraRepositories(basePackages = { "co.mimosa.cassandra.repository" })
//@EnableJpaRepositories("co.mimosa.cassandra.repository")
//@EnableJpaRepositories
public class NMSBackendConfig  {
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
        cluster.setPoolingOptions(new PoolingOptions().setMaxConnectionsPerHost(HostDistance.LOCAL, 100));
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
    @Bean
    public PhystatsParser getPhystatsParser() throws Exception {
        return new PhystatsParser();
    }

}
