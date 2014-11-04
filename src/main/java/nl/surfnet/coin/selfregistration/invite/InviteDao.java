package nl.surfnet.coin.selfregistration.invite;

import com.google.common.base.Optional;
import org.joda.time.DateTime;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class InviteDao {
  private final DataSource dataSource;

  public InviteDao(DataSource dataSource) {
    this.dataSource = dataSource;
  }


  public void persist(final Invitation invitation) {
    new JdbcTemplate(dataSource).update("insert into invitations (uuid, created_at, mailed_to, sp_entity_id) values (?, ?, ?, ?)"
      , new PreparedStatementSetter() {

        @Override
        public void setValues(PreparedStatement preparedStatement) throws SQLException {
          preparedStatement.setString(1, invitation.getUuid());
          preparedStatement.setTimestamp(2, new Timestamp(invitation.getCreatedAt().getMillis()));
          preparedStatement.setString(3, invitation.getMailedTo());
          preparedStatement.setString(4, invitation.getSpEntityId());
        }
      }
    );

  }

  public Optional<Invitation> get(String invitationId) {
    try {
      return Optional.of(new JdbcTemplate(dataSource).queryForObject("select * from invitations where uuid = ?", new RowMapper<Invitation>() {
        @Override
        public Invitation mapRow(ResultSet resultSet, int i) throws SQLException {
          Invitation invitation = new Invitation(resultSet.getString("sp_entity_id"), resultSet.getString("mailed_to"));
          invitation.setAcceptedAt(new DateTime(resultSet.getTimestamp("accepted_at")));
          invitation.setCreatedAt(new DateTime(resultSet.getTimestamp("created_at")));
          invitation.setUuid(resultSet.getString("uuid"));
          return invitation;
        }
      }, invitationId));
    } catch (EmptyResultDataAccessException e) {
      return Optional.absent();
    }
  }
}
