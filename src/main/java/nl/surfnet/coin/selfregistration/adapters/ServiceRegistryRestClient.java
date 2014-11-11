package nl.surfnet.coin.selfregistration.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static java.lang.String.format;
import static nl.surfnet.coin.selfregistration.adapters.ServiceProviderToServiceRegistryEntry.convert;

public class ServiceRegistryRestClient implements ServiceRegistryAdapter {

  private static final Logger log = LoggerFactory.getLogger(ServiceRegistryRestClient.class);

  private final String username;
  private final String password;
  private final String serverUrl;
  private ObjectMapper objectMapper;

  public ServiceRegistryRestClient(String protocol, String hostname, int port, String username, String password) {
    this.username = username;
    this.password = password;
    this.serverUrl = protocol + "://" + hostname + ":" + port;
    this.objectMapper = new ObjectMapper();
  }

  @Override
  public void postConnection(ServiceProvider serviceProvider) {
    Executor executor = Executor.newInstance();
    String basicAuth = new String(Base64.encodeBase64(format("%s:%s", this.username, this.password).getBytes()));
    try {
      String jsonBody = objectMapper.writeValueAsString(
        convert(serviceProvider)
      );

      log.debug("Sending string: " + jsonBody.replaceAll(serviceProvider.getOauthSettings().getSecret(), "[HIDDEN]"));

      HttpResponse response = executor
        .execute(
          Request
            .Post(serverUrl + "/janus/app.php/api/connections.json")
            .addHeader("Authorization", "Basic " + basicAuth)
            .addHeader("Content-Type", "application/json")
            .body(new StringEntity(jsonBody))
        )
        .returnResponse();

      if (response.getStatusLine().getStatusCode() != 201) {
        throw new ServiceRegistryRestClientException(
          EntityUtils.toString(response.getEntity(), "UTF-8"),
          response.getStatusLine().getStatusCode()
        );
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
