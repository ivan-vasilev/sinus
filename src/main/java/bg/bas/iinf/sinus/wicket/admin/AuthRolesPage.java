package bg.bas.iinf.sinus.wicket.admin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;

import bg.bas.iinf.sinus.hibernate.dao.Home;
import bg.bas.iinf.sinus.hibernate.dao.UsersHome;
import bg.bas.iinf.sinus.hibernate.entity.Users;
import bg.bas.iinf.sinus.hibernate.filter.PaginationFilter;
import bg.bas.iinf.sinus.hibernate.filter.StringFilter;
import bg.bas.iinf.sinus.hibernate.filter.StringFilter.STRING_MATCH;
import bg.bas.iinf.sinus.hibernate.filter.UsersFilter;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;
import bg.bas.iinf.sinus.wicket.auth.AuthRoles;
import bg.bas.iinf.sinus.wicket.auth.UserSession;
import bg.bas.iinf.sinus.wicket.common.AjaxAutoCompleteSearchForm;
import bg.bas.iinf.sinus.wicket.common.NotificationEventPayload;
import bg.bas.iinf.sinus.wicket.common.NotificationType;
import bg.bas.iinf.sinus.wicket.common.RefreshListEP;
import bg.bas.iinf.sinus.wicket.model.UserLDM;
import css.CSS;

/**
 * stranica za redaktirane na potrebitelskite prava
 * @author hok
 *
 */
@AuthorizeInstantiation(AuthRoles.ADMIN_CONST)
public class AuthRolesPage extends BaseAdminPage {

	private static final long serialVersionUID = 7422902123086949784L;

	public AuthRolesPage() {
		super();
	}

	public AuthRolesPage(PageParameters parameters) {
		super(parameters);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		IModel<UsersFilter> m = new Model<UsersFilter>(new UsersFilter());
		setDefaultModel(m);

		add(new AjaxAutoCompleteSearchForm<UsersFilter>("search_form", m) {

            private static final long serialVersionUID = 2241673835880856944L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				if (StringUtils.isEmpty(searchField.getConvertedInput())) {
					getModelObject().setName(null);
				} else {
					getModelObject().setName(new StringFilter(STRING_MATCH.STARTS_WITH, searchField.getConvertedInput()));
				}

				send(getPage(), Broadcast.BREADTH, new RefreshListEP(target));
			}

			@Override
			protected Iterator<String> getChoices(String input) {
				if (!StringUtils.isEmpty(input)) {
					UsersFilter filter = new UsersFilter();
					filter.setName(new StringFilter(STRING_MATCH.STARTS_WITH, input));
					return UsersHome.getUserNames(ScopedEntityManagerFactory.getEntityManager(), filter).iterator();
				}

				return Collections.<String>emptyList().iterator();
			}
		});

		final UsersDataProvider dataProvider = new UsersDataProvider();

		final WebMarkupContainer usersContainer = new WebMarkupContainer("container") {

			private static final long serialVersionUID = -7109508601691145942L;

			@Override
			public void onEvent(IEvent<?> event) {
				if (event.getPayload() instanceof RefreshListEP) {
					((RefreshListEP) event.getPayload()).getTarget().add(this);
				}
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(dataProvider.size() > 0);
			}
		};
		usersContainer.setOutputMarkupId(true);
		usersContainer.setOutputMarkupPlaceholderTag(true);
		add(usersContainer);

		UsersDataView dataView = new UsersDataView("users", dataProvider, 25);
		usersContainer.add(dataView);
		usersContainer.add(new AjaxPagingNavigator("paging", dataView));

		Label noResults = new Label("no_results", new ResourceModel("no_results")) {

			private static final long serialVersionUID = -8954753516804567813L;

			@Override
			public void onEvent(IEvent<?> event) {
				if (event.getPayload() instanceof RefreshListEP) {
					((RefreshListEP) event.getPayload()).getTarget().add(this);
				}
			}

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(dataProvider.size() == 0);
			}
		};
		noResults.setOutputMarkupId(true);
		noResults.setOutputMarkupPlaceholderTag(true);
		add(noResults);
	}

	@Override
	public IModel<String> getPageTitle() {
		return new StringResourceModel("auth_roles", this, null);
	}

	@Override
	public IModel<String> getDescription() {
		return new StringResourceModel("auth_roles", this, null);
	}

	@Override
    public void renderHead(final IHeaderResponse response) {
		super.renderHead(response);
		response.renderCSSReference(new PackageResourceReference(CSS.class, "list.css"));
	}

	private class UsersDataProvider implements IDataProvider<Users> {

		private static final long serialVersionUID = -3091072623189820198L;

		private transient Integer size;

		@Override
		public void detach() {
			size = null;
		}

		@Override
		public Iterator<? extends Users> iterator(int first, int count) {
			UsersFilter filter = (UsersFilter) AuthRolesPage.this.getDefaultModelObject();
			filter.setPaging(new PaginationFilter(first, count));
			return UsersHome.getUsers(ScopedEntityManagerFactory.getEntityManager(), filter).iterator();
		}

		@Override
		public int size() {
			if (size == null) {
				size = UsersHome.getUsersCount(ScopedEntityManagerFactory.getEntityManager(), (UsersFilter) AuthRolesPage.this.getDefaultModelObject()).intValue();
			}

			return size;
		}

		@Override
		public IModel<Users> model(Users object) {
			return new UserLDM(object);
		}
	}

	private static class UsersDataView extends DataView<Users> {

		private static final long serialVersionUID = 6525645372662704393L;

		public UsersDataView(String id, IDataProvider<Users> dataProvider, int itemsPerPage) {
			super(id, dataProvider, itemsPerPage);
		}

		@Override
		protected void populateItem(final Item<Users> item) {
			item.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));

			Form<Users> form = new Form<Users>("auth_roles_form", item.getModel());
			item.add(form);

			IChoiceRenderer<AuthRoles> renderer = new IChoiceRenderer<AuthRoles>() {

                private static final long serialVersionUID = -6267498167621004540L;

				@Override
				public Object getDisplayValue(AuthRoles object) {
					switch (object) {
					case ADMIN: return getString("admin");
					case LIBRARIAN: return getString("librarian");
					case STUDENT: return getString("student");
					case ANNOTATOR: return getString("annotator");
					}
					return "";
				}

				@Override
				public String getIdValue(AuthRoles object, int index) {
					return String.valueOf(index);
				}
			};

			IModel<List<AuthRoles>> authRoles = new LoadableDetachableModel<List<AuthRoles>>() {

                private static final long serialVersionUID = 8267773883893270929L;

				@Override
				protected List<AuthRoles> load() {
					return Arrays.asList(AuthRoles.values());
				}
			};

			IModel<Collection<AuthRoles>> userAuthRoles = new LoadableDetachableModel<Collection<AuthRoles>>() {

                private static final long serialVersionUID = -3129977554131234257L;

				@Override
				protected Collection<AuthRoles> load() {
					List<AuthRoles> result = new LinkedList<AuthRoles>();
					for (String s : UserSession.getRoles(item.getModelObject())) {
						result.add(AuthRoles.valueOf(s));
					}

					return result;
				}
			};

			final CheckBoxMultipleChoice<AuthRoles> roles = new CheckBoxMultipleChoice<AuthRoles>("roles", userAuthRoles, authRoles, renderer);
			roles.setSuffix("");
			form.add(roles);

			form.add(new AjaxButton("submit") {

                private static final long serialVersionUID = -3509768438631782067L;

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					Users u = (Users) form.getModelObject();
					List<String> userRoles = new LinkedList<String>();
					for (AuthRoles r : roles.getConvertedInput()) {
						userRoles.add(r.toString());
					}

					u.setAuthRole(StringUtils.join(userRoles, ","));
					Home.persistInOneTransaction(ScopedEntityManagerFactory.getEntityManager(), u);
					send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(target, new ResourceModel("save_success"), NotificationType.INFO));
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
				}
			});
		}
	}
}
