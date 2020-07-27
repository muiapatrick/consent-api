package com.techminia.collection.config.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "collectionDbManagerFactory", transactionManagerRef = "collectionDbTransactionManager",
        basePackages = {"com.techminia.collection" })
public class PostgresDbConfig {
    @Bean(name = "collectionDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource collectionDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        return dataSource;
    }

    @Bean(name = "collectionDbManagerFactory")
    public LocalContainerEntityManagerFactoryBean collectionDbManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("collectionDataSource") DataSource collectionDataSource) {
        return builder.dataSource(collectionDataSource).packages("com.techminia.collection")
                .persistenceUnit("collectionDatabase").build();
    }

    @Bean(name = "collectionDbTransactionManager")
    public PlatformTransactionManager collectionDbTransactionManager(
            @Qualifier("collectionDbManagerFactory") EntityManagerFactory collectionDbManagerFactory) {
        return new JpaTransactionManager(collectionDbManagerFactory);
    }
}
