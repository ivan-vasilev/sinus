package bg.bas.iinf.sinus.hibernate.filter;

import java.io.Serializable;
import java.util.Collection;

/**
 * filtyr za izbrani v "koshnicata" rezultati
 * @author hok
 *
 */
public class SelectedResultsFilter implements Serializable {

    private static final long serialVersionUID = 9136542629269539994L;

    private Integer userId;
    private StringFilter tag;
	private String classIRI;
	private Collection<String> objectIRIs;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getClassIRI() {
		return classIRI;
	}

	public void setClassIRI(String classIRI) {
		this.classIRI = classIRI;
	}

	public Collection<String> getObjectIRIs() {
    	return objectIRIs;
    }

	public void setObjectIRIs(Collection<String> objectIRIs) {
    	this.objectIRIs = objectIRIs;
    }

	public StringFilter getTag() {
    	return tag;
    }

	public void setTag(StringFilter tag) {
    	this.tag = tag;
    }
}
