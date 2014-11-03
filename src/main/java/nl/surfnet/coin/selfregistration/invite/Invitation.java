package nl.surfnet.coin.selfregistration.invite;

import org.joda.time.DateTime;

import java.util.UUID;

public class Invitation {

  public Invitation(String spEntityId, String mailedTo) {
    this.createdAt = new DateTime();
    this.uuid = UUID.randomUUID().toString();
    this.spEntityId = spEntityId;
    this.mailedTo = mailedTo;
  }

  private String uuid;
  private DateTime createdAt;
  private DateTime acceptedAt;
  private String spEntityId;
  private String mailedTo;

  @Override
  public String toString() {
    return "Invitation{" +
      "uuid='" + uuid + '\'' +
      ", createdAt=" + createdAt +
      ", acceptedAt=" + acceptedAt +
      ", spEntityId='" + spEntityId + '\'' +
      ", mailedTo='" + mailedTo + '\'' +
      '}';
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public DateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(DateTime createdAt) {
    this.createdAt = createdAt;
  }

  public DateTime getAcceptedAt() {
    return acceptedAt;
  }

  public void setAcceptedAt(DateTime acceptedAt) {
    this.acceptedAt = acceptedAt;
  }

  public String getSpEntityId() {
    return spEntityId;
  }

  public void setSpEntityId(String spEntityId) {
    this.spEntityId = spEntityId;
  }

  public String getMailedTo() {
    return mailedTo;
  }

  public void setMailedTo(String mailedTo) {
    this.mailedTo = mailedTo;
  }
}
