package nl.surfnet.coin.selfregistration.web;

import com.google.common.base.Optional;
import nl.surfnet.coin.selfregistration.invite.Invitation;
import nl.surfnet.coin.selfregistration.invite.InviteService;
import nl.surfnet.coin.selfregistration.adapters.OauthSettings;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.Locale;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ServiceProviderControllerTest {


  public static final String ACCEPTED_INVITE_ID = "accepted_invite_id";
  public static final String INVITE_ID = "invite_id";
  @InjectMocks
  private ServiceProviderController serviceProviderController;

  @Mock
  private InviteService inviteService;

  @Mock
  private MessageSource messageSource;

  private RedirectAttributesModelMap redirectAttributes;

  private Invitation acceptedInvitation;
  private Invitation invitation;
  private OauthSettings oauthSettings;

  @Before
  public void setUp() throws Exception {
    serviceProviderController = new ServiceProviderController();
    MockitoAnnotations.initMocks(this);
    redirectAttributes = new RedirectAttributesModelMap();
    acceptedInvitation = new Invitation("entityId", "foo@localhost.nl");
    acceptedInvitation.setUuid(ACCEPTED_INVITE_ID);
    invitation = new Invitation("entityId", "foo@localhost.nl");
    invitation.setUuid(INVITE_ID);
    acceptedInvitation.setAcceptedAt(new DateTime());
    oauthSettings = new OauthSettings();
  }

  @Test
  public void testFailsWhenInvitationIdDoesNotExist() throws Exception {
    when(inviteService.get("not exists")).thenReturn(Optional.<Invitation>fromNullable(null));

    ModelAndView modelAndView = serviceProviderController.home(oauthSettings, "not exists", redirectAttributes);

    assertEquals("404", modelAndView.getViewName());
  }

  @Test
  public void testDisplaysMessageWhenInvitationAlreadyAccepted() throws Exception {
    when(inviteService.get(ACCEPTED_INVITE_ID)).thenReturn(Optional.of(acceptedInvitation));
    when(messageSource.getMessage("invite.already_added", new Object[]{}, Locale.ENGLISH)).thenReturn("foobar");

    ModelAndView modelAndView = serviceProviderController.home(oauthSettings, ACCEPTED_INVITE_ID, redirectAttributes);

    assertEquals(redirectAttributes.getFlashAttributes().get("flash.notice"), "foobar");
    assertEquals("new", modelAndView.getViewName());
  }

  @Test
  public void testDisplaysTheFormWhenAnInvitationExistsAndNotYetAccepted() throws Exception {
    when(inviteService.get(INVITE_ID)).thenReturn(Optional.of(invitation));

    ModelAndView modelAndView = serviceProviderController.home(oauthSettings, INVITE_ID, redirectAttributes);
    assertThat(redirectAttributes.getFlashAttributes(), not(hasKey("flash.notice")));
    assertEquals("new", modelAndView.getViewName());
  }
}
