package bg.bas.iinf.sinus.wicket.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * vryshta formatirana data
 *
 * @author hok
 *
 */
public class DateLDM extends LoadableDetachableModel<String> {

	private static final long serialVersionUID = 4518058599432313061L;


	private Date date;
	private IModel<Date> dateModel;

	public DateLDM(Date date) {
		super();
		this.date = date;
	}

	@Override
	protected String load() {
		Date selectedDate = getDate();
		if (selectedDate != null) {
			return new SimpleDateFormat("dd.MM.yy").format(selectedDate);
		}

		return "";
	}

	protected Date getDate() {
		if (date != null) {
			return date;
		} else if (dateModel != null) {
			return dateModel.getObject();
		}

		return null;
	}
}
