package bg.bas.iinf.sinus.wicket.owl.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.semanticweb.owlapi.model.OWLLiteral;

/**
 * spisyk s literali i filteringSelect (oneof stoinosti na datatypeproperty)
 *
 * @author hok
 *
 */
public class OWLLiteralsPanel extends LiteralsPanel {

	private static final long serialVersionUID = -2526649421155258520L;

	private IModel<List<String>> literalsModel;

	public OWLLiteralsPanel(String id, IModel<String> model, Set<OWLLiteral> literals) {
		super(id, model);
		this.literalsModel = new LiteralValues(literals);
	}

	@Override
    protected IModel<List<String>> createList() {
		return literalsModel;
	}

	private class LiteralValues implements IModel<List<String>> {

		private static final long serialVersionUID = -1184588316353245817L;

		private List<String> values;

		public LiteralValues(Set<OWLLiteral> literals) {
			super();
			values = new ArrayList<String>();
			for (OWLLiteral l : literals) {
				values.add(l.getLiteral());
			}
		}

		@Override
		public void detach() {
		}

		@Override
		public List<String> getObject() {
			return values;
		}

		@Override
		public void setObject(List<String> object) {
		}
	}
}
