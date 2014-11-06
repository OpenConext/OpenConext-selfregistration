package nl.surfnet.coin.selfregistration.web;

import nl.surfnet.coin.selfregistration.adapters.ServiceRegistryRestClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionController {
  private final static Logger logger = LoggerFactory.getLogger(ExceptionController.class);

  @ExceptionHandler(Exception.class)
  public ModelAndView onException(Exception exception) {
    logger.error("Unexpected exception occurred", exception);
    return new ModelAndView("error");
  }

  @ExceptionHandler(ServiceRegistryRestClientException.class)
  public ModelAndView onException(HttpServletRequest httpServletRequest, Exception exception) {
    String message = String.format(
      "Error during communication with ServiceRegistry when trying to create " +
        "service provider for URL: %s. Exception from service registry: %s",
      httpServletRequest.getRequestURI(),
      exception.getMessage()
    );
    logger.error(message);
    return new ModelAndView("error");
  }
}
