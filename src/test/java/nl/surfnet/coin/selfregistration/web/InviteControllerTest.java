package nl.surfnet.coin.selfregistration.web;

import nl.surfnet.coin.stoker.Stoker;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class InviteControllerTest {

  @InjectMocks
  InviteController inviteController;

  private MockMvc mockMvc;

  @Mock
  private Stoker stoker;

  @Before
  public void setUp() throws Exception {
    inviteController = new InviteController();
    MockitoAnnotations.initMocks(this);
    mockMvc = standaloneSetup(inviteController).build();
  }

  @Test
  public void testShowsExistingServiceProvidersForIdp() throws Exception {
    mockMvc
      .perform(get("/fedops"))
      .andExpect(model().attributeExists("serviceProviders"));

  }

}
