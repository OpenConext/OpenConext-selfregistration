package nl.surfnet.coin.selfregistration.adapters;

public class ServiceProviderToServiceRegistryEntry {

  private ServiceProviderToServiceRegistryEntry() {
  }

  public static final String INITIAL_STATE = "testaccepted";
  public static final String TYPE_SP = "saml20-sp";

  public static ServiceRegistryEntry convert(ServiceProvider serviceProvider) {
    ServiceRegistryEntry serviceRegistryEntry = new ServiceRegistryEntry();
    serviceRegistryEntry.setName(serviceProvider.getEntityId());
    serviceRegistryEntry.setState(INITIAL_STATE);
    serviceRegistryEntry.setType(TYPE_SP);
    Metadata metadata = serviceRegistryEntry.getMetadata();
    metadata.assertionConsumerServices(serviceProvider.getAssertionConsumerServices());
    metadata.nameIdFormats(serviceProvider.getNameIdFormats());
    metadata.contactPersons(serviceProvider.getContactPersons());
    metadata.coin(serviceProvider.getOauthSettings());
    return serviceRegistryEntry;
  }
}
