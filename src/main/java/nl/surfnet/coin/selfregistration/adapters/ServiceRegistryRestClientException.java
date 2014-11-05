package nl.surfnet.coin.selfregistration.adapters;

public class ServiceRegistryRestClientException extends RuntimeException {
  private final int statusCode;

  public ServiceRegistryRestClientException(String reason, int statusCode) {
    super(reason);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }

  @Override
  public String toString() {
    return "ServiceRegistryRestClientException{" +
      "statusCode=" + statusCode +
      "message=" + getMessage() +
      '}';
  }
}
