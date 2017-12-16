package bg.bas.iinf.sinus.wicket.model;

import java.io.Serializable;

import bg.bas.iinf.sinus.hibernate.entity.Users;

/**
 * LDM za potrebitel
 * @author hok
 *
 */
public class UserLDM extends AbstractEntityModel<Users> {

    private static final long serialVersionUID = 344483096634421281L;

	public UserLDM(Integer id) {
	    super(id);
    }

	public UserLDM(Users object) {
	    super(object);
    }

	@Override
    protected Class<Users> getEntityClass() {
	    return Users.class;
    }

	@Override
    protected Serializable getId(Users object) {
	    return object.getUsersId();
    }
}
