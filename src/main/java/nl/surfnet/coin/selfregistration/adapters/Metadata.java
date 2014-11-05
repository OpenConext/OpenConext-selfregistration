package nl.surfnet.coin.selfregistration.adapters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import nl.surfnet.coin.stoker.ContactPerson;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Metadata {
  public static final String ASSERTION_CONSUMER_SERVICE = "AssertionConsumerService";
  public static final String NAME_ID_FORMAT = "NameIDFormat";
  public static final String CONTACTS = "contacts";

  private Map<String, Object> items = new HashMap<>();

  public Collection<Map<String, String>> assertionConsumerService() {
    return (List<Map<String, String>>) items.get(ASSERTION_CONSUMER_SERVICE);
  }

  public Metadata assertionConsumerServices(List<Map<String, String>> entries) {
    items.put(ASSERTION_CONSUMER_SERVICE, entries);
    return this;
  }

  public Collection<String> nameIdFormats() {
    return (List<String>) items.get(NAME_ID_FORMAT);
  }

  public Metadata nameIdFormats(List<String> nameIdFormats) {
    items.put(NAME_ID_FORMAT, nameIdFormats);
    return this;
  }

  public Collection<Map<String, String>> contactPersons() {
    return (Collection<Map<String, String>>) items.get(CONTACTS);
  }

  public Metadata contactPersons(Collection<ContactPerson> contactPersons) {
    items.put(CONTACTS, Collections2.transform(contactPersons, new Function<ContactPerson, Map<String, String>>() {
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

  /*
   * Needed for json parsing. ObjectMapper expects public getters.
   */
  public Map<String, Object> getItems() {
    return items;
  }

  public Metadata coin(OauthSettings oauthSettings) {
    items.put("coin", ImmutableMap.of(
        "gadgetbaseurl", oauthSettings.getCallbackUrl(),
        "oauth", ImmutableMap.of(
          oauthSettings.getConsumerKey(), oauthSettings.getSecret()
        )
      )
    );
    return this;
  }
}
