package bg.bas.iinf.sinus.wicket.common;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

/**
 * Event payload za notifikacii
 * @author hok
 *
 */
public class NotificationEventPayload {

	private AjaxRequestTarget target;
	private String message;
	private NotificationType notificationType;

	/**
	 * kolko vreme da se pokazva syobshtenieto (ms)
	 */
	private Integer timeout;

	public NotificationEventPayload() {
		super();
	}

	public NotificationEventPayload(AjaxRequestTarget target, IModel<String> message, NotificationType notificationType) {
	    this(target, message, notificationType, 10000);
    }

	public NotificationEventPayload(AjaxRequestTarget target, String message, NotificationType notificationType) {
	    this(target, message, notificationType, 10000);
    }

	public NotificationEventPayload(AjaxRequestTarget target, String message, NotificationType notificationType, Integer timeout) {
	    super();
	    this.target = target;
	    this.message = message;
	    this.notificationType = notificationType;
	    this.timeout = timeout;
    }

	public NotificationEventPayload(AjaxRequestTarget target, IModel<String> message, NotificationType notificationType, Integer timeout) {
	    super();
	    this.target = target;
	    this.message = message.getObject();
	    this.notificationType = notificationType;
	    this.timeout = timeout;
    }

	public AjaxRequestTarget getTarget() {
		return target;
	}

	public void setTarget(AjaxRequestTarget target) {
		this.target = target;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public NotificationType getNotificationType() {
    	return notificationType;
    }

	public void setNotificationType(NotificationType notificationType) {
    	this.notificationType = notificationType;
    }

	public Integer getTimeout() {
    	return timeout;
    }

	public void setTimeout(Integer timeout) {
    	this.timeout = timeout;
    }
}
