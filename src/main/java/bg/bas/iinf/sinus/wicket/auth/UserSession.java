package bg.bas.iinf.sinus.wicket.auth;

import java.net.URLDecoder;

import javax.servlet.http.Cookie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.util.string.Strings;

import bg.bas.iinf.sinus.hibernate.dao.UsersHome;
import bg.bas.iinf.sinus.hibernate.entity.Users;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;

/**
 * Potrebitelska sesiq - tuk se syhranqva systoqnieto i informaciqta za potrebitelq
 * @author hok
 *
 */
public class UserSession extends AbstractAuthenticatedWebSession {

    private static final long serialVersionUID = -3163627768585929685L;

	private static final Log log = LogFactory.getLog(UserSession.class);

	public static final String COOKIE_USERNAME = "cpun";
	public static final String COOKIE_PASSWORD = "cppw";
	private static final String USER_ID = "user_id";
	private static final String SEARCH_SESSION_ID = "search_session";
	private static final String IS_EMBEDDED = "is_embedded";

	public UserSession(Request request) {
		super(request);
	}

	public Integer getUserId() {
		return (Integer) getAttribute(USER_ID);
	}

	public void setUserId(Integer userId) {
		setAttribute(USER_ID, userId);
	}

	/**
	 * Creates a cookie with name and value. Valid for 1 year
	 *
	 * @param aName
	 *            cookie name
	 * @param aValue
	 *            cookie value
	 * @return created cookie
	 */
	public static Cookie createCookie(String aName, String aValue) {
		Cookie cookie = new Cookie(aName, aValue);

		cookie.setVersion(0);
		cookie.setPath("/");
		cookie.setDomain("localhost");
		// validno okolo 1 dogina
		cookie.setMaxAge(31536000);

		return cookie;
	}

	/**
	 * Checks the given username and password, returning a User object if if the
	 * username and password identify a valid user. The password id hash
	 *
	 * @param username
	 *            The username
	 * @param password
	 *            The password
	 * @return True if the user was authenticated
	 * @throws UserBlockedException
	 */
	public final boolean authenticate(final String username, final byte[] password) {
		if (getUserId() == null) {
			try {
				Users user = null;
				if (getApplication().usesDevelopmentConfig()) {
					user = UsersHome.findByUsernameOrEmail(ScopedEntityManagerFactory.getEntityManager(), username);
				} else {
					user = UsersHome.findByUsernameOrEmailAndPasswod(ScopedEntityManagerFactory.getEntityManager(), username, password);
				}

				if (user == null || user.getUsersConfirmation().size() > 0) {
					return false;
				}

				setUserId(user.getUsersId());

				return true;
			} catch (Exception e) {
				log.error(e);
			}
		}

		return false;
	}


	/**
	 * prowerqwa za cookita i fb - ako ima logva potrebitelq.
	 */
	public void tryAutoLogin(WebRequest request, WebResponse response) {
		Cookie user = request.getCookie(UserSession.COOKIE_USERNAME);
		Cookie pass = request.getCookie(UserSession.COOKIE_PASSWORD);
		if (user != null && pass != null) {
			try {
				String passHash = URLDecoder.decode(pass.getValue(), "UTF-8");
				boolean status = authenticate(user.getValue(), passHash.getBytes());
				if (!status) {
					response.clearCookie(user);
					response.clearCookie(pass);
				}
			} catch (Exception e) {
				log.error(e);
				response.clearCookie(user);
				response.clearCookie(pass);
			}
		}
	}

	public static UserSession get() {
		return (UserSession) Session.get();
	}

	public Users getUser() {
		return getUserId() != null ? UsersHome.findById(ScopedEntityManagerFactory.getEntityManager(), getUserId()) : null;
	}

	public String getLoggedEmail() {
		Users u = getUser();
		if (u != null) {
			return u.getEmail();
		}

		return null;
	}

	public void signOut() {
		setUserId(null);
	}

	@Override
	public Roles getRoles() {
		if (getUserId() == null) {
			return new Roles();
		}

		return getRoles(getUser());
	}

	public static Roles getRoles(Users user) {
		if (Strings.isEmpty(user.getAuthRole())) {
			return new Roles();
		}

		return new Roles(user.getAuthRole().split(","));
	}

	@Override
    public boolean isSignedIn() {
	    return getUserId() != null;
    }

    public boolean isEmbedded() {
    	Boolean result = false;
    	if (getAttribute(IS_EMBEDDED) != null) {
    		result = (Boolean) getAttribute(IS_EMBEDDED);
    	}

    	return result;
    }

    public void seIsEmbedded(boolean isEmbedded) {
		setAttribute(IS_EMBEDDED, isEmbedded);
	}

	public String getSearchSessionId() {
		return (String) getAttribute(SEARCH_SESSION_ID);
	}

	public void setSearchSessionId(String searchSessionId) {
		setAttribute(SEARCH_SESSION_ID, searchSessionId);
	}
}
