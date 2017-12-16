package bg.bas.iinf.sinus.wicket.model;

import java.io.Serializable;

import bg.bas.iinf.sinus.hibernate.entity.Ontologies;

/**
 * LDM za ontologiq
 * @author hok
 *
 */
public class OntologyLDM extends AbstractEntityModel<Ontologies> {

    private static final long serialVersionUID = 7743382720660168209L;

	public OntologyLDM() {
	    super();
    }

	public OntologyLDM(Ontologies object) {
	    super(object);
    }

	public OntologyLDM(Integer id) {
	    super(id);
    }

	@Override
    protected Class<Ontologies> getEntityClass() {
	    return Ontologies.class;
    }

	@Override
    protected Serializable getId(Ontologies object) {
	    return object.getOntologiesId();
    }
}
