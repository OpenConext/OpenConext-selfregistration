package nl.surfnet.coin.selfregistration.web;

import com.google.common.base.Optional;
import nl.surfnet.coin.selfregistration.invite.Invitation;
import nl.surfnet.coin.selfregistration.invite.InviteService;
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
public class AppController {

  @Autowired
  private InviteService inviteService;

  @Autowired
  private MessageSource messageSource;

  @RequestMapping(value = "/{invitationId}", method = GET)
  public ModelAndView home(ServiceProvider serviceProvider, @PathVariable String invitationId, RedirectAttributes redirectAttributes) {
    Optional<Invitation> invitation = inviteService.get(invitationId);
    if(invitation.isPresent()) {
      if(invitation.get().isAccepted()) {
        redirectAttributes.addFlashAttribute("flash.notice", messageSource.getMessage("invite.already_added", new Object[]{}, Locale.ENGLISH));
      }
      return new ModelAndView("new");
    } else {
      return new ModelAndView("404");
    }
  }

  @RequestMapping(value = "/{invitationId}", method = POST)
  public String post(@Valid ServiceProvider serviceProvider, BindingResult bindingResult, @PathVariable String invitationId) {
    if (bindingResult.hasErrors()) {
      return "new";
    }
    return "redirect:/";
  }
}
