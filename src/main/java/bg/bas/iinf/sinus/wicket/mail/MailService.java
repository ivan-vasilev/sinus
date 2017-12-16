package bg.bas.iinf.sinus.wicket.mail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * nastroiki za email server-a
 * @author hok
 *
 */
public class MailService {

	private static final String META_INF_MAIL_PROPERTIES = "/META-INF/mail.properties";

	private String user;
	private String host;
	private String password;
	private Session session;

	private Transport transport;

	private static MailService instance;

	public static MailService getService() {
		if (instance == null) {
			instance = new MailService();
		}
		return instance;
	}

	private MailService() {
		Properties properties = new Properties();
		InputStream resourceAsStream = getClass().getResourceAsStream(META_INF_MAIL_PROPERTIES);
		try {
			properties.load(resourceAsStream);
			resourceAsStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		user = properties.getProperty("user");
		host = properties.getProperty("mail.smtp.host");
		password = properties.getProperty("password");
		session = Session.getInstance(properties, null);

		try {
			transport = session.getTransport("smtp");
		} catch (NoSuchProviderException e) {
			throw new IllegalStateException(e);
		}
	}

	@SuppressWarnings("unused")
    public void sendForgottenPasswordMail(String username, String mail, String randomPassword) {
	}

	public Message createMessage(String recipient, String subject, String content) throws MessagingException {
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(user));
		message.setRecipients(Message.RecipientType.TO, new InternetAddress[] { new InternetAddress(recipient) });
		message.setSubject(subject);

		if (content != null) {
			message.setContent(content, "text/html; charset=utf-8");
		}

		return message;
	}

	private Transport createTransport() throws MessagingException {
		if (!transport.isConnected()) {
			transport.connect(host, user, password);
		}
		return transport;
	}

	public void sendMailMessage(Message message, boolean isInDevelopment) throws MessagingException {
		if (isInDevelopment) {
			try {
				saveMailContentToFile((String) message.getContent());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
    		Transport transport = createTransport();
    		transport.sendMessage(message, message.getAllRecipients());
		}
	}

	private static void saveMailContentToFile(String content) throws IOException {
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(File.createTempFile("carparts_mail", ".html")), "UTF16"));
		try {
			out.write(content);
		} finally {
			out.close();
		}
	}
}
