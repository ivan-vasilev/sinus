package bg.bas.iinf.sinus.wicket.common;

import java.io.Serializable;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

/**
 * Visitor za dobavqne na ErrorHighlightingBehavior
 * @author hok
 *
 */
public class FormErrorVisitor implements IVisitor<FormComponent<?>, Void>, Serializable {

	private static final long serialVersionUID = 3396182520509267958L;

	@Override
	public void component(FormComponent<?> object, IVisit<Void> visit) {
		if (object.getBehaviors(ErrorHighlightingBehavior.class).size() == 0) {
			object.add(new ErrorHighlightingBehavior());
		}
	}
}