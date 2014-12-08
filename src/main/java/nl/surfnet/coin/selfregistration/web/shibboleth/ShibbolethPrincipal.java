package nl.surfnet.coin.selfregistration.web.shibboleth;

/**
 * Represents the data about the user that is provided to us by Shibboleth
 */
public class ShibbolethPrincipal {

  private final String uid;
  private final String displayName;
  private final String email;

  public ShibbolethPrincipal(String uid, String displayName, String email) {
    this.uid = uid;
    this.displayName = displayName;
    this.email = email;
  }

  public String getUid() {
    return uid;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public String toString() {
    return "ShibbolethPrincipal{" +
      "uid='" + uid + '\'' +
      ", displayName='" + displayName + '\'' +
      ", email='" + email + '\'' +
      '}';
  }
}
