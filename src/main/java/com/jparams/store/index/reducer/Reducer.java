package com.jparams.store.index.reducer;

import java.util.Collection;

import com.jparams.store.index.Element;

/**
 * Reduce all values matching the same key
 *
 * @param <K> key
 * @param <V> value
 */
@FunctionalInterface
public interface Reducer<K, V>
{
    /**
     * Reduce values matched for the same key
     *
     * @param key      key for values
     * @param elements elements to reduce
     * @return key
     */
    void reduce(K key, Collection<Element<V>> elements);
}
