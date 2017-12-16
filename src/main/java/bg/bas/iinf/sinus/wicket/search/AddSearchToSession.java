package bg.bas.iinf.sinus.wicket.search;

import java.util.Iterator;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import bg.bas.iinf.sinus.hibernate.dao.Home;
import bg.bas.iinf.sinus.hibernate.dao.SavedSearchesHome;
import bg.bas.iinf.sinus.hibernate.entity.SavedSearches;
import bg.bas.iinf.sinus.hibernate.filter.PaginationFilter;
import bg.bas.iinf.sinus.hibernate.filter.SavedSearchesFilter;
import bg.bas.iinf.sinus.hibernate.filter.StringFilter;
import bg.bas.iinf.sinus.hibernate.filter.StringFilter.STRING_MATCH;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;
import bg.bas.iinf.sinus.wicket.auth.UserSession;

/**
 * panel za dobavqne na zapomneno tyrsene kym dadena sesiq
 * @author hok
 *
 */
public class AddSearchToSession extends GenericPanel<SavedSearches> {

    private static final long serialVersionUID = 7977790332824317415L;

	public AddSearchToSession(String id, IModel<SavedSearches> model) {
	    super(id, model);
    }

	@Override
	protected void onInitialize() {
		super.onInitialize();
		Form<SavedSearches> form = new Form<SavedSearches>("form", getModel());
		form.setOutputMarkupId(true);
		add(form);

		form.add(new AutoCompleteTextField<String>("tag", new PropertyModel<String>(getModel(), "tag")) {

			private static final long serialVersionUID = 1L;

			@Override
			protected Iterator<String> getChoices(String input) {
				SavedSearchesFilter f = new SavedSearchesFilter();
				f.setUserId(UserSession.get().getUserId());
				f.setTag(new StringFilter(STRING_MATCH.STARTS_WITH, input));
				return SavedSearchesHome.getSavedSearchesTags(ScopedEntityManagerFactory.getEntityManager(), f, new PaginationFilter(0, 15)).iterator();
			}
		});

		form.add(new AjaxButton("submit") {

            private static final long serialVersionUID = 1907271531098912410L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Home.persistInOneTransaction(ScopedEntityManagerFactory.getEntityManager(), form.getModelObject());
				send(getPage(), Broadcast.BREADTH, new TagAddedEP(target, (SavedSearches) form.getModelObject()));
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(form);
			}
		});
	}

	public static class TagAddedEP {
		private AjaxRequestTarget target;
		private SavedSearches search;

		public TagAddedEP(AjaxRequestTarget target, SavedSearches search) {
			super();
			this.target = target;
			this.search = search;
		}

		public AjaxRequestTarget getTarget() {
			return target;
		}

		public SavedSearches getSearch() {
			return search;
		}
	}
}
