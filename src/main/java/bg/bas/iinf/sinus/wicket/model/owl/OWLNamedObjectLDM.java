package bg.bas.iinf.sinus.wicket.model.owl;

import java.net.URI;

import org.apache.wicket.model.LoadableDetachableModel;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLObject;

import bg.bas.iinf.sinus.cache.CacheFactory;
import bg.bas.iinf.sinus.cache.CacheWrapper;

/**
 * LDM za OWLNamedObject
 * @author hok
 *
 */
public class OWLNamedObjectLDM<T extends OWLNamedObject> extends LoadableDetachableModel<T> {

    private static final long serialVersionUID = 5013539617348090497L;

    private URI uri;

	public OWLNamedObjectLDM(T object) {
	    super(object);
	    if (object != null) {
		    this.uri = object.getIRI().toURI();
			CacheWrapper<URI, OWLObject> cache = CacheFactory.getCache();
			cache.put(uri, object);
	    }
    }

	public OWLNamedObjectLDM(URI uri) {
	    super();
	    this.uri = uri;
    }

	public OWLNamedObjectLDM() {
	    super();
    }

	@Override
	public void setObject(T object) {
		super.setObject(object);
		CacheWrapper<URI, OWLObject> cache = CacheFactory.getCache();
		cache.put(uri, object);
		this.uri = object.getIRI().toURI();
	}

	@SuppressWarnings("unchecked")
    @Override
    protected T load() {
		if (uri == null) {
			return null;
		}

		CacheWrapper<URI, OWLObject> cache = CacheFactory.getCache();

		return (T) cache.get(uri);
    }

	public URI getUri() {
    	return uri;
    }

	public void setUri(URI uri) {
    	this.uri = uri;
    }
}
