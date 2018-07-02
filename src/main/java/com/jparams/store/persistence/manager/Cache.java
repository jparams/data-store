package com.jparams.store.persistence.manager;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

class Cache<V>
{
    private final long maxSize;
    private final Map<Object, V> cacheMap;

    Cache(final long maxSize)
    {
        this.maxSize = maxSize;
        this.cacheMap = new LinkedHashMap<>();
    }

    V get(final Object identity)
    {
        if (!isCachingEnabled())
        {
            return null;
        }

        // remove from cache and re-insert on latest access
        final V cachedValue = cacheMap.remove(identity);

        if (cachedValue == null)
        {
            return null;
        }

        cacheMap.put(identity, cachedValue);
        return cachedValue;
    }

    void add(final Object identity, final V value)
    {
        if (!isCachingEnabled())
        {
            return;
        }

        cacheMap.put(identity, value);

        if (cacheMap.size() > maxSize)
        {
            final Iterator<Entry<Object, V>> it = cacheMap.entrySet().iterator();

            if (it.hasNext())
            {
                it.next();
                it.remove();
            }
        }
    }

    void remove(final Object identity)
    {
        if (!isCachingEnabled())
        {
            return;
        }

        cacheMap.remove(identity);
    }

    private boolean isCachingEnabled()
    {
        return maxSize > 0;
    }
}
