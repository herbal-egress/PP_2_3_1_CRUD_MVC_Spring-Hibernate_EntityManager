package mvc.spring.hiber.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@EnableJpaRepositories("mvc.spring.hiber.dao")
@EnableTransactionManagement
@PropertySource("classpath:db.properties")
@ComponentScan("mvc.spring.hiber") // где все файлы и папки нашейго проекта
public class DataBaseConfig {
    @Autowired
    private Environment env; // инжектим Environment из фреймворка

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManager() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(datasource());
        emf.setPackagesToScan(env.getRequiredProperty("db.entity.package"));
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setJpaProperties(getHibernateProperties());
        return emf;
    }

    // для управления транзакциями:
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager().getObject());
        return transactionManager;
    }

    // получаю все конфигурации hibernate из отдельного файла:
    @Bean
    public Properties getHibernateProperties() {
        Properties properties = new Properties();
               InputStream is = getClass().getClassLoader().getResourceAsStream("hibernate.properties");
        try {
            properties.load(is);
            return properties;
        } catch (IOException e) {
            throw new IllegalArgumentException("Не нашёл файл hibernate.properties", e);
        }
    }

    // указываю параметры подключения к БД для hibernate:
    @Bean
    public DataSource datasource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(env.getRequiredProperty("db.url"));
        ds.setDriverClassName(env.getRequiredProperty("db.driver"));
        ds.setUsername(env.getRequiredProperty("db.username"));
        ds.setPassword(env.getRequiredProperty("db.password"));
        // Свойства для DBCP - настройка пула соединений с БД.
        // Управляет соединениями с БД, исключая постоянные открытия/закрытия соединений:
        ds.setInitialSize(Integer.parseInt(env.getRequiredProperty("db.initialSize")));
        ds.setMinIdle(Integer.parseInt(env.getRequiredProperty("db.minIdle")));
        ds.setMaxIdle(Integer.parseInt(env.getRequiredProperty("db.maxIdle")));
        ds.setTimeBetweenEvictionRunsMillis(Long.parseLong(env.getRequiredProperty("db.timeBetweenEvictionRunsMillis")));
        ds.setMinEvictableIdleTimeMillis(Long.parseLong(env.getRequiredProperty("db.minEvictableIdleTimeMillis")));
        ds.setTestOnBorrow(Boolean.parseBoolean(env.getRequiredProperty("db.testOnBorrow")));
        ds.setValidationQuery(env.getRequiredProperty("db.validationQuery"));
        return ds;
    }
}