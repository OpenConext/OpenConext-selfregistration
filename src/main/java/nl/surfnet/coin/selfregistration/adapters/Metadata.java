package nl.surfnet.coin.selfregistration.adapters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Metadata {
  public static final String ASSERTION_CONSUMER_SERVICE = "AssertionConsumerService";
  private Map<String, Object> items = new HashMap<>();

  public List<Map<String, String>> assertionConsumerService() {
    return (List<Map<String, String>>) items.get(ASSERTION_CONSUMER_SERVICE);
  }

  public Metadata assertionConsumerServices(List<Map<String, String>> entries) {
    items.put(ASSERTION_CONSUMER_SERVICE, entries);
    return this;
  }
}
