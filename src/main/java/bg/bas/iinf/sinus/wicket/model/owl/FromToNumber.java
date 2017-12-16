package bg.bas.iinf.sinus.wicket.model.owl;

import java.io.Serializable;

/**
 * ot-do
 * @author hok
 *
 * @param <T>
 */
public class FromToNumber<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 893334145170665300L;

    private T from;
	private T to;

	public FromToNumber() {
		super();
	}

	public T getFrom() {
		return from;
	}

	public void setFrom(T from) {
		this.from = from;
	}

	public T getTo() {
		return to;
	}

	public void setTo(T to) {
		this.to = to;
	}
}
