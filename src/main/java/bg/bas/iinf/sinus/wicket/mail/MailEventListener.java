package bg.bas.iinf.sinus.wicket.mail;

import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;

import javax.mail.Message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.event.IEventSink;

import bg.bas.iinf.sinus.hibernate.entity.Users;
import bg.bas.iinf.sinus.wicket.auth.ForgottenPasswordPage.ForgottenPasswordEP;
import bg.bas.iinf.sinus.wicket.auth.RegistrationPage.UserRegisteredEP;

/**
 * Slusha za eventi i izprashta mail ako trqbva
 *
 * @author hok
 *
 */
public class MailEventListener extends Thread implements IEventSink {

	private static final Log log = LogFactory.getLog(MailEventListener.class);

	private ArrayBlockingQueue<EmailQueueEntry> mailsQueue;

	public MailEventListener() {
		super();

		mailsQueue = new ArrayBlockingQueue<EmailQueueEntry>(100);

		start();
	}

	@Override
	public void onEvent(IEvent<?> event) {
		if (event.getPayload() instanceof ForgottenPasswordEP) {
			sendMessage((ForgottenPasswordEP) event.getPayload());
		} else if (event.getPayload() instanceof UserRegisteredEP) {
			UserRegisteredEP payload = (UserRegisteredEP) event.getPayload();
			sendConfirmUserMessage(payload.getUser());
		}
	}

	/**
	 * pri zabravena parola
	 *
	 * @param payload
	 */
	private void sendMessage(ForgottenPasswordEP payload) {
		EmailQueueEntry queueEntry = new EmailQueueEntry();

		try {
			queueEntry.setMessage(MailService.getService().createMessage(payload.getUser().getEmail(), getString("mail.title.forgotten_password"), null));
			queueEntry.getMessage().setContent(BaseMainTemplate.getForgottenPasswordContent(payload.getUser(), payload.getNewPassword()), "text/html; charset=utf-8");

            mailsQueue.put(queueEntry);
        } catch (InterruptedException e) {
        	log.error(e);
        } catch (javax.mail.MessagingException e) {
        	log.error(e);
        }
    }

	/**
	 * izprashta syobshtenie za potvyrjdavane na registraciqta
	 *
	 * @param user
	 */
	private void sendConfirmUserMessage(Users user) {
		if (user.getUsersConfirmation().size() > 0) {
    		EmailQueueEntry queueEntry = new EmailQueueEntry();

    		try {
    			queueEntry.setMessage(MailService.getService().createMessage(user.getEmail(), getString("mail.title.confirm_user"), null));
    			queueEntry.getMessage().setContent(BaseMainTemplate.getConfirmUserContent(user), "text/html; charset=utf-8");

    			mailsQueue.put(queueEntry);
            } catch (InterruptedException e) {
            	log.error(e);
            } catch (javax.mail.MessagingException e) {
            	log.error(e);
            }
		}
    }

	@Override
	public void run() {

		while (true) {
			try {
				EmailQueueEntry queueEntry = mailsQueue.take();

				MailService.getService().sendMailMessage(queueEntry.getMessage(), queueEntry.isAppInDevelopment());

			} catch (InterruptedException ie) {
				return;
			} catch (Exception e) { // narochno e generalen exception, za da moje nishkata da prodylji da se izpylnqva, dori pri exception
				log.error(e);
			}
		}
	}

	private static class EmailQueueEntry {

		private Message message;
		private boolean isAppInDevelopment;

		public EmailQueueEntry() {
			super();

			this.isAppInDevelopment = Application.get().usesDevelopmentConfig();
		}

		public Message getMessage() {
			return message;
		}

		public void setMessage(Message message) {
			this.message = message;
		}

		public boolean isAppInDevelopment() {
			return isAppInDevelopment;
		}

	}

	private static String getString(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle(MailEventListener.class.getName(), Session.get().getLocale());
		return bundle.getString(key);
	}
}
