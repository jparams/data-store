package com.jparams.store;

/**
 * Transform a given value into a key
 *
 * @param <K> key type
 * @param <V> value type
 */
@FunctionalInterface
public interface KeyProvider<K, V>
{
    /**
     * Index value to key or return null
     *
     * @param value value to transform into a key
     * @return key
     */
    K provide(V value);
}
