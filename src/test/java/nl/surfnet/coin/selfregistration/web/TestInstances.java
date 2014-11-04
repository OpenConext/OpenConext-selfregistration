package nl.surfnet.coin.selfregistration.web;

import nl.surfnet.coin.selfregistration.model.OauthSettings;

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

}
