package com.jparams.store.index;

/**
 * Map a value to a single indexed key
 *
 * @param <K> key type
 * @param <V> value type
 */
@FunctionalInterface
public interface KeyMapper<K, V>
{
    /**
     * Index value to key or return null
     *
     * @param value value to transform into a key
     * @return key
     */
    K map(V value);
}
