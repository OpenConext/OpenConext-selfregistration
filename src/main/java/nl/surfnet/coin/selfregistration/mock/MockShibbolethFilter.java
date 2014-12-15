package nl.surfnet.coin.selfregistration.mock;

import nl.surfnet.coin.selfregistration.web.shibboleth.ShibbolethRequestAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.*;

public class MockShibbolethFilter implements Filter {

  private static class SetHeader extends HttpServletRequestWrapper {

    private final HashMap<String, String> headers;

    public SetHeader(HttpServletRequest request) {
      super(request);
      this.headers = new HashMap<>();
    }

    public void setHeader(String name, String value) {
      this.headers.put(name, value);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
      List<String> names = Collections.list(super.getHeaderNames());
      names.addAll(headers.keySet());
      return Collections.enumeration(names);
    }

    @Override
    public String getHeader(String name) {
      if (headers.containsKey(name)) {
        return headers.get(name);
      }
      return super.getHeader(name);
    }
  }

  private static final Logger LOG = LoggerFactory.getLogger(MockShibbolethFilter.class);

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    LOG.info("=================================");
    LOG.info("MockShibbolethFilter initialized!");
    LOG.info("=================================");
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    SetHeader wrapper = new SetHeader((HttpServletRequest) servletRequest);
    wrapper.setHeader(ShibbolethRequestAttributes.UID.getAttributeName(), "csa_admin");
    wrapper.setHeader(ShibbolethRequestAttributes.DISPLAY_NAME.getAttributeName(), "dev admin");
    wrapper.setHeader(ShibbolethRequestAttributes.EMAIL.getAttributeName(), "admin@local");
    wrapper.setHeader(ShibbolethRequestAttributes.IDP_ID.getAttributeName(), "http://mock-idp");

    LOG.info("ShibbolethRequestAttributes set on servletRequest!");
    filterChain.doFilter(wrapper, servletResponse);
  }

  @Override
  public void destroy() {

  }
}
