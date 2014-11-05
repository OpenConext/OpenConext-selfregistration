package nl.surfnet.coin.selfregistration.adapters;

import nl.surfnet.coin.stoker.ContactPerson;
import nl.surfnet.coin.stoker.StokerEntry;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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

  public String getEntityId() {
    return stokerEntry.getEntityId();
  }

  public List<Map<String, String>> getAssertionConsumerServices() {
    return stokerEntry.getAssertionConsumerServices();
  }

  public List<String> getNameIdFormats() {
    return stokerEntry.getNameIdFormats();
  }

  public Collection<ContactPerson> getContactPersons() {
    return stokerEntry.getContactPersons();
  }
}
