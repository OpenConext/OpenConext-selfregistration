package nl.surfnet.coin.selfregistration.mock;

import nl.surfnet.coin.selfregistration.adapters.ServiceProvider;
import nl.surfnet.coin.selfregistration.adapters.ServiceRegistryAdapter;

public class ServiceRegistryMock implements ServiceRegistryAdapter {

  public static interface Mocker {
    public void execute(ServiceProvider serviceProvider);
  }

  private static class NoopMocker implements Mocker {
    @Override
    public void execute(ServiceProvider serviceProvider) {
      // do nothing
    }
  }

  public Mocker mocker = new NoopMocker();

  @Override
  public void postConnection(ServiceProvider serviceProvider) {
    mocker.execute(serviceProvider);
  }

  public void reset() {
    this.mocker = new NoopMocker();
  }

}
