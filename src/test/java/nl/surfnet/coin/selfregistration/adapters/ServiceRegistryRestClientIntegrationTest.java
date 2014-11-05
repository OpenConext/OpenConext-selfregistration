package nl.surfnet.coin.selfregistration.adapters;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import static org.junit.Assert.fail;

public class ServiceRegistryRestClientIntegrationTest {

  public static final String SP_ENTITY_ID = "http://saml.ps-ui-test.qalab.geant.net";

  private ServiceRegistryRestClient subject;

  @Before
  public void setUp() throws Exception {
    subject = new ServiceRegistryRestClient(
      "https",
      "serviceregistry.test.surfconext.nl",
      443,
      "engine",
      "engineblock"
    );
  }

  @Ignore("Only run manually")
//  @Test
  public void testCreatesANewEntry() throws Exception {
    try {
      subject.postConnection(ServiceProviderFactory.from(StokerEntryFactory.stokerEntry(new ClassPathResource("/adapters/metadata.index.json"), SP_ENTITY_ID)));
    } catch (ServiceRegistryRestClientException e) {
      fail(e.toString());
    }
  }
}
