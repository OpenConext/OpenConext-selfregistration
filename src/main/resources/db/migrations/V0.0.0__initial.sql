DROP TABLE IF EXISTS invitations;

CREATE TABLE invitations (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  uuid varchar(255) DEFAULT NULL,
  sp_entity_id varchar(1000) DEFAULT NULL,
  mailed_to varchar(1000) DEFAULT NULL,
  created_at DATETIME NOT NULL,
  accepted_at DATETIME,
  PRIMARY KEY (id)
) ENGINE=InnoDB;
