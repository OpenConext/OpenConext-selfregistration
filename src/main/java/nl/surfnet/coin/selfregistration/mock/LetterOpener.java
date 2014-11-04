package nl.surfnet.coin.selfregistration.mock;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;

public class LetterOpener extends JavaMailSenderImpl {

  private static final Logger log = LoggerFactory.getLogger(LetterOpener.class);

  @Override
  protected void doSend(MimeMessage[] mimeMessages, Object[] originalMessages) throws MailException {
    try {
      for (int i = 0; i < mimeMessages.length; i++) {
        MimeMessage mimeMessage = mimeMessages[i];
        if (mimeMessage.getContent() instanceof MimeMultipart) {
          MimeMultipart m = (MimeMultipart) mimeMessage.getContent();
          for (int j = 0; j < m.getCount(); j++) {
            BodyPart bodyPart = m.getBodyPart(j);
            String text = getText(bodyPart);
            openInBrowser(text);
          }
        } else {
          String message = (String) mimeMessage.getContent();
          if(mimeMessage.getContentType().equalsIgnoreCase("text/plain")) {
            message = String.format("<html><body>Subject:<pre>%s</pre><p>Body:<pre>%s</pre></body></html>", mimeMessage.getSubject(), mimeMessage.getContent());
          }
          openInBrowser(message);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  private void openInBrowser(String text) throws IOException {
    File tempFile = File.createTempFile("javamail", ".html");
    FileUtils.writeStringToFile(tempFile, text);
    String osName = System.getProperty("os.name").toLowerCase();
    if (osName.contains("mac os x")) {
      Runtime runtime = Runtime.getRuntime();
      runtime.exec("open " + tempFile.getAbsolutePath());
    }
    log.info("*************************** CONTENTS EMAIL ***************************");
    log.info(text);
    log.info("**********************************************************************");
  }

  /**
   * Return the primary text content of the message.
   */
  private String getText(Part p) throws MessagingException, IOException {
    if (p.isMimeType("text/plain") || p.isMimeType("text/html")) {
      return (String) p.getContent();
    }

    if (p.isMimeType("multipart/alternative")) {
      // prefer html text over plain text
      Multipart mp = (Multipart) p.getContent();
      String text = null;
      for (int i = 0; i < mp.getCount(); i++) {
        Part bp = mp.getBodyPart(i);
        if (bp.isMimeType("text/plain")) {
          if (text == null)
            text = getText(bp);
          continue;
        } else if (bp.isMimeType("text/html")) {
          String s = getText(bp);
          if (s != null)
            return s;
        } else {
          return getText(bp);
        }
      }
      return text;
    } else if (p.isMimeType("multipart/*")) {
      Multipart mp = (Multipart) p.getContent();
      for (int i = 0; i < mp.getCount(); i++) {
        String s = getText(mp.getBodyPart(i));
        if (s != null)
          return s;
      }
    }

    return null;
  }

}
