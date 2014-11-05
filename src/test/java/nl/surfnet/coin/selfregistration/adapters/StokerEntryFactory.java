package nl.surfnet.coin.selfregistration.adapters;

import nl.surfnet.coin.stoker.Stoker;
import nl.surfnet.coin.stoker.StokerEntry;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class StokerEntryFactory {
  private StokerEntryFactory() {
  }

  public static StokerEntry stokerEntry(Resource metadataFile, String entityId) throws Exception {
    Stoker stoker = new Stoker(metadataFile, new ClassPathResource("/stoker/"));
    return stoker.getEduGainServiceProvider(entityId);
  }
}
