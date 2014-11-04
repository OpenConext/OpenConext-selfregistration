package nl.surfnet.coin.selfregistration.web;

import com.google.common.base.Optional;
import nl.surfnet.coin.selfregistration.invite.Invitation;
import nl.surfnet.coin.selfregistration.invite.InviteService;
import nl.surfnet.coin.selfregistration.model.ServiceProvider;
import org.hamcrest.collection.IsMapContaining;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.context.MessageSource;
import org.springframework.validation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.beans.PropertyEditor;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class AppControllerTest {


  public static final String ACCEPTED_INVITE_ID = "accepted_invite_id";
  public static final String INVITE_ID = "invite_id";
  @InjectMocks
  private AppController appController;

  @Mock
  private InviteService inviteService;

  @Mock
  private MessageSource messageSource;

  private RedirectAttributesModelMap redirectAttributes;

  private Invitation acceptedInvitation;
  private Invitation invitation;
  private ServiceProvider serviceProvider;

  @Before
  public void setUp() throws Exception {
    appController = new AppController();
    MockitoAnnotations.initMocks(this);
    redirectAttributes = new RedirectAttributesModelMap();
    acceptedInvitation = new Invitation("entityId", "foo@localhost.nl");
    acceptedInvitation.setUuid(ACCEPTED_INVITE_ID);
    invitation = new Invitation("entityId", "foo@localhost.nl");
    invitation.setUuid(INVITE_ID);
    acceptedInvitation.setAcceptedAt(new DateTime());
    serviceProvider = new ServiceProvider();
  }

  @Test
  public void testFailsWhenInvitationIdDoesNotExist() throws Exception {
    when(inviteService.get("not exists")).thenReturn(Optional.<Invitation>fromNullable(null));

    ModelAndView modelAndView = appController.home(serviceProvider, "not exists", redirectAttributes);

    assertEquals("404", modelAndView.getViewName());
  }

  @Test
  public void testDisplaysMessageWhenInvitationAlreadyAccepted() throws Exception {
    when(inviteService.get(ACCEPTED_INVITE_ID)).thenReturn(Optional.of(acceptedInvitation));
    when(messageSource.getMessage("invite.already_added", new Object[]{}, Locale.ENGLISH)).thenReturn("foobar");

    ModelAndView modelAndView = appController.home(serviceProvider, ACCEPTED_INVITE_ID, redirectAttributes);

    assertEquals(redirectAttributes.getFlashAttributes().get("flash.notice"), "foobar");
    assertEquals("new", modelAndView.getViewName());
  }

  @Test
  public void testDisplaysTheFormWhenAnInvitationExistsAndNotYetAccepted() throws Exception {
    when(inviteService.get(INVITE_ID)).thenReturn(Optional.of(invitation));

    ModelAndView modelAndView = appController.home(serviceProvider, INVITE_ID, redirectAttributes);
    assertThat(redirectAttributes.getFlashAttributes(), not(hasKey("flash.notice")));
    assertEquals("new", modelAndView.getViewName());
  }
}
