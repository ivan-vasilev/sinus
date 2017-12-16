package bg.bas.iinf.sinus.wicket.auth;

/**
 * Izbroen tip s potrebitelskite prava
 * @author hok
 *
 */
public enum AuthRoles {

	ADMIN {
		@Override
		public String toString() {
			return ADMIN_CONST;
		}
	},
	LIBRARIAN {
		@Override
		public String toString() {
			return LIBRARIAN_CONST;
		}
	},
	STUDENT {
		@Override
		public String toString() {
			return STUDENT_CONST;
		}
	},
	ANNOTATOR {
		@Override
		public String toString() {
			return ANNOTATOR_CONST;
		}
	};

	public static final String ADMIN_CONST = "ADMIN";
	public static final String LIBRARIAN_CONST = "LIBRARIAN";
	public static final String STUDENT_CONST = "STUDENT";
	public static final String ANNOTATOR_CONST = "ANNOTATOR";
}
