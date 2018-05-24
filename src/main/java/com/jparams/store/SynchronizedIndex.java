package com.jparams.store;

import java.util.List;
import java.util.Optional;

class SynchronizedIndex<T> implements Index<T>
{
    private final Index<T> index;
    private final Object mutex;

    SynchronizedIndex(final Index<T> index, final Object mutex)
    {
        this.index = index;
        this.mutex = mutex;
    }

    @Override
    public Optional<T> findFirst(final Object key)
    {
        synchronized (mutex)
        {
            return index.findFirst(key);
        }
    }

    @Override
    public List<T> get(final Object key)
    {
        synchronized (mutex)
        {
            return index.get(key);
        }
    }

    @Override
    public String getName()
    {
        return index.getName();
    }
}
