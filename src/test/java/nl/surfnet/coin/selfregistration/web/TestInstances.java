package nl.surfnet.coin.selfregistration.web;

import nl.surfnet.coin.selfregistration.invite.Invitation;
import nl.surfnet.coin.selfregistration.adapters.OauthSettings;

import java.util.UUID;

public class TestInstances {
  private TestInstances() {
  }

  public static OauthSettings emptyOauthSettings() {
    return new OauthSettings();
  }

  public static OauthSettings validOauthSettings() {
    OauthSettings oauthSettings = emptyOauthSettings();
    oauthSettings.setCallbackUrl("http://localhost:8080");
    oauthSettings.setConsumerKey("key");
    oauthSettings.setSecret(UUID.randomUUID().toString());
    return oauthSettings;
  }

  public static Invitation newInvitation(String spEntityId) {
    return new Invitation(spEntityId, "foo@localhost.nl");
  }

}
