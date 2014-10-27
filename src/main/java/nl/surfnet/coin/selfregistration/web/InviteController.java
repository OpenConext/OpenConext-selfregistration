package nl.surfnet.coin.selfregistration.web;

import com.google.common.collect.Ordering;
import nl.surfnet.coin.selfregistration.model.ServiceProvider;
import nl.surfnet.coin.stoker.Stoker;
import nl.surfnet.coin.stoker.StokerEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/fedops")
public class InviteController {


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

  @RequestMapping(method = GET)
  public ModelAndView index() {
    return new ModelAndView("index", "serviceProviders", new ServiceProviderOrderer(stoker.getEduGainServiceProviders()).ordered());
  }

}
