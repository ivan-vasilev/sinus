package bg.bas.iinf.sinus.wicket.model;

import java.io.Serializable;

import org.apache.wicket.model.LoadableDetachableModel;

import bg.bas.iinf.sinus.hibernate.dao.Home;
import bg.bas.iinf.sinus.hibernate.lifecycle.ScopedEntityManagerFactory;

/**
 * abstrakten model za zarejdane na 1 entity s "id" ot bazata
 * ne moje da se izpolzva za redaktirane
 *
 * @author hok
 *
 * @param <T>
 */
public abstract class AbstractEntityModel<T> extends LoadableDetachableModel<T> {

    private static final long serialVersionUID = 1064419075958294502L;

    protected Serializable id;

    public AbstractEntityModel() {
	    super();
    }

	public AbstractEntityModel(T object) {
	    super(object);
	    if (object != null) {
	    	this.id = getId(object);
	    }
    }

	public AbstractEntityModel(Serializable id) {
	    super();
	    this.id = id;
    }

    @Override
    protected T load() {
    	if (id != null) {
    		return Home.findById(ScopedEntityManagerFactory.getEntityManager(), id, getEntityClass());
    	}

    	return null;
    }

	@Override
	public void setObject(T object) {
		super.setObject(object);
		if (object != null) {
			this.id = getId(object);
		} else {
			this.id = null;
		}
	}

	@Override
    public T getObject() {
    	T object = super.getObject();
    	if (object != null && !ScopedEntityManagerFactory.getEntityManager().contains(object)) {
    		object = Home.findById(ScopedEntityManagerFactory.getEntityManager(), getId(object), getEntityClass());
    		setObject(object);
    	}

    	return object;
    }

    protected abstract Class<T> getEntityClass();

	protected abstract Serializable getId(T object);
}
