package bg.bas.iinf.sinus.repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

public class RepoUtil {

	private static final Log log = LogFactory.getLog(RepoUtil.class);

	private static Repository repository = new HTTPRepository("http://82.103.112.243:8081/openrdf-sesame/repositories/SinusRepositoryWithOWLIM4.2");

	static {
		try {
			repository.initialize();
		} catch (RepositoryException e) {
			log.error(e);
		}
	}

	public static TupleQueryResult executeSPARQLQuery(String query) {
		RepositoryConnection con = null;
		try {
			con = repository.getConnection();
			return con.prepareTupleQuery(QueryLanguage.SPARQL, query).evaluate();
		} catch (Exception e) {
			log.error(e);
		} finally {
			try {
				con.close();
			} catch (RepositoryException e) {
				log.error(e);
			}
		}

		return null;
	}
}
