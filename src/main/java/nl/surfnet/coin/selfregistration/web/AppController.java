package nl.surfnet.coin.selfregistration.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppController {

  @RequestMapping("/")
  public ModelAndView home() {
    return new ModelAndView("index");
  }
}
