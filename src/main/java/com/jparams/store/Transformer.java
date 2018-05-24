package com.jparams.store;

/**
 * Transform a given value into one or more keys for indexing
 *
 * @param <V> value type
 * @param <K> key type
 */
@FunctionalInterface
public interface Transformer<V, K>
{
    /**
     * Transform value to one or more keys. To exclude this item from indexing, return {@link Keys#none()}.
     *
     * @param value value to transform into a key
     * @return key
     */
    Keys<K> transform(V value);
}
