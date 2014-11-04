package nl.surfnet.coin.selfregistration.web;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Ordering;
import nl.surfnet.coin.selfregistration.invite.Invitation;
import nl.surfnet.coin.selfregistration.invite.InviteService;
import nl.surfnet.coin.stoker.ContactPerson;
import nl.surfnet.coin.stoker.Stoker;
import nl.surfnet.coin.stoker.StokerEntry;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/fedops")
public class InviteController extends BaseController {

  private static class ServiceProviderOrderer {
    private final Collection<StokerEntry> serviceProviders;

    private final static Ordering<StokerEntry> byDisplayNameEn = new Ordering<StokerEntry>() {
      @Override
      public int compare(StokerEntry left, StokerEntry right) {
        return left.getDisplayNameEn().compareTo(right.getDisplayNameEn());
      }
    };

    private ServiceProviderOrderer(Collection<StokerEntry> serviceProviders) {
      this.serviceProviders = serviceProviders;
    }

    public Collection<StokerEntry> ordered() {
      return byDisplayNameEn.sortedCopy(serviceProviders);
    }
  }


  @Autowired
  private Stoker stoker;

  @Autowired
  private InviteService inviteService;

  @RequestMapping(method = GET)
  public ModelAndView index() {
    return new ModelAndView("index", "serviceProviders", new ServiceProviderOrderer(stoker.getEduGainServiceProviders()).ordered());
  }

  @RequestMapping(value = "/invite", method = GET)
  public ModelAndView invite(@RequestParam("spEntityId") String spEntityId) {
    return new ModelAndView("invite", "serviceProvider", stoker.getEduGainServiceProvider(spEntityId));
  }

  @RequestMapping(value = "/invite", method = POST)
  public String doInvite(@RequestParam String spEntityId, RedirectAttributes redirectAttributes) {
    StokerEntry serviceProvider = stoker.getEduGainServiceProvider(spEntityId);
    String[] to = mailTo(serviceProvider);
    final Invitation invitation = new Invitation(spEntityId, StringUtils.join(to, ","));
    inviteService.perEmail(invitation, to);
    notice(redirectAttributes, "invite.invited", spEntityId);
    return "redirect:/fedops";
  }

  private String[] mailTo(StokerEntry serviceProvider) {
    Collection<ContactPerson> contactPersons = serviceProvider.getContactPersons();
    return Collections2.transform(contactPersons, new Function<ContactPerson, String>() {
      @Override
      public String apply(ContactPerson input) {
        return input.getEmailAddress();
      }
    }).toArray(new String[contactPersons.size()]);
  }
}
