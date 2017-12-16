package bg.bas.iinf.sinus.wicket.common;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

/**
 * pokazva "nqma namereni rezultati" ako dataview-to nqma rezultat
 *
 * @author hok
 *
 */
public class NoResultsLabel extends Label {

	private static final long serialVersionUID = 7236547109179886566L;

	protected final DataView<?> dataView;
	protected final IDataProvider<?> dataProvider;
	protected final Component[] hideComponents;

	public NoResultsLabel(String id, IModel<String> model, DataView<?> dataView, Component... hideComponents) {
		super(id, model);
		this.dataView = dataView;
		this.hideComponents = hideComponents;
		this.dataProvider = null;
	}

	public NoResultsLabel(String id, IModel<String> model, IDataProvider<?> dataProvider, Component... hideComponents) {
		super(id, model);
		this.dataView = null;
		this.dataProvider = dataProvider;
		this.hideComponents = hideComponents;
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();

		if ((dataProvider != null && dataProvider.size() == 0) ||
			(dataView != null && dataView.getDataProvider().size() == 0)) {
			setVisible(true);

			if (dataView != null) {
				dataView.setVisible(false);
			}

			for (Component c : hideComponents) {
				c.setVisible(false);
			}
		} else {
			setVisible(false);
			if (dataView != null) {
				dataView.setVisible(true);
			}

			for (Component c : hideComponents) {
				c.setVisible(true);
			}
		}
	}
}
