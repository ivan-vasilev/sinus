package bg.bas.iinf.sinus.wicket.search;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import bg.bas.iinf.sinus.hibernate.filter.SavedSearchesFilter;
import bg.bas.iinf.sinus.wicket.auth.UserSession;
import bg.bas.iinf.sinus.wicket.common.ErrorHighlightingForm;

/**
 * panel za izbor na sesiq, kym koqto shte sa prilojeni novite tyrseniq
 * @author hok
 *
 */
public class SelectSessionPanel extends GenericPanel<SavedSearchesFilter> {

	private static final long serialVersionUID = -2759708847484034465L;

	public SelectSessionPanel(String id, IModel<SavedSearchesFilter> model) {
	    super(id, model);
    }

	@Override
	protected void onInitialize() {
		super.onInitialize();

		Form<Void> form = new ErrorHighlightingForm<Void>("form") {

			private static final long serialVersionUID = -234002583366434447L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(StringUtils.isEmpty(UserSession.get().getSearchSessionId()));
			}
		};
		add(form.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

		final TextField<String> newSearchSession = new TextField<String>("new_session", new Model<String>());
		form.add(newSearchSession);

		form.add(new AjaxButton("add_new_session") {

			private static final long serialVersionUID = 7086318063788228631L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				UserSession.get().setSearchSessionId(newSearchSession.getConvertedInput());
				send(getPage(), Broadcast.BREADTH, new SessionSelectedEP(target, newSearchSession.getConvertedInput()));
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
		});

		SavedSearchesListLDM savedSearchesListLDM = new SavedSearchesListLDM() {

			private static final long serialVersionUID = 1L;

			@Override
			public SavedSearchesFilter getFilter() {
				return (SavedSearchesFilter) SelectSessionPanel.this.getDefaultModelObject();
			}
		};

		final DropDownChoice<String> tags = new DropDownChoice<String>("tags", new Model<String>(), savedSearchesListLDM) {

			private static final long serialVersionUID = -1088597837551548079L;

			@Override
			protected String getNullValidKey() {
				return "search_sessions";
			}

			@Override
			protected String getNullKey() {
				return "search_sessions";
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(getChoices().size() > 0);
			}
		};
		form.add(tags.setNullValid(true).setRequired(false).setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

		form.add(new AjaxButton("select_existing_session") {

			private static final long serialVersionUID = 7086318063788228631L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (tags.getConvertedInput() != null) {
					UserSession.get().setSearchSessionId(tags.getConvertedInput());
				} else {
					UserSession.get().setSearchSessionId(null);
				}

				send(getPage(), Broadcast.BREADTH, new SessionSelectedEP(target, tags.getConvertedInput()));
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
		});
	}

	public static class SessionSelectedEP {
		private AjaxRequestTarget target;
		private String sessionId;

		public SessionSelectedEP(AjaxRequestTarget target, String sessionId) {
			super();
			this.target = target;
			this.sessionId = sessionId;
		}

		public AjaxRequestTarget getTarget() {
			return target;
		}

		public String getSessionId() {
			return sessionId;
		}

	}
}
