package bg.bas.iinf.sinus.cache;

import net.sf.ehcache.CacheManager;

/**
 * univeralno za razlichni cache-ove
 *
 * @author hok
 *
 * @param <K>
 * @param <V>
 */
public interface CacheWrapper<K, V> {
	void put(K key, V value);

	void remove(K key);

	V get(K key);

	public CacheManager getCacheManager();
}
