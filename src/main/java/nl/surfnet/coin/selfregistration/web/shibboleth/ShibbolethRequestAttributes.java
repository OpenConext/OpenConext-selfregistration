package nl.surfnet.coin.selfregistration.web.shibboleth;

/**
 * Lists the names under which Shibboleth makes SAML attributes available on the HttpServletRequest
 */
public enum ShibbolethRequestAttributes {

  UID("eduteams_uid"), DISPLAY_NAME("eduteams_displayname"), EMAIL("eduteams_shib-inetorgperson-mail"), IDP_ID("eduteams_shib-identity-provider");

  private final String attributeName;

  public String getAttributeName() {
    return attributeName;
  }

  ShibbolethRequestAttributes(String attributeName) {
    this.attributeName = attributeName;
  }
}
