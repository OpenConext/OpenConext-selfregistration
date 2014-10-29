package nl.surfnet.coin.selfregistration.web;

import nl.surfnet.coin.stoker.Stoker;
import nl.surfnet.coin.stoker.StokerEntry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

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
  }

  @Test
  public void testShowsExistingServiceProvidersForIdp() throws Exception {
    ModelAndView actual = inviteController.index();
    assertEquals("index", actual.getViewName());
    assertTrue("model not present", actual.getModelMap().containsAttribute("serviceProviders"));
  }

  @Test
  public void testDisplaysServiceProviderToInvite() throws Exception {
    when(stoker.getEduGainServiceProvider("ID")).thenReturn(new StokerEntry());

    ModelAndView actual = inviteController.invite("ID");
    assertEquals("invite", actual.getViewName());
    assertTrue("model not present", actual.getModelMap().containsAttribute("serviceProvider"));

  }
}
