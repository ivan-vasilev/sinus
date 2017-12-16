package bg.bas.iinf.sinus.wicket.search;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.PackageResourceReference;

import bg.bas.iinf.sinus.hibernate.entity.SavedSearches;
import bg.bas.iinf.sinus.hibernate.filter.SavedSearchesFilter;
import bg.bas.iinf.sinus.hibernate.filter.StringFilter;
import bg.bas.iinf.sinus.hibernate.filter.StringFilter.STRING_MATCH;
import bg.bas.iinf.sinus.wicket.auth.UserSession;
import bg.bas.iinf.sinus.wicket.model.HumanReadableLDM;
import css.CSS;

/**
 * Fragment, sydyrjasht zapomnenite tyrseniq
 * @author hok
 *
 */
public class SavedSearchesFragment extends Fragment {

    private static final long serialVersionUID = 1588945123646225848L;

    private DataView<SavedSearches> dataView;

	public SavedSearchesFragment(String id, String markupId, MarkupContainer markupProvider, IModel<SavedSearchesFilter> model) {
	    super(id, markupId, markupProvider, model);
	    model.getObject().setUserId(UserSession.get().getUserId());
    }

    @Override
	protected void onInitialize() {
		super.onInitialize();

		setOutputMarkupId(true);

		final WebMarkupContainer container = new WebMarkupContainer("container", getDefaultModel()) {

            private static final long serialVersionUID = 2828450278147997463L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(dataView.getDataProvider().size() > 0);
			}
		};
		container.setOutputMarkupId(true);
		container.setOutputMarkupPlaceholderTag(true);
		add(container);

		SavedSearchesDataProvider dataProvider = new SavedSearchesDataProvider((SavedSearchesFilter) getDefaultModelObject()) {

            private static final long serialVersionUID = 5397742998659916969L;

			@Override
		    public int size() {
				if (size == null) {
    				SavedSearchesFilter filter = (SavedSearchesFilter) getDefaultModelObject();
    				if (!StringUtils.isEmpty(UserSession.get().getSearchSessionId())) {
    					filter.setTag(new StringFilter(STRING_MATCH.IS_EXACTLY, UserSession.get().getSearchSessionId()));
    				} else {
    					filter.setTag(null);
    					size = 0;
    				}
				}

				return super.size();
			}

		};
		container.add(dataView = new SavedSearchesDataView("data_view", dataProvider, 10));

		AjaxPagingNavigator paging = new AjaxPagingNavigator("paging", dataView);
		container.add(paging);

		add(new Label ("no_results", new StringResourceModel("no_results", this, null)) {

            private static final long serialVersionUID = 1L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(!StringUtils.isEmpty(UserSession.get().getSearchSessionId()) && dataView.getDataProvider().size() == 0);
			}
		}.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));
	}

	@Override
	public void renderHead(final IHeaderResponse response) {
		response.renderCSSReference(new PackageResourceReference(CSS.class, "list.css"));
	}

	/**
	 * spisyk sys zapomneni tyrseniq
	 * @author hok
	 *
	 */
	private class SavedSearchesDataView extends DataView<SavedSearches> {

        private static final long serialVersionUID = 6199412400475791321L;

		protected SavedSearchesDataView(String id, IDataProvider<SavedSearches> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@Override
		protected void populateItem(Item<SavedSearches> item) {
			item.add(new MultiLineLabel("humanReadable", new HumanReadableLDM(item.getModel())).setEscapeModelStrings(false));

			item.add(new IndicatingAjaxLink<SavedSearches>("select", item.getModel()) {

				private static final long serialVersionUID = -2869688748538881198L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					send(getPage(), Broadcast.BREADTH, new SearchSelectedEP(target, getModelObject()));
				}
			});
		}
	}

	public static class SearchSelectedEP {

		private AjaxRequestTarget target;
		private SavedSearches search;

		public SearchSelectedEP(AjaxRequestTarget target, SavedSearches search) {
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
