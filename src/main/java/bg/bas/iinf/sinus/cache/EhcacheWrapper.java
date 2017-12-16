package bg.bas.iinf.sinus.cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * konkretna implementaciq za Ehcache
 *
 * @author hok
 *
 * @param <K>
 * @param <V>
 */
public class EhcacheWrapper<K, V> implements CacheWrapper<K, V> {
	private final String cacheName;
	private final CacheManager cacheManager;

	public EhcacheWrapper(final String cacheName, final CacheManager cacheManager) {
		this.cacheName = cacheName;
		this.cacheManager = cacheManager;
	}

	@Override
	public void put(final K key, final V value) {
		getCache().put(new Element(key, value));
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(final K key) {
		Element element = getCache().get(key);
		if (element != null) {
			return (V) element.getObjectValue();
		}
		return null;
	}

	public Ehcache getCache() {
		return cacheManager.getEhcache(cacheName);
	}

	@Override
    public CacheManager getCacheManager() {
    	return cacheManager;
    }

	@Override
    public void remove(K key) {
		getCache().remove(key);
    }
}
