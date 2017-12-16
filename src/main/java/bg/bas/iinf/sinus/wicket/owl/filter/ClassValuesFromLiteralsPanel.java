package bg.bas.iinf.sinus.wicket.owl.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLQuantifiedRestriction;

import bg.bas.iinf.sinus.cache.CacheFactory;

/**
 * spisyk s literali i filteringSelect (oneof stoinosti na datatypeproperty)
 *
 * @author hok
 *
 */
public class ClassValuesFromLiteralsPanel extends LiteralsPanel {

	private static final long serialVersionUID = -2526649421155258520L;

	private IModel<OWLQuantifiedRestriction<?, ?, ?>> restrictionModel;

	public ClassValuesFromLiteralsPanel(String id, IModel<String> model, OWLQuantifiedRestriction<?, ?, ?> restriction) {
		super(id, model);
		this.restrictionModel = new QuantifiedRestrictionModel(restriction);
	}

	@Override
    protected IModel<List<String>> createList() {
		return new LoadableDetachableModel<List<String>>() {

			private static final long serialVersionUID = -9054068871794007955L;

            @Override
			protected List<String> load() {
				OWLDataOneOf oneOf = (OWLDataOneOf) restrictionModel.getObject().getFiller();

				List<String> result = new ArrayList<String>();
				for (OWLLiteral l : oneOf.getValues()) {
					result.add(l.getLiteral());
				}
				Collections.sort(result);

				return result;
			}
		};
	}


	private static class QuantifiedRestrictionModel extends LoadableDetachableModel<OWLQuantifiedRestriction<?, ?, ?>> {

		private static final long serialVersionUID = -5166096006035754350L;

		private String cacheKey;

		public QuantifiedRestrictionModel(OWLQuantifiedRestriction<?, ?, ?> object) {
			super(object);
			cacheKey = object.toString();
			CacheFactory.getGeneralCache().put(cacheKey, object);
		}

		@Override
		protected OWLQuantifiedRestriction<?, ?, ?> load() {
			return (OWLQuantifiedRestriction<?, ?, ?>) CacheFactory.getGeneralCache().get(cacheKey);
		}
	};
}
