package com.jparams.store;

/**
 * Transform a given value into one or more keys for indexing
 *
 * @param <K> key type
 * @param <V> value type
 */
@FunctionalInterface
public interface KeyProvider<K, V>
{
    /**
     * Index value to one or more keys. To exclude this item from indexing, return {@link Key#none()}.
     *
     * @param value value to transform into a key
     * @return key
     */
    Key<K> provide(V value);
}
