package nl.surfnet.coin.selfregistration.web.shibboleth.mock;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.internet.MimeMessage;

public class InMemoryMail extends JavaMailSenderImpl {
  @Override
  protected void doSend(MimeMessage[] mimeMessages, Object[] originalMessages) throws MailException {

  }
}
