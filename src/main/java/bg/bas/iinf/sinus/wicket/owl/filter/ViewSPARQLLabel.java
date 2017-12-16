package bg.bas.iinf.sinus.wicket.owl.filter;

import java.util.Set;

import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import bg.bas.iinf.sinus.repository.QueryUtil;

/**
 * pokazva SPARQL na baza na stoinostite ot OWLClassFilterDisplayPanel
 * @author hok
 *
 */
public class ViewSPARQLLabel extends MultiLineLabel {

    private static final long serialVersionUID = 8782361372214219602L;

    private OWLClassFilterDisplayPanel panel;
    private IModel<Set<String>> IRIs;

    public ViewSPARQLLabel(String id, OWLClassFilterDisplayPanel panel, IModel<Set<String>> IRIs) {
		super(id);
		this.panel = panel;
		this.IRIs = IRIs;
		setDefaultModel(new ViewSPARQLLDM());
	}

	private class ViewSPARQLLDM extends LoadableDetachableModel<String> {

        private static final long serialVersionUID = 4042252295091147462L;

        @Override
		protected String load() {
			return QueryUtil.createFullFilterQueryString(panel, IRIs.getObject());
		}
	}
}
