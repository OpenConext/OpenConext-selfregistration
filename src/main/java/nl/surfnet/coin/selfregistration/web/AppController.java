package nl.surfnet.coin.selfregistration.web;

import nl.surfnet.coin.selfregistration.model.ServiceProvider;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import java.util.ArrayList;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/service-provider")
public class AppController {

  @RequestMapping(value = "/{invitationId}", method = GET)
  public ModelAndView home(ServiceProvider serviceProvider, @PathVariable String invitationId) {
    return new ModelAndView("new");
  }

  @RequestMapping(value = "", method = POST)
  public String post(@Valid ServiceProvider serviceProvider, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      System.out.println(bindingResult);
      return "new";
    } else {
      System.out.println(serviceProvider);
    }
    return "redirect:/";
  }
}
