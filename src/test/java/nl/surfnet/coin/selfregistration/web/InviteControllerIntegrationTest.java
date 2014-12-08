package nl.surfnet.coin.selfregistration.web;

import nl.surfnet.coin.selfregistration.Application;
import nl.surfnet.coin.selfregistration.mock.InMemoryMail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.mail.internet.InternetAddress;

import static java.util.Arrays.asList;
import static nl.surfnet.coin.selfregistration.mock.InMemoryMail.inbox;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("unittest")
public class InviteControllerIntegrationTest {

  // in file 64db397e6f93619687d294bed6639c29.xml
  public static final String SP_ENTITY_ID = "http://saml.ps-ui-test.qalab.geant.net";
  @Autowired
  InviteController inviteController;

  private MockMvc mockMvc;

  @Autowired
  WebApplicationContext wac;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @After
  public void tearDown() throws Exception {
    InMemoryMail.empty();
  }

  @Test
  public void testInvitesSpContactPersons() throws Exception {
    this.mockMvc
      .perform(
        post("/fedops/invite")
          .param("spEntityId", SP_ENTITY_ID)
      )
      .andExpect(status().is3xxRedirection())
      .andExpect(redirectedUrl("/fedops"));

    assertEquals(1, inbox().size());
    assertThat(asList(inbox().get(0).getAllRecipients()), hasItem(new InternetAddress("it@dante.net")));
    assertThat(asList(inbox().get(0).getAllRecipients()), hasItem(new InternetAddress("DANTEITSupport@dante.net")));
    assertEquals("Please register your service provider", inbox().get(0).getSubject());
  }


}
