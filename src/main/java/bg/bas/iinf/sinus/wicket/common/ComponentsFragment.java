package bg.bas.iinf.sinus.wicket.common;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.panel.Fragment;

/**
 * Fragment, koito sydyrja drugi fragmenti
 * @author hok
 *
 */
public class ComponentsFragment extends Fragment {

	private static final long serialVersionUID = 5396466081287096769L;

	protected Component[] component;

	public ComponentsFragment(String id, String markupId, MarkupContainer markupProvider, Component... component) {
		super(id, markupId, markupProvider);

		this.component = component;
	}

    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (component != null) {
        	for (int i = 0; i < component.length; i++) {
        		add(component[i]);
        	}
        }

	}
}
