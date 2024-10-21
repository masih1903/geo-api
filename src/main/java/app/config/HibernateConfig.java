package app.config;

import app.entities.Hotel;
import app.security.entities.User;
import app.security.entities.Role;
import app.utils.ApiProps;
import jakarta.persistence.EntityManagerFactory;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.util.Properties;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class HibernateConfig {

    // ========================= VARIABLES ==============================
    private static boolean IS_TEST = false;
    private static EntityManagerFactory entityManagerFactory;

    // ========================= PUBLIC METHODS =========================
    public static void setTest(boolean isTest) {
        IS_TEST = isTest;
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (IS_TEST) return getEntityManagerFactoryConfigTest();
        return getEntityManagerFactoryConfigDevelopment();
    }

    // ========================= PRIVATE METHODS =========================
    private static EntityManagerFactory getEntityManagerFactoryConfigDevelopment() {
        if (entityManagerFactory == null) entityManagerFactory = setupHibernateConfigurationForDevelopment();
        return entityManagerFactory;
    }

    public static EntityManagerFactory getEntityManagerFactoryConfigTest() {
        if (entityManagerFactory == null) entityManagerFactory = setupHibernateConfigurationForTesting();
        return entityManagerFactory;
    }

    private static EntityManagerFactory setupHibernateConfigurationForDevelopment() {
        try {
            Configuration configuration = new Configuration();
            Properties props = new Properties();
            hibernateDevelopmentConfiguration(props);
            hibernateBasicConfiguration(props);
            return getEntityManagerFactory(configuration, props);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static EntityManagerFactory setupHibernateConfigurationForTesting() {
        try {
            Configuration configuration = new Configuration();
            Properties props = new Properties();
            props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            props.put("hibernate.connection.driver_class", "org.testcontainers.jdbc.ContainerDatabaseDriver");
            props.put("hibernate.connection.url", "jdbc:tc:postgresql:15.3-alpine3.18:///test-db");
            props.put("hibernate.connection.username", "postgres");
            props.put("hibernate.connection.password", "postgres");
            props.put("hibernate.archive.autodetection", "class");
            props.put("hibernate.show_sql", "true");
            props.put("hibernate.hbm2ddl.auto", "create-drop");
            return getEntityManagerFactory(configuration, props);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static void hibernateDevelopmentConfiguration(Properties props) throws IOException {
        props.put("hibernate.connection.url", ApiProps.DB_URL);
        props.put("hibernate.connection.username", ApiProps.DB_USER);
        props.put("hibernate.connection.password", ApiProps.DB_PASS);
    }

    private static void hibernateBasicConfiguration(Properties props) {
        props.put("hibernate.show_sql", "false"); // show sql in console
        props.put("hibernate.format_sql", "false"); // format sql in console
        props.put("hibernate.use_sql_comments", "false"); // show sql comments in console
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"); // dialect for postgresql
        props.put("hibernate.connection.driver_class", "org.postgresql.Driver"); // driver class for postgresql
        props.put("hibernate.archive.autodetection", "class"); // hibernate scans for annotated classes
        props.put("hibernate.current_session_context_class", "thread"); // hibernate current session context
        props.put("hibernate.hbm2ddl.auto", "update"); // hibernate creates tables based on entities
    }

    private static EntityManagerFactory getEntityManagerFactory(Configuration configuration, Properties props) {
        configuration.setProperties(props);
        getAnnotationConfiguration(configuration);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        SessionFactory sf = configuration.buildSessionFactory(serviceRegistry);
        return sf.unwrap(EntityManagerFactory.class);
    }

    private static void getAnnotationConfiguration(Configuration configuration) {
        //configuration.addAnnotatedClass(Hotel.class);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Role.class);

    }

}
