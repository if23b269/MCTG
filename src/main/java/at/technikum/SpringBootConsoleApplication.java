package at.technikum;

import at.technikum.DAL.DAO.User;
import at.technikum.httpserver.server.Server;
import at.technikum.persistence.UserDBService;
import at.technikum.persistence.UserRepository;
import jakarta.persistence.EntityManagerFactory;
import org.postgresql.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.DatabaseStartupValidator;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.stream.Stream;

@SpringBootApplication(scanBasePackages={
        "at.technikum", "at.technikum"})
public class SpringBootConsoleApplication implements CommandLineRunner {
    private Driver driver;
    private static final Logger log = LoggerFactory.getLogger(SpringBootConsoleApplication.class);

    @Autowired
    UserRepository userRepository;

    @Bean
    public DatabaseStartupValidator databaseStartupValidator(DataSource dataSource) {
        var dsv = new DatabaseStartupValidator();
        dsv.setDataSource(dataSource);
        dsv.setValidationQuery(DatabaseDriver.POSTGRESQL.getValidationQuery());
        return dsv;
    }

    @Bean
    public static BeanFactoryPostProcessor dependsOnPostProcessor() {
        return bf -> {
            // Let beans that need the database depend on the DatabaseStartupValidator
            // like the JPA EntityManagerFactory or Flyway

            String[] jpa = bf.getBeanNamesForType(EntityManagerFactory.class);
            Stream.of(jpa)
                    .map(bf::getBeanDefinition)
                    .forEach(it -> it.setDependsOn("databaseStartupValidator"));
        };
    }



    @Override
    public void run(String... args) throws Exception {
        log.info("EXECUTING : command line runner");

        for (int i = 0; i < args.length; ++i) {
            log.info("args[{}]: {}", i, args[i]);
        }
        //userRepository.save(new User("User4","pwd4"));
        //System.out.println("----------All Data saved into Database----------------------");

        Server server = new Server(10001, Main.configureRouter());
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
