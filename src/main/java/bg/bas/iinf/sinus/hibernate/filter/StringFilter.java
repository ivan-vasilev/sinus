package bg.bas.iinf.sinus.hibernate.filter;

import java.io.Serializable;

/**
 * universalen filtyr za syvpadenie na string
 * @author hok
 *
 */
public class StringFilter implements Serializable {

	private static final long serialVersionUID = -6223940772236939175L;

	public enum STRING_MATCH { IS_EXACTLY, STARTS_WITH, ANYWHERE, ENDS_WITH };

	private STRING_MATCH stringMatchType;
	private String stringLike;

	public StringFilter() {
		super();
	}

	public StringFilter(STRING_MATCH stringMatchType, String stringLike) {
		super();
		this.stringMatchType = stringMatchType;
		this.stringLike = stringLike;
	}

	public STRING_MATCH getStringMatchType() {
		return stringMatchType;
	}

	public void setStringMatchType(STRING_MATCH stringMatchType) {
		this.stringMatchType = stringMatchType;
	}

	public String getStringLike() {
		return stringLike;
	}

	public void setStringLike(String stringLike) {
		this.stringLike = stringLike;
	}

	public String getClause() {
		if ((stringLike == null) || stringLike.equals("")) {
			return "%";
		}

		if (stringMatchType != null) {
			if (stringMatchType.equals(STRING_MATCH.IS_EXACTLY)) {
				return stringLike;
			} else if (stringMatchType.equals(STRING_MATCH.STARTS_WITH)) {
				return stringLike + "%";
			} else if (stringMatchType.equals(STRING_MATCH.ENDS_WITH)) {
				return "%" + stringLike;
			} else {
				return "%" + stringLike + "%";
			}
		}

		return "%" + stringLike + "%";
	}
}
