package nl.surfnet.coin.selfregistration.web.shibboleth;

/**
 * Lists the names under which Shibboleth makes SAML attributes available on the HttpServletRequest
 */
public enum ShibbolethRequestAttributes {

  UID("uid"), DISPLAY_NAME("displayName"), EMAIL("Shib-InetOrgPerson-mail"), IDP_ID("Shib-Identity-Provider");

  private final String attributeName;

  public String getAttributeName() {
    return attributeName;
  }

  ShibbolethRequestAttributes(String attributeName) {
    this.attributeName = attributeName;
  }
}
