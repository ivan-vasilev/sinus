package bg.bas.iinf.sinus.wicket.common;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * v dolniq desen ygyl pokazva slujebni syobshteniq
 * @author hok
 *
 */
public class NotificationComponent extends Panel {

    private static final long serialVersionUID = 5947086842023156519L;

    public static final int DEFAULT_TIMEOUT = 10000;

    public NotificationComponent(String id) {
	    super(id);
    }

	@Override
	public void onEvent(IEvent<?> event) {
		if (event.getPayload() instanceof NotificationEventPayload) {
			NotificationEventPayload payload = (NotificationEventPayload) event.getPayload();
			AjaxRequestTarget target = payload.getTarget();
			int timeout = payload.getTimeout() != null ? payload.getTimeout() : DEFAULT_TIMEOUT;
			target.appendJavaScript("addnotification(\"" + payload.getMessage() + "\", '" + payload.getNotificationType() + "', " + timeout + ")");
		}
	}

	@Override
	public void renderHead(final IHeaderResponse response) {
		response.renderJavaScriptReference(new PackageResourceReference(NotificationComponent.class, NotificationComponent.class.getSimpleName() + ".js"));
		response.renderCSSReference(new PackageResourceReference(NotificationComponent.class, NotificationComponent.class.getSimpleName() + ".css"));
	}
}
