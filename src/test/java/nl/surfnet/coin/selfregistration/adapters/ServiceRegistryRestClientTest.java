package nl.surfnet.coin.selfregistration.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.*;
import org.apache.http.entity.StringEntity;
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
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static nl.surfnet.coin.selfregistration.adapters.StokerEntryFactory.stokerEntry;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    public int wantedResponseCode = 201;
    public String wantedResponseContent = "";

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
      this.httpResponse.setStatusCode(wantedResponseCode);
      this.httpResponse.setEntity(new StringEntity(wantedResponseContent));
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
    serviceProvider.getOauthSettings().setCallbackUrl("http://callback");
    serviceProvider.getOauthSettings().setSecret("secret key");
    serviceProvider.getOauthSettings().setConsumerKey("consumer key");
    subject = new ServiceRegistryRestClient(
      "http",
      server.getServiceAddress().getHostName(),
      server.getServiceAddress().getPort(),
      USERNAME,
      PASSWORD
    );

  }

  @After
  public void tearDown() throws Exception {
    server.stop();
  }

  @Test
  public void testBasicAuthenticationPresent() throws Exception {
    subject.postConnection(serviceProvider);

    assertTrue(spyHandler.httpRequest.containsHeader("Authorization"));
    assertEquals(format("%s:%s", USERNAME, PASSWORD), new BasicAuthTokenExtractor().extract(spyHandler.httpRequest));
  }

  @Test
  public void testJsonBodyHasName() throws Exception {
    subject.postConnection(serviceProvider);

    assertThat(body(), hasKey("name"));
    assertEquals(SP_ENTITY_ID, body().get("name"));
  }

  @Test
  public void testJsonBodyHasType() throws Exception {
    subject.postConnection(serviceProvider);

    assertThat(body(), hasKey("type"));
    assertEquals("saml20-sp", body().get("type"));
  }

  @Test
  public void testJsonBodyHasState() throws Exception {
    subject.postConnection(serviceProvider);

    assertThat(body(), hasKey("state"));
    assertEquals("testaccepted", body().get("state"));
  }

  @Test
  public void testJsonBodyHasContactPersons() throws Exception {
    subject.postConnection(serviceProvider);

    assertThat(body(), hasKey("metadata"));
    List<Map<String, ?>> contacts = getAsListOfMaps("contacts", getMetadata());
    assertEquals(2, contacts.size());
  }

  @Test
  public void testJsonBodyHasNameIDFormats() throws Exception {
    subject.postConnection(serviceProvider);

    List<String> nameIDFormats = getAsListOfStrings("NameIDFormat", getMetadata());
    assertEquals(3, nameIDFormats.size());
  }

  @Test
  public void testJsonBodyHasOauthCallbackUrl() throws Exception {
    subject.postConnection(serviceProvider);

    Map<String, ?> coin = getCoinMap();
    assertThat(coin, hasKey("gadgetbaseurl"));
    assertEquals(serviceProvider.getOauthSettings().getCallbackUrl(), coin.get("gadgetbaseurl"));
  }

  @Test
  public void testJsonBodyHasOauthKeyAndSecret() throws Exception {
    subject.postConnection(serviceProvider);

    Map<String, ?> coin = getCoinMap();
    Map<String, ?> oauth = getAsMap("oauth", coin);
    assertThat(oauth, hasKey(serviceProvider.getOauthSettings().getConsumerKey()));
    assertEquals(
      serviceProvider.getOauthSettings().getSecret(),
      oauth.get(serviceProvider.getOauthSettings().getConsumerKey())
    );
  }

  @Test
  public void testThrowsExceptionWhenStatusIsNot201() throws Exception {
    spyHandler.wantedResponseCode = 500;
    spyHandler.wantedResponseContent = "Breaking Bad";

    try {
      subject.postConnection(serviceProvider);
      fail("Expected ServiceRegistryRestClientException");
    } catch(ServiceRegistryRestClientException e) {
      assertEquals(spyHandler.wantedResponseCode, e.getStatusCode());
      assertEquals(spyHandler.wantedResponseContent, e.getMessage());
    }
  }

  private Map<String, ?> getMetadata() throws IOException {
    return getAsMap("metadata", body());
  }

  private Map<String, ?> getCoinMap() throws IOException {
    return getAsMap("coin", getMetadata());
  }

  private Map<String, ?> body() throws IOException {
    return new ObjectMapper().readValue(spyHandler.body, HashMap.class);
  }

  private Map<String, ?> getAsMap(String key, Map<String, ?> from) {
    return (Map<String, ?>) from.get(key);
  }

  private List<String> getAsListOfStrings(String key, Map<String, ?> from) {
    return (List<String>) from.get(key);
  }

  private List<Map<String, ?>> getAsListOfMaps(String key, Map<String, ?> from) {
    return (List<Map<String, ?>>) from.get(key);
  }
}
