package bg.bas.iinf.sinus.wicket.common;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * pokazva da/ne v zavisimost ot bulevata promenliva
 *
 * @author hok
 *
 */
public class YesNoLabel extends Label {

    private static final long serialVersionUID = -2941395921492976824L;

    private IModel<Boolean> isPaid;

	public YesNoLabel(String id, IModel<Boolean> isPaid) {
		super(id);
		this.isPaid = isPaid;
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		isPaid.detach();
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		Boolean yesNo = isPaid.getObject();
		if (yesNo == null) {
			setVisible(false);
		} else if (yesNo == true) {
	    	setDefaultModel(new StringResourceModel("yes", this, null));
	    } else {
	    	setDefaultModel(new StringResourceModel("no", this, null));
	    }
	}
}
