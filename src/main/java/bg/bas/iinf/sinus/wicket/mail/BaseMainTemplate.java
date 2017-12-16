package bg.bas.iinf.sinus.wicket.mail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.template.PackageTextTemplate;

import bg.bas.iinf.sinus.hibernate.entity.Users;
import bg.bas.iinf.sinus.wicket.auth.ConfirmUserPage;

/**
 * syzdava html shabloni sys sydyrjanie na email-i
 * @author hok
 *
 */
public class BaseMainTemplate {

	private static final Log log = LogFactory.getLog(BaseMainTemplate.class);

	/**
	 * basic mail template
	 * @param content child content
	 * @return complete mail template
	 */
	private static String getBaseContent(String content) {
		PackageTextTemplate tmp = new PackageTextTemplate(BaseMainTemplate.class, "base_main_template.html", "text/html", "UTF-8");

		Map<String, String> variables = new HashMap<String, String>();

		variables.put("logo", getFullUrl("images/mail/mail_logo.gif"));
		variables.put("blank_left", getFullUrl("images/mail/blank.gif"));
		variables.put("blank_right", getFullUrl("images/mail/blank.gif"));
		variables.put("mail_bottom", getFullUrl("images/mail/mail_bottom.gif"));
		variables.put("help_href", getFullUrl(Application.get().getHomePage(), null));
		variables.put("help", getString("mail.help"));
		variables.put("company_mail", getString("mail.company_mail"));
		variables.put("contact_us", getString("mail.contact_us"));
		variables.put("company", getString("mail.company"));
		variables.put("child_tmp", content);

		String result = tmp.asString(variables);
		IOUtils.closeQuietly(tmp);
		return result;
	}

	public static String getConfirmUserContent(Users user) {
		PackageTextTemplate tmp = new PackageTextTemplate(BaseMainTemplate.class, "confirm_user_template.html", "text/html", "UTF-8");

		Map<String, String> variables = new HashMap<String, String>();

		variables.put("hello", String.format(getString("mail.hello"), user.getName()));
		variables.put("thank_you", getString("mail.confirm_user.thank_you"));
		variables.put("confirm", getString("mail.confirm_user.confirm"));
		variables.put("confirm_link", getString("mail.confirm_user.confirm"));

		PageParameters pp = new PageParameters();
		pp.set(ConfirmUserPage.CONFIRM_KEY_PARAM, user.getUsersConfirmation().iterator().next().getConfirmKey());
		variables.put("confirm_link", getFullUrl(ConfirmUserPage.class, pp));

		String content = tmp.asString(variables);

		IOUtils.closeQuietly(tmp);

		return getBaseContent(content);
	}

	public static String getForgottenPasswordContent(Users user, String newPassword) {
		PackageTextTemplate tmp = new PackageTextTemplate(BaseMainTemplate.class, "forgotten_password_template.html", "text/html", "UTF-8");

		Map<String, String> variables = new HashMap<String, String>();

		variables.put("hello", String.format(getString("mail.hello"), user.getName()));
		variables.put("info", String.format(getString("mail.forgotten_password.message")));
		variables.put("new_password", newPassword);

		String content = tmp.asString(variables);
		IOUtils.closeQuietly(tmp);

		return getBaseContent(content);
	}


	private static String getString(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle(BaseMainTemplate.class.getName(), Session.get().getLocale());
		return bundle.getString(key);
	}

	private static String getFullUrl(Class<? extends Page> pageClass, PageParameters pp) {
		return getFullUrl(RequestCycle.get().urlFor(pageClass, pp).toString());
	}

	private static String getFullUrl(String path) {
		URL url = null;
		try {
			url = new URL(RequestCycle.get().getUrlRenderer().renderFullUrl(Url.parse(path)));

			String[] split = url.getHost().split("\\.");
			if (split.length == 3 && !split[0].equals("www")) {
				url = new URL(url.getProtocol(), "www." + split[1] + "." + split[2], url.getPort(), url.getFile());
			}
		} catch (MalformedURLException e) {
			log.error(e);
			return "";
		}

		return url.toString();
	}
}