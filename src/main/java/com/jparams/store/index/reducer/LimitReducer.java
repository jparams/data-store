package com.jparams.store.index.reducer;

import java.util.List;

import com.jparams.store.index.Element;

/**
 * Reduces elements a key once the configured limit has been reached
 *
 * @param <K> key type
 * @param <V> value type
 */
public class LimitReducer<K, V> implements Reducer<K, V>
{
    private final int limit;
    private final Retain retain;

    public LimitReducer(final int limit, final Retain retain)
    {
        this.limit = limit;
        this.retain = retain;
    }

    @Override
    public void reduce(final K key, final List<Element<V>> elements)
    {
        if (elements.size() <= limit)
        {
            return;
        }

        if (retain == Retain.OLDEST)
        {
            reduceOldest(elements);
        }
        else if (retain == Retain.NEWEST)
        {
            reduceNewest(elements);
        }
    }

    private void reduceNewest(final List<Element<V>> elements)
    {
        for (int i = 0; i < (elements.size() - limit); i++)
        {
            elements.get(i).remove();
        }
    }

    private void reduceOldest(final List<Element<V>> elements)
    {
        for (int i = limit; i < elements.size(); i++)
        {
            elements.get(i).remove();
        }
    }

    public enum Retain
    {
        NEWEST,
        OLDEST
    }
}
