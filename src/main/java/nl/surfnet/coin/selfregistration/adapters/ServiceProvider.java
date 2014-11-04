package nl.surfnet.coin.selfregistration.adapters;

import nl.surfnet.coin.stoker.StokerEntry;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.net.URL;

public class ServiceProvider {
  private final StokerEntry stokerEntry;
  private OauthSettings oauthSettings;

  public ServiceProvider(StokerEntry stokerEntry) {
    this.stokerEntry = stokerEntry;
  }

  public static ServiceProvider from(StokerEntry stokerEntry) {
    return new ServiceProvider(stokerEntry);
  }

  public ServiceProvider with(OauthSettings oauthSettings) {
    this.oauthSettings = oauthSettings;
    return this;
  }

  @Override
  public String toString() {
    return "ServiceProvider{" +
      "stokerEntry=" + stokerEntry +
      ", oauthSettings=" + oauthSettings +
      '}';
  }
}
