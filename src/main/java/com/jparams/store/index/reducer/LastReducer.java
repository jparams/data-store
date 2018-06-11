package com.jparams.store.index.reducer;

import java.util.Collection;

import com.jparams.store.index.Element;

/**
 * Reduces all elements for a key retaining the last value
 *
 * @param <K> key type
 * @param <V> value type
 */
public class LastReducer<K, V> implements Reducer<K, V>
{
    @Override
    public void reduce(final K key, final Collection<Element<V>> elements)
    {
        elements.stream().reduce(this::reduce);
    }

    private Element<V> reduce(final Element<V> element1, final Element<V> element2)
    {
        element1.remove();
        return element2;
    }
}
