package nl.surfnet.coin.selfregistration.invite;

import org.springframework.context.MessageSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Locale;

public class InviteService {
  private final InviteDao inviteDao;
  private final JavaMailSender javaMailSender;
  private final MessageSource messageSource;
  private final TransactionTemplate transactionTemplate;
  private final String invitationBaseUrl;

  public InviteService(InviteDao inviteDao, DataSource dataSource, JavaMailSender javaMailSender, MessageSource messageSource, String invitationBaseUrl) {
    this.inviteDao = inviteDao;
    this.javaMailSender = javaMailSender;
    this.messageSource = messageSource;
    this.invitationBaseUrl = invitationBaseUrl;
    this.transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
  }

  public void perEmail(final Invitation invitation, final String[] to) {
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
        inviteDao.persist(invitation);
        SimpleMailMessage mailMessage = createMailMessage(to, invitation);
        javaMailSender.send(mailMessage);
      }
    });
  }

  private SimpleMailMessage createMailMessage(String[] to, Invitation invitation) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setSubject(
      messageSource.getMessage(
        "invite.text.subject",
        null,
        Locale.ENGLISH
      )
    );
    mailMessage.setText(
      messageSource.getMessage(
        "invite.text.body",
        new Object[]{String.format("%s/service-provider/%s",
          invitationBaseUrl,
          invitation.getUuid())
        },
        Locale.ENGLISH
      )
    );
    mailMessage.setTo(to);
    return mailMessage;
  }


}
