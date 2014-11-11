package nl.surfnet.coin.selfregistration.adapters;

import nl.surfnet.coin.stoker.StokerEntry;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.*;

public class ServiceProviderToServiceRegistryEntryTest {
  // in file 64db397e6f93619687d294bed6639c29.xml
  public static final String SP_ENTITY_ID = "http://saml.ps-ui-test.qalab.geant.net";

  private StokerEntry stokerEntry;
  private ServiceRegistryEntry actual;

  @Before
  public void setUp() throws Exception {
    stokerEntry = StokerEntryFactory.stokerEntry(new ClassPathResource("/adapters/metadata.index.json"), SP_ENTITY_ID);
    ServiceProvider serviceProvider = ServiceProviderFactory.from(stokerEntry);
    actual = ServiceProviderToServiceRegistryEntry.convert(serviceProvider);
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
    Collection<Map<String, String>> result = metadata().getAssertionConsumerService();
    assertEquals(2, result.size());
  }

  @Test
  public void testHasCorrectNameIDFormats() throws Exception {
    Collection<String> result = metadata().getNameIdFormats();
    assertEquals(3, result.size());
  }

  @Test
  public void testHasCorrectContactPersons() throws Exception {
    Collection<Map<String, String>> result = metadata().getContactPersons();
    assertEquals(2, result.size());
  }

  @Test
  public void testMustHaveARevisionNote() throws Exception {
    assertNotNull(actual.getRevisionNote());
  }

  private Metadata metadata() {
    return actual.getMetadata();
  }
}
