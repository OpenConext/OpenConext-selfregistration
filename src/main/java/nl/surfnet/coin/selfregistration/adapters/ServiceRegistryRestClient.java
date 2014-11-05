package nl.surfnet.coin.selfregistration.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

import static java.lang.String.format;

public class ServiceRegistryRestClient implements ServiceRegistryAdapter {
  private final String hostname;
  private final int port;
  private final String username;
  private final String password;
  private final String serverUrl;
  private ObjectMapper objectMapper;

  public ServiceRegistryRestClient(String protocol, String hostname, int port, String username, String password) {
    this.hostname = hostname;
    this.port = port;
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
      HttpResponse response = executor
        .execute(
          Request
            .Post(serverUrl + "/janus/app.php/api/connections.json")
            .addHeader("Authorization", "Basic " + basicAuth)
            .body(
              new StringEntity(
                objectMapper.writeValueAsString(
                  ServiceProviderToServiceRegistryEntry.convert(serviceProvider)
                )
              )
            )
        ).returnResponse();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
