package com.jparams.store.index.reducer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jparams.store.index.Element;

/**
 * Wrapper for multiple reducers that are triggered in order.
 *
 * @param <K>      key
 * @param <V>value
 */
class MultiReducer<K, V> implements Reducer<K, V>
{
    private final List<Reducer<K, V>> reducers;

    MultiReducer(final List<Reducer<K, V>> reducers)
    {
        this.reducers = new ArrayList<>(reducers);
    }

    @Override
    public void reduce(final K key, final List<Element<V>> elements)
    {
        for (final Reducer<K, V> reducer : reducers)
        {
            reducer.reduce(key, elements);
        }
    }

    @Override
    public MultiReducer<K, V> andThen(final Reducer<K, V> reducer)
    {
        final List<Reducer<K, V>> updatedReducers = new ArrayList<>(reducers);
        updatedReducers.add(reducer);
        return new MultiReducer<>(updatedReducers);
    }

    public List<Reducer<K, V>> getReducers()
    {
        return Collections.unmodifiableList(reducers);
    }
}
