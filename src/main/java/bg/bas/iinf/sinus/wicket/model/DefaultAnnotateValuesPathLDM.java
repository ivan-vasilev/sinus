package bg.bas.iinf.sinus.wicket.model;

import java.io.Serializable;

import bg.bas.iinf.sinus.hibernate.entity.DefaultAnnotateValuesPath;

/**
 * LDM za pyt za anotacii
 * @author hok
 *
 */
public class DefaultAnnotateValuesPathLDM extends AbstractEntityModel<DefaultAnnotateValuesPath> {

    private static final long serialVersionUID = 7743382720660168209L;

	public DefaultAnnotateValuesPathLDM() {
	    super();
    }

	public DefaultAnnotateValuesPathLDM(DefaultAnnotateValuesPath object) {
	    super(object);
    }

	public DefaultAnnotateValuesPathLDM(Integer id) {
	    super(id);
    }

	@Override
    protected Class<DefaultAnnotateValuesPath> getEntityClass() {
	    return DefaultAnnotateValuesPath.class;
    }

	@Override
    protected Serializable getId(DefaultAnnotateValuesPath object) {
	    return object.getDefaultAnnotateValuesPathsId();
    }
}
