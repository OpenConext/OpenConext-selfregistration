package nl.surfnet.coin.selfregistration.invite;

import nl.surfnet.coin.selfregistration.invite.Invitation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
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
}
