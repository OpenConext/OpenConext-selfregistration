package nl.surfnet.coin.selfregistration.web.shibboleth.mock;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.internet.MimeMessage;
import java.util.*;

/**
 * Test mail server.
 */
public class InMemoryMail extends JavaMailSenderImpl {

  private final static List<MimeMessage> messages = new ArrayList<>();

  @Override
  protected void doSend(MimeMessage[] mimeMessages, Object[] originalMessages) throws MailException {
    messages.addAll(Arrays.asList(mimeMessages));
  }

  public static void empty() {
    messages.clear();
  }

  public static List<MimeMessage> inbox() {
    return messages;
  }
}
