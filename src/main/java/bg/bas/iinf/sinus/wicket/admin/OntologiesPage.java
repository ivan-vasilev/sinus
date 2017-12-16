package bg.bas.iinf.sinus.wicket.admin;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.lang.Bytes;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import bg.bas.iinf.sinus.hibernate.dao.Home;
import bg.bas.iinf.sinus.hibernate.dao.OntologiesHome;
import bg.bas.iinf.sinus.hibernate.entity.Ontologies;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;
import bg.bas.iinf.sinus.wicket.auth.AuthRoles;
import bg.bas.iinf.sinus.wicket.common.ConfirmAjaxLink;
import bg.bas.iinf.sinus.wicket.common.ErrorHighlightingForm;
import bg.bas.iinf.sinus.wicket.common.NotificationEventPayload;
import bg.bas.iinf.sinus.wicket.common.NotificationType;
import bg.bas.iinf.sinus.wicket.common.RefreshListEP;
import bg.bas.iinf.sinus.wicket.common.YesNoLabel;
import bg.bas.iinf.sinus.wicket.model.OntologyLDM;
import css.CSS;

/**
 * stranica za upravlenie na ontologii
 * @author hok
 *
 */
@AuthorizeInstantiation({AuthRoles.ADMIN_CONST, AuthRoles.LIBRARIAN_CONST})
public class OntologiesPage extends BaseAdminPage {

	private static final Log log = LogFactory.getLog(OntologiesPage.class);

	private static final long serialVersionUID = 7422902123086949784L;

	public OntologiesPage() {
		super();
	}

	public OntologiesPage(PageParameters parameters) {
		super(parameters);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		// upload
		ErrorHighlightingForm<Void> uploadForm = new ErrorHighlightingForm<Void>("upload_form");
		add(uploadForm);
		uploadForm.setMultiPart(true);
		uploadForm.setOutputMarkupId(true);
		uploadForm.setMaxSize(Bytes.megabytes(2));

		final FileUploadField uploadField = new FileUploadField("file_input");
		uploadField.setRequired(true);
		uploadForm.add(uploadField);

		// submit
		uploadForm.add(new IndicatingAjaxButton("upload_button") {

            private static final long serialVersionUID = 3635787012429075242L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				FileUpload upload = uploadField.getFileUpload();
				ServletContext servletContext = WebApplication.get().getServletContext();
				String realPath = servletContext.getRealPath("ont");
				File f = new File(realPath + File.separator + uploadField.getFileUpload().getClientFileName());

				if (f.exists()) {
					f.delete();
				}

				try {
	                f.createNewFile();
	                upload.writeTo(f);
	                OWLOntology ont = OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(f);

	                Ontologies ontology = new Ontologies();
	                ontology.setId(ont.getOntologyID().toString());
	                ontology.setUri(f.toURI().toString());

	                Home.persistInOneTransaction(ScopedEntityManagerFactory.getEntityManager(), ontology);
				} catch (IOException e) {
	                log.error(e);
	                send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(target, new StringResourceModel("error_upload", this, null), NotificationType.ERROR));
	                return;
                } catch (OWLOntologyCreationException e) {
	                log.error(e);
	                send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(target, new StringResourceModel("error_upload", this, null), NotificationType.ERROR));
	                return;
                }

				uploadField.setConvertedInput(null);
				target.add(form);
				send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(target, new ResourceModel("save_success"), NotificationType.INFO));
				send(getPage(), Broadcast.BREADTH, new RefreshListEP(target));
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(form);
			}
		});

		OntologiesLDM allOntologies = new OntologiesLDM();

		final WebMarkupContainer ontologiesContainer = new WebMarkupContainer("container", allOntologies) {

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
				OntologiesLDM allOntologies = (OntologiesLDM) getDefaultModel();
				setVisible(allOntologies.getObject().size() > 0);
			}
		};
		ontologiesContainer.setOutputMarkupId(true);
		ontologiesContainer.setOutputMarkupPlaceholderTag(true);
		add(ontologiesContainer);

		RefreshingView<Ontologies> ontologiesView = new RefreshingView<Ontologies>("ontologies", allOntologies) {

            private static final long serialVersionUID = 1L;

			@Override
            protected Iterator<IModel<Ontologies>> getItemModels() {
				OntologiesLDM allOntologies = (OntologiesLDM) getDefaultModel();
	            List<IModel<Ontologies>> result = new ArrayList<IModel<Ontologies>>();
	            for (Ontologies o : allOntologies.getObject()) {
	            	result.add(new OntologyLDM(o));
	            }

	            return result.iterator();
            }

			@Override
            protected void populateItem(Item<Ontologies> item) {

				PageParameters pp = new PageParameters();
				pp.set(OntologyConfigurationPage.ONTOLOGY_PARAM, item.getModelObject().getOntologiesId());

				BookmarkablePageLink<OntologyConfigurationPage> defaultDisplayValues = new BookmarkablePageLink<OntologyConfigurationPage>("id", OntologyConfigurationPage.class, pp);
				defaultDisplayValues.setBody(new PropertyModel<String>(item.getModel(), "id"));
				item.add(defaultDisplayValues);

				item.add(new YesNoLabel("is_configured", new PropertyModel<Boolean>(item.getModel(), "isConfigured")));

				item.add(new ConfirmAjaxLink<Ontologies>("delete", item.getModel(), new ResourceModel("confirm")) {

                    private static final long serialVersionUID = 7717677671931809927L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						try {
	                        OntologiesHome.removeInOneTransaction(ScopedEntityManagerFactory.getEntityManager(), getModelObject());
                        } catch (URISyntaxException e) {
        	                log.error(e);
        	                send(getPage(), Broadcast.BREADTH, new NotificationEventPayload(target, new StringResourceModel("error_upload", this, null), NotificationType.INFO));
        	                return;
                        }

						send(getPage(), Broadcast.BREADTH, new RefreshListEP(target));
					}
				});
            }
		};
		ontologiesContainer.add(ontologiesView);

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
				ontologiesContainer.configure();
				setVisible(!ontologiesContainer.isVisible());
			}
		};
		noResults.setOutputMarkupId(true);
		noResults.setOutputMarkupPlaceholderTag(true);
		add(noResults);
	}

	@Override
	public IModel<String> getPageTitle() {
		return new StringResourceModel("ontologies", this, null);
	}

	@Override
	public IModel<String> getDescription() {
		return new StringResourceModel("ontologies", this, null);
	}

	@Override
	public void renderHead(final IHeaderResponse response) {
		super.renderHead(response);
		response.renderCSSReference(new PackageResourceReference(CSS.class, "list.css"));
	}

	private static class OntologiesLDM extends LoadableDetachableModel<List<Ontologies>> {

        private static final long serialVersionUID = -5915651348496162074L;

		@Override
		protected List<Ontologies> load() {
			return OntologiesHome.getOntologies(ScopedEntityManagerFactory.getEntityManager(), null);
		}
	};
}