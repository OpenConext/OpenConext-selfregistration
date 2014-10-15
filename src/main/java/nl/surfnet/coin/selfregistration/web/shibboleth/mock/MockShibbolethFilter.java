package nl.surfnet.coin.selfregistration.web.shibboleth.mock;

import nl.surfnet.coin.selfregistration.web.shibboleth.ShibbolethRequestAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

public class MockShibbolethFilter implements Filter {

  private static final Logger LOG = LoggerFactory.getLogger(MockShibbolethFilter.class);

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    LOG.info("=================================");
    LOG.info("MockShibbolethFilter initialized!");
    LOG.info("=================================");
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    servletRequest.setAttribute(ShibbolethRequestAttributes.UID.getAttributeName(), "csa_admin");
    servletRequest.setAttribute(ShibbolethRequestAttributes.DISPLAY_NAME.getAttributeName(), "dev admin");
    servletRequest.setAttribute(ShibbolethRequestAttributes.EMAIL.getAttributeName(), "admin@local");
    servletRequest.setAttribute(ShibbolethRequestAttributes.IDP_ID.getAttributeName(), "http://mock-idp");

    LOG.info("ShibbolethRequestAttributes set on servletRequest!");
    filterChain.doFilter(servletRequest, servletResponse);
  }

  @Override
  public void destroy() {

  }
}
