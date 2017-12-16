package bg.bas.iinf.sinus.wicket.common;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Util klas za Dojo neshta
 * @author hok
 *
 */
public class DojoUtils {

	private static final String DESTROY_WIDGETS = "dojo.forEach(dijit.findWidgets(dojo.byId('%s')), function(w) { w.destroyRecursive(true) });";

	public static void refreshCustomCombos(AjaxRequestTarget target, MarkupContainer c) {
		target.appendJavaScript(
				String.format(
    				"initCheckBoxes('%s')", c.getMarkupId()));
	}

	/**
	 * obnovqva dojo widget-ite v komponent
	 * zadyljitelno se izvikva zaedno s target.add(c);
	 * @param target
	 * @param c
	 */
	public static void refreshWidgets(AjaxRequestTarget target, Component c) {
		target.prependJavaScript(String.format(DESTROY_WIDGETS, c.getMarkupId(true)));

		refreshWidget(target, c.getMarkupId());

		target.appendJavaScript(
				String.format(
    				"dojo.addOnLoad(function() {" +
    				"dojo.parser.parse('%s')" +
    				"})", c.getMarkupId()));
	}

	/**
	 * premahva komponenta ot dom-dyrvoto s Javascript
	 * zadyljitelno komponenta trqbva da ima id
	 * @param target
	 * @param c
	 */
	public static void removeComponent(AjaxRequestTarget target, Component c) {
		if (c.getOutputMarkupId()) {
			target.appendJavaScript(String.format(
	    								"dojo.addOnLoad(function() {" +
	    								"dojo.query('#%s').orphan()" +
	    								"});", c.getMarkupId(true))
	    							);
		}
	}

	/**
	 * validira forma i obnovqva widgetite
	 * @param target
	 * @param c
	 */
	public static void validateForm(AjaxRequestTarget target, MarkupContainer c) {
		target.prependJavaScript(String.format(DESTROY_WIDGETS, c.getMarkupId()));

		target.appendJavaScript(
				String.format(
						"dojo.addOnLoad(function() {" +
						"dojo.parser.parse('%s')" +
						"});"
						, c.getMarkupId(true)));
	}

	public static void refreshWidget(AjaxRequestTarget target, String id) {
		target.appendJavaScript(
				String.format("dojo.addOnLoad(function() {" +
    				"var w = dijit.byId('%s');" +
    				"if (w) {" +
    				"w.destroy(true);" +
    				"dojo.parser.instantiate([dojo.byId('%s')]);" +
    				"}" +
    				"});", id, id));
	}
}
