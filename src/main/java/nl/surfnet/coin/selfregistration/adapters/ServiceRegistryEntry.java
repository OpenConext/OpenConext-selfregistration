package nl.surfnet.coin.selfregistration.adapters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceRegistryEntry {
  private String name;
  private String state;
  private String type;
  private DateTime expirationDate;
  private String metadataUrl;
  private String metadataValidUntil;
  private String metadataCacheUntil;
  private String manipulationCode;
  private String revisionNote;
  private String notes;
  private boolean allowAllEntities;
  private Map<String, List<String>> arpAttributes = new HashMap<>();
  private boolean isActive;
  private Metadata metadata = new Metadata();
  private List<String> allowedConnections = new ArrayList<>();
  private List<String> blockedConnections = new ArrayList<>();
  private List<String> disableConsentConnections = new ArrayList<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public DateTime getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(DateTime expirationDate) {
    this.expirationDate = expirationDate;
  }

  public String getMetadataUrl() {
    return metadataUrl;
  }

  public void setMetadataUrl(String metadataUrl) {
    this.metadataUrl = metadataUrl;
  }

  public String getMetadataValidUntil() {
    return metadataValidUntil;
  }

  public void setMetadataValidUntil(String metadataValidUntil) {
    this.metadataValidUntil = metadataValidUntil;
  }

  public String getMetadataCacheUntil() {
    return metadataCacheUntil;
  }

  public void setMetadataCacheUntil(String metadataCacheUntil) {
    this.metadataCacheUntil = metadataCacheUntil;
  }

  public String getManipulationCode() {
    return manipulationCode;
  }

  public void setManipulationCode(String manipulationCode) {
    this.manipulationCode = manipulationCode;
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

  public Map<String, List<String>> getArpAttributes() {
    return arpAttributes;
  }

  public void setArpAttributes(Map<String, List<String>> arpAttributes) {
    this.arpAttributes = arpAttributes;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public Metadata getMetadata() {
    return metadata;
  }

  public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
  }

  public List<String> getAllowedConnections() {
    return allowedConnections;
  }

  public void setAllowedConnections(List<String> allowedConnections) {
    this.allowedConnections = allowedConnections;
  }

  public List<String> getBlockedConnections() {
    return blockedConnections;
  }

  public void setBlockedConnections(List<String> blockedConnections) {
    this.blockedConnections = blockedConnections;
  }

  public List<String> getDisableConsentConnections() {
    return disableConsentConnections;
  }

  public void setDisableConsentConnections(List<String> disableConsentConnections) {
    this.disableConsentConnections = disableConsentConnections;
  }
}
