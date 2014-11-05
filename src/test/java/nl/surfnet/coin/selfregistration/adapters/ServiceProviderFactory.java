package nl.surfnet.coin.selfregistration.adapters;

import nl.surfnet.coin.stoker.StokerEntry;

public class ServiceProviderFactory {
  private ServiceProviderFactory() {
  }

  public static ServiceProvider from(StokerEntry stokerEntry) {
    ServiceProvider serviceProvider = ServiceProvider.from(stokerEntry);
    OauthSettings oauthSettings = serviceProvider.getOauthSettings();
    oauthSettings.setCallbackUrl("http://localhost");
    oauthSettings.setConsumerKey("comsumer key");
    oauthSettings.setSecret("secret");
    return serviceProvider;
  }
}
