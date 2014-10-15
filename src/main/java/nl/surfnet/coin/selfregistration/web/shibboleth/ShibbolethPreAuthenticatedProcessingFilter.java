package nl.surfnet.coin.selfregistration.web.shibboleth;

import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

public class ShibbolethPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

  private static final Logger LOG = LoggerFactory.getLogger(ShibbolethPreAuthenticatedProcessingFilter.class);

  @Override
  protected Object getPreAuthenticatedPrincipal(final HttpServletRequest request) {
    final Optional<String> uid = Optional.fromNullable((String) request.getAttribute(ShibbolethRequestAttributes.UID.getAttributeName()));
    if (uid.isPresent()) {
      LOG.info("Found user with uid {}", uid.get());
      final String displayName = (String) request.getAttribute(ShibbolethRequestAttributes.DISPLAY_NAME.getAttributeName());
      final String email = (String) request.getAttribute(ShibbolethRequestAttributes.EMAIL.getAttributeName());
      final String idpId = (String) request.getAttribute(ShibbolethRequestAttributes.IDP_ID.getAttributeName());
      return new ShibbolethPrincipal(uid.get(), displayName, email, idpId);
    } else {
      LOG.info("No principal found. This should trigger shibboleth.");
      return null;
    }
  }

  @Override
  protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
    return "N/A";
  }
}
