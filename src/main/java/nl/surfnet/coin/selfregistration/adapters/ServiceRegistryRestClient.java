package nl.surfnet.coin.selfregistration.adapters;

import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.net.InetAddress;

public class ServiceRegistryRestClient implements ServiceRegistryAdapter {
  private final String hostname;
  private final int port;
  private final String username;
  private final String password;
  private final String serverUrl;

  public ServiceRegistryRestClient(String hostname, int port, String username, String password) {
    this.hostname = hostname;
    this.port = port;
    this.username = username;
    this.password = password;
    this.serverUrl = "http://" + hostname + ":" + port;
  }

  @Override
  public void postConnection(ServiceProvider serviceProvider) {
    Executor executor = Executor.newInstance();
    try {
      executor
        .auth(new UsernamePasswordCredentials(username, password))
        .authPreemptive(new HttpHost(hostname, port))
        .execute(
          Request
            .Post(serverUrl + "/api/connections.json")
        );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
