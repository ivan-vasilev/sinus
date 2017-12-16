package bg.bas.iinf.sinus.wicket.owl.filter.searchresults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.UrlValidator;

import bg.bas.iinf.sinus.wicket.owl.ViewObjectPage;
import bg.bas.iinf.sinus.wicket.owl.annotations.AnnotateObjectPage;
import bg.bas.iinf.sinus.wicket.owl.filter.searchresults.SearchResultsDataView.OWLNamedObjectResult;

/**
 * DataView, koeto pokazva "liftnati" obekti na baza izbrani property-ta
 * @author hok
 *
 * @param <T>
 */
public class SearchResultsDataView<T> extends DataView<OWLNamedObjectResult<T>> {

	private String classIRI;

	public SearchResultsDataView(String id, String classIRI, IDataProvider<OWLNamedObjectResult<T>> dataProvider, int itemsPerPage) {
		super(id, dataProvider, itemsPerPage);
		this.classIRI = classIRI;
	}

	private static final long serialVersionUID = -4744872461512107366L;

	@Override
	protected void populateItem(final Item<OWLNamedObjectResult<T>> bindingItem) {
		addViewObjectLink(bindingItem);
		addAnnotateObjectLink(bindingItem);

		bindingItem.add(new Label("values", new LoadableDetachableModel<String>() {

			private static final long serialVersionUID = 5255146289518502526L;

			@Override
			protected String load() {
				UrlValidator urlValidator = new UrlValidator();
				StringBuilder result = new StringBuilder();
				for (String s : bindingItem.getModelObject().getProperties()) {
					result.append("<td>");
					if (urlValidator.isValid(s)) {
						if (s.endsWith("jpg") || s.endsWith("png") || s.endsWith("gif")) {
							result.append("<img src=\"").append(s).append("\" />");
						} else {
							result.append("<a href=\"").append(s).append("\">").append(s).append("</a>");
						}
					} else {
						result.append(s);
					}
					result.append("</td>");
				}

				return result.toString();
			}
		}).setEscapeModelStrings(false));
	}

	protected void addViewObjectLink(Item<OWLNamedObjectResult<T>> bindingItem) {
		PageParameters pp = new PageParameters();
		pp.set(ViewObjectPage.PARAMETER_CLASS, classIRI);
		pp.set(ViewObjectPage.PARAMETER_URI, bindingItem.getModelObject().getIri());

		bindingItem.add(new BookmarkablePageLink<ViewObjectPage>("view_object", ViewObjectPage.class, pp));
	}

	protected void addAnnotateObjectLink(Item<OWLNamedObjectResult<T>> bindingItem) {
		PageParameters pp = new PageParameters();
		pp.set(ViewObjectPage.PARAMETER_CLASS, classIRI);
		pp.set(ViewObjectPage.PARAMETER_URI, bindingItem.getModelObject().getIri());

		bindingItem.add(new BookmarkablePageLink<AnnotateObjectPage>("annotate_object", AnnotateObjectPage.class, pp));
	}

	public static class OWLNamedObjectResult<T> implements Serializable {

		private static final long serialVersionUID = 5779868357108806167L;

		private T iri;
		private List<String> properties;

		public OWLNamedObjectResult(T iri) {
			super();
			this.iri = iri;
			properties = new ArrayList<String>();
		}

		public T getIri() {
			return iri;
		}

		public void setIri(T iri) {
			this.iri = iri;
		}

		public List<String> getProperties() {
			return properties;
		}
	}
}
