package nl.surfnet.coin.selfregistration.web;

import com.google.common.base.Optional;
import nl.surfnet.coin.selfregistration.adapters.ServiceRegistryAdapter;
import nl.surfnet.coin.selfregistration.invite.Invitation;
import nl.surfnet.coin.selfregistration.invite.InviteService;
import nl.surfnet.coin.selfregistration.adapters.OauthSettings;
import nl.surfnet.coin.selfregistration.adapters.ServiceProvider;
import nl.surfnet.coin.stoker.Stoker;
import nl.surfnet.coin.stoker.StokerEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/service-provider")
public class AppController extends BaseController {

  @Autowired
  private InviteService inviteService;

  @Autowired
  private Stoker stoker;

  @Autowired
  private ServiceRegistryAdapter serviceRegistry;

  @RequestMapping(value = "/{invitationId}", method = GET)
  public ModelAndView home(OauthSettings oauthSettings, @PathVariable String invitationId, RedirectAttributes redirectAttributes) {
    Optional<Invitation> invitation = inviteService.get(invitationId);
    if (invitation.isPresent()) {
      if (invitation.get().isAccepted()) {
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

    Optional<Invitation> invitation = inviteService.get(invitationId);
    if (invitation.isPresent()) {
      StokerEntry stokerEntry = stoker.getEduGainServiceProvider(invitation.get().getSpEntityId());
      notice(redirectAttributes, "serviceProvider.created");
      serviceRegistry.postConnection(ServiceProvider.from(stokerEntry).with(oauthSettings));
    } else {
      notice(redirectAttributes, "serviceProvider.notExist", invitation.get().getSpEntityId());
    }
    return "redirect:/service-provider/thanks";
  }

  @RequestMapping(value = "/thanks", method = GET)
  public ModelAndView thanks() {
    return new ModelAndView("thanks");
  }

}
