package bg.bas.iinf.sinus.wicket.common;

import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class TabLinksComponent extends GenericPanel<List<? extends AbstractLink>> {

	private static final long serialVersionUID = -3787015538513923624L;

	private Class<? extends Page> selectedTabPage;

	public static final String TAB_LINK_ID = "link";

	public TabLinksComponent(String id, IModel<List<? extends AbstractLink>> pageLinksModel, Class<? extends Page> selectedPage) {
		super(id, pageLinksModel);

		this.selectedTabPage = selectedPage;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		if (selectedTabPage == null) {
			selectedTabPage = getPage().getClass();
		}

		List<? extends AbstractLink> pageLinks = getModelObject();

		if (pageLinks != null) {
			ListView<? extends AbstractLink> listView = new ListView<AbstractLink>("page_tabs", pageLinks) {

				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<AbstractLink> item) {
					AbstractLink l = item.getModelObject();
					if (l instanceof BookmarkablePageLink) {
						BookmarkablePageLink<?> bpl = (BookmarkablePageLink<?>) l;
						if (isCurrentPage(bpl)) {
							item.add(new ComponentsFragment("link_comp", "tab_label_fragment", this, new Label("label", bpl.getBody())));
						} else {
							item.add(new ComponentsFragment("link_comp", "tab_link_fragment", this, bpl));
						}
					} else {
						item.add(new ComponentsFragment("link_comp", "tab_link_fragment", this, l));
					}
				}

			};
			listView.setRenderBodyOnly(true);

			ComponentsFragment f = new ComponentsFragment("page_tabs_panel", "page_tabs_fragment", this, listView);
			f.setRenderBodyOnly(true);
			add(f);
		} else {
			add(new EmptyPanel("page_tabs_panel").setVisible(false));
		}
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();

		// ako samo edin link e visible - nqma smisyl tozi komponent da se vijda
		short visibleCount = 0;
		for (AbstractLink l : getModelObject()) {
			l.configure();
			visibleCount += l.isVisible() ? 1 : 0;
		}

		setVisible(visibleCount > 1);
	}

	private boolean isCurrentPage(BookmarkablePageLink<?> link) {
		return link.getPageClass().equals(selectedTabPage);
	}
}
