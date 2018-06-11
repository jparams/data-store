package com.jparams.store.index.reducer;

import java.util.List;
import java.util.function.Function;

import com.jparams.store.index.Element;

/**
 * Reduces all elements for a key retaining the max value
 *
 * @param <K> key type
 * @param <V> value type
 */
public class MaxReducer<K, V, C extends Comparable<C>> implements Reducer<K, V>
{
    private final Function<V, C> valueProvider;
    private final boolean nullGreater;

    public MaxReducer(final Function<V, C> valueProvider, final boolean nullGreater)
    {
        this.valueProvider = valueProvider;
        this.nullGreater = nullGreater;
    }

    @Override
    public void reduce(final K key, final List<Element<V>> elements)
    {
        elements.stream().reduce(this::reduce);
    }

    private Element<V> reduce(final Element<V> element1, final Element<V> element2)
    {
        final C comparable1 = valueProvider.apply(element1.get());
        final C comparable2 = valueProvider.apply(element2.get());

        if (compare(comparable1, comparable2) > 0)
        {
            element2.remove();
            return element1;
        }

        element1.remove();
        return element2;
    }

    private int compare(final C value1, final C value2)
    {
        if (value1 == value2)
        {
            return 0;
        }

        if (value1 == null)
        {
            return nullGreater ? 1 : -1;
        }

        if (value2 == null)
        {
            return nullGreater ? -1 : 1;
        }

        return value1.compareTo(value2);
    }
}
