package nl.surfnet.coin.selfregistration.web;

import nl.surfnet.coin.stoker.ContactPerson;
import nl.surfnet.coin.stoker.Stoker;
import nl.surfnet.coin.stoker.StokerEntry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class InviteControllerTest {

  public static final String ENTITY_ID = "ID";

  @InjectMocks
  InviteController inviteController;

  @Mock
  private Stoker stoker;

  private StokerEntry stokerEntry;

  @Before
  public void setUp() throws Exception {
    inviteController = new InviteController();
    MockitoAnnotations.initMocks(this);

    stokerEntry = new StokerEntry();
    stokerEntry.setEntityId(ENTITY_ID);
    stokerEntry.setContactPersons(Arrays.asList(new ContactPerson("technical", "surfnet", "", "surfnet@localhost.local", "", "")));
  }

  @Test
  public void testShowsExistingServiceProvidersForIdp() throws Exception {
    ModelAndView actual = inviteController.index();
    assertEquals("index", actual.getViewName());
    assertTrue("model not present", actual.getModelMap().containsAttribute("serviceProviders"));
  }

  @Test
  public void testDisplaysServiceProviderToInvite() throws Exception {
    when(stoker.getEduGainServiceProvider(ENTITY_ID)).thenReturn(stokerEntry);

    ModelAndView actual = inviteController.invite(ENTITY_ID);
    assertEquals("invite", actual.getViewName());
    assertTrue("model not present", actual.getModelMap().containsAttribute("serviceProvider"));

  }
}
