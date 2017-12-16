package bg.bas.iinf.sinus.cache;

import java.net.URI;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

/**
 * factory za CacheWrapper-i
 * @author hok
 *
 */
public class CacheFactory {

	private static String CACHE_NAME = "cache";
	private static String GENERAL_CACHE_NAME = "general_cache";
	private static String REASONER_CACHE_NAME = "reasoner";
	private static String ONTOLOGY_CACHE_NAME = "ontology";

	/**
	 * cache
	 * @return
	 */
	public static CacheWrapper<URI, OWLObject> getCache() {
		 CacheManager manager = CacheManager.create();
		 Cache c = manager.getCache(CACHE_NAME);
		 if (c == null) {
			 CacheConfiguration cc = new CacheConfiguration();
			 cc.setName(CACHE_NAME);
			 c = new Cache(cc);
			 manager.addCache(c);
		 }

		return new EhcacheWrapper<URI, OWLObject>(CACHE_NAME, manager);
	}

	/**
	 * cache
	 * @return
	 */
	public static CacheWrapper<String, OWLOntology> getOntologyCache() {
		CacheManager manager = CacheManager.create();
		Cache c = manager.getCache(ONTOLOGY_CACHE_NAME);
		if (c == null) {
			CacheConfiguration cc = new CacheConfiguration();
			cc.setName(ONTOLOGY_CACHE_NAME);
			c = new Cache(cc);
			manager.addCache(c);
		}

		return new EhcacheWrapper<String, OWLOntology>(ONTOLOGY_CACHE_NAME, manager);
	}

	/**
	 * cache
	 * @return
	 */
	public static CacheWrapper<String, Object> getGeneralCache() {
		 CacheManager manager = CacheManager.create();
		 Cache c = manager.getCache(GENERAL_CACHE_NAME);
		 if (c == null) {
			 CacheConfiguration cc = new CacheConfiguration();
			 cc.setName(GENERAL_CACHE_NAME);
			 c = new Cache(cc);
			 manager.addCache(c);
		 }

		return new EhcacheWrapper<String, Object>(GENERAL_CACHE_NAME, manager);
	}

	/**
	 * cache za reasoner-i
	 * @return
	 */
	public static CacheWrapper<String, OWLReasoner> getReasonerCache() {
		 CacheManager manager = CacheManager.create();
		 Cache c = manager.getCache(REASONER_CACHE_NAME);
		 if (c == null) {
			 CacheConfiguration cc = new CacheConfiguration();
			 cc.setName(REASONER_CACHE_NAME);
			 c = new Cache(cc);
			 manager.addCache(c);
		 }

		return new EhcacheWrapper<String, OWLReasoner>(REASONER_CACHE_NAME, manager);
	}
}
