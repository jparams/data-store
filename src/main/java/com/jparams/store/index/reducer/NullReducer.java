package com.jparams.store.index.reducer;

import java.util.List;
import java.util.function.Function;

import com.jparams.store.index.Element;

/**
 * Reduces values and removes all null values from the index
 *
 * @param <K> key type
 * @param <V> value
 */
public class NullReducer<K, V> implements Reducer<K, V>
{
    private final Function<V, ?> valueProvider;

    public NullReducer(final Function<V, ?> valueProvider)
    {
        this.valueProvider = valueProvider;
    }

    @Override
    public void reduce(final K key, final List<Element<V>> elements)
    {
        for (final Element<V> element : elements)
        {
            if (valueProvider.apply(element.get()) == null)
            {
                element.remove();
            }
        }
    }
}
