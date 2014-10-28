package nl.surfnet.coin.selfregistration;


import nl.surfnet.coin.selfregistration.web.shibboleth.ShibbolethPreAuthenticatedProcessingFilter;
import nl.surfnet.coin.selfregistration.web.shibboleth.ShibbolethUserDetailService;
import nl.surfnet.coin.selfregistration.web.shibboleth.mock.MockShibbolethFilter;
import nl.surfnet.coin.stoker.Stoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;

@Configuration
@EnableAutoConfiguration
@ComponentScan("nl.surfnet.coin.selfregistration")
public class Application extends WebMvcConfigurerAdapter {

  public static void main(String[] args) {
    new SpringApplicationBuilder(Application.class).run(args);
  }

  @Bean
  public Stoker stoker(@Value("${stoker.metaDataLocation}") Resource metaDataFileLocation, @Value("${stoker.location}") Resource stokerLocation) throws Exception {
    return new Stoker(metaDataFileLocation, stokerLocation);
  }


  @Configuration
  @EnableWebSecurity
  protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationSecurity.class);

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      LOG.info("Configuring application security using {}", authenticationManagerBean().getClass());
      ShibbolethPreAuthenticatedProcessingFilter filter = new ShibbolethPreAuthenticatedProcessingFilter();
      filter.setAuthenticationManager(authenticationManagerBean());
      http.addFilterBefore(filter, AbstractPreAuthenticatedProcessingFilter.class);
      http.authorizeRequests().anyRequest().hasAnyRole("USER");
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
  @Profile("dev")
  protected static class DevelopmentSecurity {

    @Bean
    public FilterRegistrationBean mockShibbolethFilter() {
      FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
      filterRegistrationBean.setFilter(new MockShibbolethFilter());
      filterRegistrationBean.addUrlPatterns("/*");
      return filterRegistrationBean;
    }
  }
}
