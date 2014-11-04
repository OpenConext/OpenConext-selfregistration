package nl.surfnet.coin.selfregistration.adapters;

import nl.surfnet.coin.stoker.Stoker;
import nl.surfnet.coin.stoker.StokerEntry;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ServiceProviderToServiceRegistryEntryTest {
  // in file 64db397e6f93619687d294bed6639c29.xml
  public static final String SP_ENTITY_ID = "http://saml.ps-ui-test.qalab.geant.net";

  private Stoker stoker;
  private ServiceProviderToServiceRegistryEntry subject;

  @Before
  public void setUp() throws Exception {
    subject = new ServiceProviderToServiceRegistryEntry();
    stoker = new Stoker(new ClassPathResource("/stoker/metadata.index.json"), new ClassPathResource("/stoker/"));
  }

  @Test
  public void testConvertsStokerEntryToServiceRegistry() throws Exception {
    StokerEntry stokerEntry = stoker.getEduGainServiceProvider(SP_ENTITY_ID);
    ServiceRegistryEntry actual = subject.convert(ServiceProvider.from(stokerEntry));

    assertEquals(stokerEntry.getEntityId(), actual.getName());
    assertEquals("testaccepted", actual.getState());
    assertEquals("saml20-sp", actual.getType());
    List<Map<String, String>> assertionConsumerService = actual.getMetadata().assertionConsumerService();
    assertEquals(2, assertionConsumerService.size());
  }
}
