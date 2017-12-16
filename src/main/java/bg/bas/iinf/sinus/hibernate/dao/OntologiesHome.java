package bg.bas.iinf.sinus.hibernate.dao;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.util.string.Strings;
import org.hibernate.Session;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyCreationIOException;
import org.semanticweb.owlapi.io.UnparsableOntologyException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.UnloadableImportException;

import bg.bas.iinf.sinus.cache.CacheFactory;
import bg.bas.iinf.sinus.hibernate.entity.Ontologies;

public class OntologiesHome {

	private static final Log log = LogFactory.getLog(OntologiesHome.class);

	@SuppressWarnings("unchecked")
	public static List<Ontologies> getOntologies(EntityManager em, Boolean isConfigured) {
		try {
			if (isConfigured != null) {
				return em.unwrap(Session.class).createQuery("select o from Ontologies o where o.isConfigured = :isConfigured order by o.ontologiesId").setParameter("isConfigured", isConfigured).setCacheable(true).list();
			}

			return em.unwrap(Session.class).createQuery("select o from Ontologies o order by o.ontologiesId").setCacheable(true).list();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public static Ontologies findByID(EntityManager em, String id) {
		try {
			List<?> result = em.unwrap(Session.class).createQuery("select o from Ontologies o where o.id like :id").setParameter("id", id).setCacheable(true).list();
			if (result.size() == 1) {
				return (Ontologies) result.get(0);
			}

			return null;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public static OWLOntology getOntolgoy(EntityManager em, String id) {
		Ontologies ontology = findByID(em, id.toString());
		if (ontology != null) {
			return getOntology(ontology.getUri());
		}

		return null;
	}

	public static OWLOntology getOntology(String uri) {
		OWLOntology ont = CacheFactory.getOntologyCache().get(uri);
		if (ont == null) {
			OWLOntologyManager manager = null;

			try {
				// Get hold of an ontology manager
				manager = OWLManager.createOWLOntologyManager();

				// Let's load an ontology from the web
				URI ontUri = new URI(uri);
				if (isLocalFile(ontUri)) {
					ont = manager.loadOntologyFromOntologyDocument(new File(ontUri));
				} else {
					ont = manager.loadOntologyFromOntologyDocument(IRI.create(ontUri));
				}

				log.debug("Loaded ontology: " + ont);
			} catch (OWLOntologyCreationIOException e) {
				log.debug(e);
				return null;
			} catch (UnparsableOntologyException e) {
				log.debug(e);
				return null;
			} catch (UnloadableImportException e) {
				log.debug(e);
				return null;
			} catch (OWLOntologyCreationException e) {
				log.debug(e);
				return null;
			} catch (URISyntaxException e) {
				log.debug(e);
				return null;
			}

			CacheFactory.getOntologyCache().put(ont.getOntologyID().toString(), ont);
		}

		return ont;
	}

	public static void removeInOneTransaction(EntityManager em, Ontologies ont) throws URISyntaxException {
		try {
			em.getTransaction().begin();

			Home.remove(em, ont);

			URI uri = new URI(ont.getUri());
			if (isLocalFile(uri)) {
				new File(uri).delete();
			}

			em.getTransaction().commit();
		} catch (RuntimeException re) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			log.error("get failed", re);
			throw re;
		} catch (URISyntaxException e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			log.error("get failed", e);
			throw e;
        }
	}

	private static boolean isLocalFile(URI uri) {
		return "file".equalsIgnoreCase(uri.getScheme()) && Strings.isEmpty(uri.getHost());
	}
}
