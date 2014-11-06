package nl.surfnet.coin.selfregistration.web;

import nl.surfnet.coin.selfregistration.Application;
import nl.surfnet.coin.selfregistration.adapters.ServiceProvider;
import nl.surfnet.coin.selfregistration.adapters.ServiceRegistryRestClientException;
import nl.surfnet.coin.selfregistration.invite.Invitation;
import nl.surfnet.coin.selfregistration.invite.InviteDao;
import nl.surfnet.coin.selfregistration.mock.ServiceRegistryMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.net.URLEncoder;

import static nl.surfnet.coin.selfregistration.web.TestInstances.*;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class ServiceProviderControllerIntegrationTest {

  // in file 64db397e6f93619687d294bed6639c29.xml
  public static final String SP_ENTITY_ID = "http://saml.ps-ui-test.qalab.geant.net";

  public static final String CSRF_TOKEN_PARAM_NAME = "_csrf";
  public static final String TOKEN_STRING = "foo";
  private String CSRF_TOKEN_SESSION_NAME = HttpSessionCsrfTokenRepository.class.getName().concat(".CSRF_TOKEN");

  @Autowired
  ServiceProviderController serviceProviderController;

  private MockMvc mockMvc;

  @Autowired
  WebApplicationContext wac;

  @Autowired
  FilterChainProxy springSecurityFilterChain;

  @Autowired
  DataSource dataSource;

  @Autowired
  InviteDao inviteDao;

  @Autowired
  ServiceRegistryMock serviceRegistryAdapter;

  private JdbcTemplate jdbcTemplate;


  @Before
  public void setUp() throws Exception {
    jdbcTemplate = new JdbcTemplate(dataSource);
    this.mockMvc = MockMvcBuilders
      .webAppContextSetup(wac)
      .addFilter(springSecurityFilterChain)
      .build();
  }

  @After
  public void tearDown() throws Exception {
    this.serviceRegistryAdapter.reset();
    SecurityContextHolder.clearContext();
    jdbcTemplate.update("delete from invitations");
  }

  @Test
  public void testGetsAnEmptyForm() throws Exception {
    this.mockMvc
      .perform(
        get("/service-provider/foo")
      )
      .andExpect(model().attributeExists("oauthSettings"));
  }

  @Test
  public void testPostToServiceProviderWithErrors() throws Exception {
    this.mockMvc
      .perform(
        post("/service-provider/foo")
          .param(CSRF_TOKEN_PARAM_NAME, TOKEN_STRING)
          .sessionAttr("oauthSettings", emptyOauthSettings())
          .sessionAttr(CSRF_TOKEN_SESSION_NAME, new DefaultCsrfToken(CSRF_TOKEN_SESSION_NAME, CSRF_TOKEN_PARAM_NAME, TOKEN_STRING))
      )
      .andExpect(
        model()
          .attributeHasFieldErrors("oauthSettings", "secret")
      )
      .andExpect(
        model()
          .attributeHasFieldErrors("oauthSettings", "consumerKey")
      )
      .andExpect(
        model()
          .attributeHasFieldErrors("oauthSettings", "callbackUrl")
      );
  }

  @Test
  public void testPostNewServiceProviderSuccess() throws Exception {

    postValidOauthSettings()
      .andExpect(
        model().hasNoErrors()
      )
      .andExpect(
        redirectedUrl("/service-provider/thanks")
      )
      .andExpect(
        flash().attribute("flash.notice", is("Service created"))
      );
  }

  @Test
  public void testDisplaysErrorPageWhenCallToServiceRegistryWithServiceRegistryRestClientException() throws Exception {

    throwExceptionOnPostConnection(new ServiceRegistryRestClientException("foo", 404));

    postValidOauthSettings()
      .andExpect(view().name("error"));

  }

  @Test
  public void testDisplaysErrorPageWhenCallToServiceRegistryWithRuntimeException() throws Exception {

    throwExceptionOnPostConnection(new RuntimeException("foo"));

    postValidOauthSettings()
      .andExpect(view().name("error"));

  }

  @Test
  public void testDisplaysThankYouPage() throws Exception {
    this.mockMvc
      .perform(
        get("/service-provider/thanks")
      )
      .andExpect(view().name("thanks"));
  }

  private ResultActions postValidOauthSettings() throws Exception {
    Invitation invitation = persistInvitation();
    return this.mockMvc
      .perform(
        post("/service-provider/" + URLEncoder.encode(invitation.getUuid(), "UTF-8"))
          .param(CSRF_TOKEN_PARAM_NAME, TOKEN_STRING)
          .param("callbackUrl", validOauthSettings().getCallbackUrl())
          .param("secret", validOauthSettings().getSecret())
          .param("consumerKey", validOauthSettings().getConsumerKey())
          .sessionAttr(CSRF_TOKEN_SESSION_NAME, new DefaultCsrfToken(CSRF_TOKEN_SESSION_NAME, CSRF_TOKEN_PARAM_NAME, TOKEN_STRING))
      );
  }


  private Invitation persistInvitation() {
    Invitation invitation = newInvitation(SP_ENTITY_ID);
    inviteDao.persist(invitation);
    return invitation;
  }

  private void throwExceptionOnPostConnection(final RuntimeException exception) {
    serviceRegistryAdapter.mocker = new ServiceRegistryMock.Mocker() {
      @Override
      public void execute(ServiceProvider serviceProvider) {
        throw exception;
      }
    };
  }

}


