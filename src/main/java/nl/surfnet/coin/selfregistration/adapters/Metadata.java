package nl.surfnet.coin.selfregistration.adapters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import nl.surfnet.coin.stoker.ContactPerson;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Metadata {
  public static final String ASSERTION_CONSUMER_SERVICE = "AssertionConsumerService";
  public static final String NAME_ID_FORMAT = "NameIDFormats";
  public static final String CONTACTS = "contacts";
  public static final String COIN = "coin";

  private Map<String, Object> metadata = new HashMap<>();


  @JsonProperty(ASSERTION_CONSUMER_SERVICE)
  public Collection<Map<String, String>> getAssertionConsumerService() {
    return (List<Map<String, String>>) metadata.get(ASSERTION_CONSUMER_SERVICE);
  }

  public Metadata assertionConsumerServices(List<Map<String, String>> entries) {
    metadata.put(ASSERTION_CONSUMER_SERVICE, entries);
    return this;
  }

  @JsonProperty(NAME_ID_FORMAT)
  public Collection<String> getNameIdFormats() {
    return (List<String>) metadata.get(NAME_ID_FORMAT);
  }

  public Metadata nameIdFormats(List<String> nameIdFormats) {
    metadata.put(NAME_ID_FORMAT, nameIdFormats);
    return this;
  }

  @JsonProperty(CONTACTS)
  public Collection<Map<String, String>> getContactPersons() {
    return (Collection<Map<String, String>>) metadata.get(CONTACTS);
  }

  public Metadata contactPersons(Collection<ContactPerson> contactPersons) {
    metadata.put(CONTACTS, Collections2.transform(contactPersons, new Function<ContactPerson, Map<String, String>>() {
      @Override
      public Map<String, String> apply(ContactPerson input) {
        return ImmutableMap.of(
          "contactType", input.getType(),
          "emailAddress", input.getEmailAddress(),
          "givenName", input.getGivenName(),
          "surName", input.getSurName(),
          "telephoneNumber", input.getTelephoneNumber()
        );
      }
    }));
    return this;
  }

  public Metadata coin(OauthSettings oauthSettings) {
    metadata.put("coin", ImmutableMap.of(
        "gadgetbaseurl", oauthSettings.getConsumerKey(),
        "oauth", ImmutableMap.of(
          "secret", oauthSettings.getSecret(),
          "callback_url", oauthSettings.getCallbackUrl()
        )
      )
    );
    return this;
  }

  @JsonProperty(COIN)
  public Map<String, ?> getCoin() {
    return (Map<String, ?>) metadata.get(COIN);
  }
}
