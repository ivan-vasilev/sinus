package bg.bas.iinf.sinus.wicket.common;

/**
 * Tip na notifikaciq
 * @author hok
 *
 */
public enum NotificationType {
	INFO {
		@Override
		public String toString() {
			return "info";
		}
	},
	ERROR {
		@Override
		public String toString() {
			return "error";
		}
	}
}
