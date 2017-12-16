package bg.bas.iinf.sinus.wicket.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import bg.bas.iinf.sinus.wicket.common.AuthBookmarkablePageLink;
import bg.bas.iinf.sinus.wicket.common.TabLinksComponent;
import bg.bas.iinf.sinus.wicket.page.BasePage;

/**
 * bazova stranica (header i footer) za vsichki ostanali
 * @author hok
 *
 */
public abstract class BaseAdminPage extends BasePage {

	private static final long serialVersionUID = 2248204753993255273L;

	public BaseAdminPage() {
		super();
	}

	public BaseAdminPage(IModel<?> model) {
		super(model);
	}

	public BaseAdminPage(PageParameters parameters) {
		super(parameters);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		IModel<List<? extends AbstractLink>> m = new LoadableDetachableModel<List<? extends AbstractLink>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<? extends AbstractLink> load() {
				List<AbstractLink> list = new ArrayList<AbstractLink>();

				AuthBookmarkablePageLink<AuthRolesPage> authRoles = new AuthBookmarkablePageLink<AuthRolesPage>(TabLinksComponent.TAB_LINK_ID, AuthRolesPage.class);
				authRoles.setBody(new StringResourceModel("auth_roles", BaseAdminPage.this, null));
				list.add(authRoles);

				AuthBookmarkablePageLink<OntologiesPage> ontologies = new AuthBookmarkablePageLink<OntologiesPage>(TabLinksComponent.TAB_LINK_ID, OntologiesPage.class);
				ontologies.setBody(new StringResourceModel("ontologies", BaseAdminPage.this, null));
				list.add(ontologies);

				AuthBookmarkablePageLink<AnnotationsConfigurationPage> annotations = new AuthBookmarkablePageLink<AnnotationsConfigurationPage>(TabLinksComponent.TAB_LINK_ID, AnnotationsConfigurationPage.class);
				annotations.setBody(new StringResourceModel("annotations", BaseAdminPage.this, null));
				list.add(annotations);

				return list;
			}
		};
		add(new TabLinksComponent("page_tabs", m, getPageClass()));

		add(createTopRightComponent("top_right"));
	}

	protected Component createTopRightComponent(String id) {
		return new EmptyPanel(id).setVisible(false);
	}
}
