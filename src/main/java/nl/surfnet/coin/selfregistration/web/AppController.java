package nl.surfnet.coin.selfregistration.web;

import com.google.common.base.Optional;
import nl.surfnet.coin.selfregistration.invite.Invitation;
import nl.surfnet.coin.selfregistration.invite.InviteService;
import nl.surfnet.coin.selfregistration.model.OauthSettings;
import nl.surfnet.coin.selfregistration.model.ServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/service-provider")
public class AppController extends BaseController {

  @Autowired
  private InviteService inviteService;

  @RequestMapping(value = "/{invitationId}", method = GET)
  public ModelAndView home(OauthSettings oauthSettings, @PathVariable String invitationId, RedirectAttributes redirectAttributes) {
    Optional<Invitation> invitation = inviteService.get(invitationId);
    if(invitation.isPresent()) {
      if(invitation.get().isAccepted()) {
        notice(redirectAttributes, "invite.already_added");
      }
      return new ModelAndView("new", "invitationId", invitationId);

    } else {
      return new ModelAndView("404");
    }
  }

  @RequestMapping(value = "/{invitationId}", method = POST)
  public String post(@Valid OauthSettings oauthSettings, BindingResult bindingResult, @PathVariable String invitationId, RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      return "new";
    }
    notice(redirectAttributes, "serviceProvider.created");
    return "redirect:/";
  }
}
