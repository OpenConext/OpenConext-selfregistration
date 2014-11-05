package nl.surfnet.coin.selfregistration.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.*;
import org.apache.http.localserver.BasicAuthTokenExtractor;
import org.apache.http.localserver.LocalTestServer;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static nl.surfnet.coin.selfregistration.adapters.StokerEntryFactory.stokerEntry;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServiceRegistryRestClientTest {
  // in file 64db397e6f93619687d294bed6639c29.xml
  public static final String SP_ENTITY_ID = "http://saml.ps-ui-test.qalab.geant.net";

  public static final String PASSWORD = "bar";
  public static final String USERNAME = "foo";


  private static class SpyHandler implements HttpRequestHandler {

    public HttpRequest httpRequest;
    public HttpResponse httpResponse;
    public HttpContext httpContext;
    public String body = "";

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
      this.httpRequest = httpRequest;
      this.httpResponse = httpResponse;
      this.httpContext = httpContext;
      if (httpRequest instanceof HttpEntityEnclosingRequest) {
        try {
          HttpEntity entity = ((HttpEntityEnclosingRequest) httpRequest).getEntity();
          byte[] data = EntityUtils.toByteArray(entity);
          this.body = new String(data);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
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

  @Test
  public void testJsonBodyHasName() throws Exception {
    assertThat(body(), hasKey("name"));
    assertEquals(SP_ENTITY_ID, body().get("name"));
  }

  @Test
  public void testJsonBodyHasType() throws Exception {
    assertThat(body(), hasKey("type"));
    assertEquals("saml20-sp", body().get("type"));
  }

  @Test
  public void testJsonBodyHasState() throws Exception {
    assertThat(body(), hasKey("state"));
    assertEquals("testaccepted", body().get("state"));
  }

  private Map<String, ?> body() throws IOException {
    return new ObjectMapper().readValue(spyHandler.body, HashMap.class);
  }
}
