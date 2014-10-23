package nl.surfnet.coin.selfregistration.model;

import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.net.URL;

public class ServiceProvider {
  @NotNull
  @Size(min = 1)
  private String entityId;
  private State state;
  private Type type;
  private DateTime expirationDate;
  private URL metadataUrl;
  private DateTime metadataValidUntil;
  private DateTime metadataCacheUntil;
  private String revisionNote;
  private String notes;
  private boolean allowAllEntities;
  private boolean isActive;

  public String getEntityId() {
    return entityId;
  }

  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public DateTime getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(DateTime expirationDate) {
    this.expirationDate = expirationDate;
  }

  public URL getMetadataUrl() {
    return metadataUrl;
  }

  public void setMetadataUrl(URL metadataUrl) {
    this.metadataUrl = metadataUrl;
  }

  public DateTime getMetadataValidUntil() {
    return metadataValidUntil;
  }

  public void setMetadataValidUntil(DateTime metadataValidUntil) {
    this.metadataValidUntil = metadataValidUntil;
  }

  public DateTime getMetadataCacheUntil() {
    return metadataCacheUntil;
  }

  public void setMetadataCacheUntil(DateTime metadataCacheUntil) {
    this.metadataCacheUntil = metadataCacheUntil;
  }

  public String getRevisionNote() {
    return revisionNote;
  }

  public void setRevisionNote(String revisionNote) {
    this.revisionNote = revisionNote;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public boolean isAllowAllEntities() {
    return allowAllEntities;
  }

  public void setAllowAllEntities(boolean allowAllEntities) {
    this.allowAllEntities = allowAllEntities;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  @Override
  public String toString() {
    return "ServiceProvider{" +
      "entityId='" + entityId + '\'' +
      ", state=" + state +
      ", type=" + type +
      ", expirationDate=" + expirationDate +
      ", metadataUrl=" + metadataUrl +
      ", metadataValidUntil=" + metadataValidUntil +
      ", metadataCacheUntil=" + metadataCacheUntil +
      ", revisionNote='" + revisionNote + '\'' +
      ", notes='" + notes + '\'' +
      ", allowAllEntities=" + allowAllEntities +
      ", isActive=" + isActive +
      '}';
  }
}
