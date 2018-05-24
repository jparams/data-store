package com.jparams.store.index;

import java.util.List;

/**
 * Key and value entry in an index
 *
 * @param <V> value type
 */
public class Entry<V>
{
    private final Object key;
    private final Index<V> index;

    public Entry(final Object key, final Index<V> index)
    {
        this.key = key;
        this.index = index;
    }

    public Object getKey()
    {
        return key;
    }

    public List<V> getValues()
    {
        return index.get(index);
    }

    public V getFirstValue()
    {
        return index.findFirst(key).orElse(null);
    }
}
