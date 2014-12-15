package nl.surfnet.coin.selfregistration;


import com.googlecode.flyway.core.Flyway;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import nl.surfnet.coin.selfregistration.adapters.ServiceRegistryAdapter;
import nl.surfnet.coin.selfregistration.adapters.ServiceRegistryRestClient;
import nl.surfnet.coin.selfregistration.invite.InviteDao;
import nl.surfnet.coin.selfregistration.invite.InviteService;
import nl.surfnet.coin.selfregistration.mock.InMemoryMail;
import nl.surfnet.coin.selfregistration.mock.LetterOpener;
import nl.surfnet.coin.selfregistration.mock.MockShibbolethFilter;
import nl.surfnet.coin.selfregistration.mock.ServiceRegistryMock;
import nl.surfnet.coin.selfregistration.web.shibboleth.ShibbolethPreAuthenticatedProcessingFilter;
import nl.surfnet.coin.selfregistration.web.shibboleth.ShibbolethUserDetailService;
import nl.surfnet.coin.stoker.Stoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
@EnableAutoConfiguration
@ComponentScan("nl.surfnet.coin.selfregistration")
public class Application extends WebMvcConfigurerAdapter {

  public static void main(String[] args) {
    new SpringApplicationBuilder(Application.class).run(args);
  }

  @Value("${db.user}")
  private String user;

  @Value("${db.password}")
  private String password;

  @Value("${db.jdbcUrl}")
  private String jdbcUrl;

  @Value("${invitation.baseUrl}")
  private String invitationBaseUrl;

  @Autowired
  private MessageSource messageSource;


  @Bean
  public Stoker stoker(@Value("${stoker.metaDataLocation}") Resource metaDataFileLocation, @Value("${stoker.location}") Resource stokerLocation) throws Exception {
    return new Stoker(metaDataFileLocation, stokerLocation);
  }

  @Bean
  public DataSource dataSource()
    throws PropertyVetoException {

    ComboPooledDataSource dataSource = new ComboPooledDataSource();
    dataSource.setUser(user);
    dataSource.setPassword(password);
    dataSource.setJdbcUrl(jdbcUrl);
    dataSource.setDriverClass("com.mysql.jdbc.Driver");
    dataSource.setAcquireIncrement(5);
    dataSource.setMinPoolSize(5);
    dataSource.setMaxPoolSize(5);
    dataSource.setMaxConnectionAge(60);
    dataSource.setAcquireRetryAttempts(3);
    dataSource.setAcquireRetryDelay(1000);
    return dataSource;
  }

  @Bean(initMethod = "migrate")
  public Flyway flyway() throws PropertyVetoException {
    Flyway flyway = new Flyway();
    flyway.setDataSource(dataSource());
    flyway.setLocations("/db/migrations");
    return flyway;
  }

  @Bean
  public InviteDao inviteDao() throws PropertyVetoException {
    return new InviteDao(dataSource());
  }

  @Bean
  public InviteService inviteService(JavaMailSender javaMailSender) throws PropertyVetoException {
    return new InviteService(inviteDao(), dataSource(), javaMailSender, messageSource, invitationBaseUrl);
  }


  @Configuration
  @EnableWebSecurity
  protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationSecurity.class);

    @Override
    public void configure(WebSecurity web) throws Exception {
      web.
        ignoring()
        .antMatchers("/css.**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      LOG.info("Configuring application security using {}", authenticationManagerBean().getClass());
      ShibbolethPreAuthenticatedProcessingFilter filter = new ShibbolethPreAuthenticatedProcessingFilter();
      filter.setAuthenticationManager(authenticationManagerBean());
      http.addFilterBefore(filter, AbstractPreAuthenticatedProcessingFilter.class);
      http
        .authorizeRequests()
        .antMatchers("/*").permitAll()
        .antMatchers("/css/**").permitAll()
        .antMatchers("/fedops").hasAnyRole("USER");
      http.csrf();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      LOG.info("Configuring AuthenticationManager with a PreAuthenticatedAuthenticationProvider");
      PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
      authenticationProvider.setPreAuthenticatedUserDetailsService(new ShibbolethUserDetailService());

      auth.authenticationProvider(authenticationProvider);
    }
  }

  @Configuration
  @Profile({"dev", "production", "test"})
  protected static class ProductionAndDev {

    @Value("${sr.protocol}")
    private String protocol;

    @Value("${sr.hostname}")
    private String hostname;

    @Value("${sr.port}")
    private int port;

    @Value("${sr.username}")
    private String username;

    @Value("${sr.password}")
    private String password;


    @Bean
    public ServiceRegistryAdapter serviceRegistryAdapter() {
      return new ServiceRegistryRestClient(protocol, hostname, port, username, password);
    }


  }

  @Configuration
  @Profile("production")
  protected static class Production {
    @Bean
    public JavaMailSender mail() {
      return new JavaMailSenderImpl();
    }
  }

  @Configuration
  @Profile("unittest")
  protected static class UnitTest {
    @Bean
    public JavaMailSender mail() {
      return new InMemoryMail();
    }

    @Bean
    public ServiceRegistryAdapter serviceRegistryAdapter() {
      return new ServiceRegistryMock();
    }

  }


  @Configuration
  @Profile("test")
  protected static class Test {
    @Bean
    public JavaMailSender mail() {
      return new JavaMailSenderImpl();
    }

  }

  @Configuration
  @Profile({"dev", "unittest"})
  protected static class DevelopmentAndUnitTest {

    @Bean
    public FilterRegistrationBean mockShibbolethFilter() {
      FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
      filterRegistrationBean.setFilter(new MockShibbolethFilter());
      filterRegistrationBean.addUrlPatterns("/*");
      return filterRegistrationBean;
    }

  }

  @Configuration
  @Profile("dev")
  protected static class Development {

    @Bean
    public JavaMailSender mail() {
      return new LetterOpener();
    }

  }
}
