package nl.surfnet.coin.selfregistration.web;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Ordering;
import nl.surfnet.coin.selfregistration.model.Invitation;
import nl.surfnet.coin.selfregistration.model.ServiceProvider;
import nl.surfnet.coin.stoker.ContactPerson;
import nl.surfnet.coin.stoker.Stoker;
import nl.surfnet.coin.stoker.StokerEntry;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/fedops")
public class InviteController {

  private static class ServiceProviderOrderer {
    private final Collection<StokerEntry> serviceProviders;

    private final static Ordering<StokerEntry> byDisplayNameEn = new Ordering<StokerEntry>() {
      @Override
      public int compare(StokerEntry left, StokerEntry right) {
        return left.getDisplayNameEn().compareTo(right.getDisplayNameEn());
      }
    };

    private ServiceProviderOrderer(Collection<StokerEntry> serviceProviders) {
      this.serviceProviders = serviceProviders;
    }

    public Collection<StokerEntry> ordered() {
      return byDisplayNameEn.sortedCopy(serviceProviders);
    }
  }


  @Autowired
  private Stoker stoker;

  @Autowired
  private JavaMailSender javaMailSender;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @RequestMapping(method = GET)
  public ModelAndView index() {
    return new ModelAndView("index", "serviceProviders", new ServiceProviderOrderer(stoker.getEduGainServiceProviders()).ordered());
  }

  @RequestMapping(value="/invite",method = GET)
  public ModelAndView invite(@RequestParam("spEntityId") String spEntityId) {
    return new ModelAndView("invite", "serviceProvider", stoker.getEduGainServiceProvider(spEntityId));
  }

  @RequestMapping(value="/invite",method = POST)
  public String doInvite(@RequestParam String spEntityId) {
    StokerEntry serviceProvider = stoker.getEduGainServiceProvider(spEntityId);
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setText("foo");
    mailMessage.setSubject("text");
    String[] to = mailTo(serviceProvider);
    mailMessage.setTo(to);
    final Invitation invitation = new Invitation(spEntityId, StringUtils.join(to, ","));
    jdbcTemplate.update("insert into invitations (uuid, created_at, mailed_to, sp_entity_id) values (?, ?, ?, ?)"
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
    javaMailSender.send(mailMessage);
    return "redirect:/invite";
  }

  private String[] mailTo(StokerEntry serviceProvider) {
    Collection<ContactPerson> contactPersons = serviceProvider.getContactPersons();
    return Collections2.transform(contactPersons, new Function<ContactPerson, String>() {
      @Override
      public String apply(ContactPerson input) {
        return input.getEmailAddress();
      }
    }).toArray(new String[contactPersons.size()]);
  }
}
