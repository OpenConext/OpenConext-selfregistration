package nl.surfnet.coin.selfregistration.adapters;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.localserver.BasicAuthTokenExtractor;
import org.apache.http.localserver.LocalTestServer;
import org.apache.http.localserver.RequestBasicAuth;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static nl.surfnet.coin.selfregistration.adapters.StokerEntryFactory.stokerEntry;
import static org.junit.Assert.*;
import static java.lang.String.format;
public class ServiceRegistryRestClientTest {
  // in file 64db397e6f93619687d294bed6639c29.xml
  public static final String SP_ENTITY_ID = "http://saml.ps-ui-test.qalab.geant.net";

  public static final String PASSWORD = "bar";
  public static final String USERNAME = "foo";


  private static class SpyHandler implements HttpRequestHandler {

    public HttpRequest httpRequest;
    public HttpResponse httpResponse;
    public HttpContext httpContext;

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
      this.httpRequest = httpRequest;
      this.httpResponse = httpResponse;
      this.httpContext = httpContext;
    }
  }

  private LocalTestServer server;
  private SpyHandler spyHandler;
  private ServiceProvider serviceProvider;

  private ServiceRegistryRestClient subject;


  @Before
  public void setUp() throws Exception {
    server = new LocalTestServer(null, null);
    spyHandler = new SpyHandler();
    server.register("/*", spyHandler);
    server.start();
    serviceProvider = new ServiceProvider(stokerEntry(new ClassPathResource("/adapters/metadata.index.json"), SP_ENTITY_ID));
    subject = new ServiceRegistryRestClient(
      server.getServiceAddress().getHostName(),
      server.getServiceAddress().getPort(),
      USERNAME,
      PASSWORD
    );

    subject.postConnection(serviceProvider);
  }

  @After
  public void tearDown() throws Exception {
    server.stop();
  }

  @Test
  public void testBasicAuthenticationPresent() throws Exception {
    assertTrue(spyHandler.httpRequest.containsHeader("Authorization"));
    assertEquals(format("%s:%s", USERNAME, PASSWORD), new BasicAuthTokenExtractor().extract(spyHandler.httpRequest));
  }

}
