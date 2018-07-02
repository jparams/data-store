package com.jparams.store.index.reducer;

import java.util.List;

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
     */
    void reduce(K key, List<Element<V>> elements);

    /**
     * Chain two reducers together
     *
     * @param reducer reducer
     * @return chained reducer
     */
    default Reducer<K, V> andThen(final Reducer<K, V> reducer)
    {
        return new MultiReducer<>(this, reducer);
    }
}
