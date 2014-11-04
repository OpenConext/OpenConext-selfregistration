package nl.surfnet.coin.selfregistration.adapters;

public class ServiceProviderToServiceRegistryEntry {

  public static final String INITIAL_STATE = "testaccepted";
  public static final String TYPE_SP = "saml20-sp";

  public ServiceRegistryEntry convert(ServiceProvider serviceProvider) {
    ServiceRegistryEntry serviceRegistryEntry = new ServiceRegistryEntry();
    serviceRegistryEntry.setName(serviceProvider.getEntityId());
    serviceRegistryEntry.setState(INITIAL_STATE);
    serviceRegistryEntry.setType(TYPE_SP);
    serviceRegistryEntry.getMetadata().assertionConsumerServices(serviceProvider.getAssertionConsumerServices());
    return serviceRegistryEntry;
  }
}
