package nl.surfnet.coin.selfregistration.adapters;

import nl.surfnet.coin.stoker.ContactPerson;
import nl.surfnet.coin.stoker.Stoker;
import nl.surfnet.coin.stoker.StokerEntry;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ServiceProviderToServiceRegistryEntryTest {
  // in file 64db397e6f93619687d294bed6639c29.xml
  public static final String SP_ENTITY_ID = "http://saml.ps-ui-test.qalab.geant.net";

  private ServiceProviderToServiceRegistryEntry subject;
  private StokerEntry stokerEntry;
  private ServiceRegistryEntry actual;

  @Before
  public void setUp() throws Exception {
    subject = new ServiceProviderToServiceRegistryEntry();
    stokerEntry = StokerEntryFactory.stokerEntry(new ClassPathResource("/adapters/metadata.index.json"), SP_ENTITY_ID);
    actual = subject.convert(ServiceProvider.from(stokerEntry));
  }

  @Test
  public void testHasEntityId() throws Exception {
    assertEquals(stokerEntry.getEntityId(), actual.getName());
  }

  @Test
  public void testHasCorrectState() throws Exception {
    assertEquals("testaccepted", actual.getState());
  }

  @Test
  public void testHasCorrectType() throws Exception {
    assertEquals("saml20-sp", actual.getType());
  }

  @Test
  public void testHasCorrectAssertionConsumerServices() throws Exception {
    Collection<Map<String, String>> result = metadata().assertionConsumerService();
    assertEquals(2, result.size());
  }

  @Test
  public void testHasCorrectNameIDFormats() throws Exception {
    Collection<String> result = metadata().nameIdFormats();
    assertEquals(3, result.size());
  }

  @Test
  public void testHasCorrectContactPersons() throws Exception {
    Collection<Map<String, String>> result =  metadata().contactPersons();
    assertEquals(2, result.size());
  }

  private Metadata metadata() {
    return actual.getMetadata();
  }
}
