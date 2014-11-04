package nl.surfnet.coin.selfregistration.web;

import nl.surfnet.coin.selfregistration.Application;
import nl.surfnet.coin.selfregistration.model.OauthSettings;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class AppControllerIntegrationTest {

  public static final String CSRF_TOKEN_PARAM_NAME = "_csrf";
  public static final String TOKEN_STRING = "foo";
  private String CSRF_TOKEN_SESSION_NAME = HttpSessionCsrfTokenRepository.class.getName().concat(".CSRF_TOKEN");

  @Autowired
  AppController appController;

  private MockMvc mockMvc;

  @Autowired
  WebApplicationContext wac;

  @Autowired
  FilterChainProxy springSecurityFilterChain;


  @Before
  public void setUp() throws Exception {
    this.mockMvc = MockMvcBuilders
      .webAppContextSetup(wac)
      .addFilter(springSecurityFilterChain)
      .build();
  }

  @After
  public void tearDown() throws Exception {
    SecurityContextHolder.clearContext();
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
  public void testPostToServiceProvider() throws Exception {
    this.mockMvc
      .perform(
        post("/service-provider/foo")
          .param(CSRF_TOKEN_PARAM_NAME, TOKEN_STRING)
          .sessionAttr("oauthSettings", new OauthSettings())
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
}
