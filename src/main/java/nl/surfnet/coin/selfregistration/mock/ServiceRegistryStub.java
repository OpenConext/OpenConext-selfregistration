package nl.surfnet.coin.selfregistration.mock;

import nl.surfnet.coin.selfregistration.adapters.ServiceProvider;
import nl.surfnet.coin.selfregistration.adapters.ServiceRegistryAdapter;

public class ServiceRegistryStub implements ServiceRegistryAdapter {

  @Override
  public void postConnection(ServiceProvider serviceProvider) {
    // okay
  }
}
