package com.jparams.store.index;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class SynchronizedIndex<T> implements Index<T>
{
    private final Index<T> index;
    private final Object mutex;

    public SynchronizedIndex(final Index<T> index, final Object mutex)
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

    @Override
    public Set<Object> getKeys()
    {
        synchronized (mutex)
        {
            return index.getKeys();
        }
    }

    public Index<T> getIndex()
    {
        return index;
    }

    @Override
    public boolean equals(final Object other)
    {
        if (this == other)
        {
            return true;
        }

        if (other == null || getClass() != other.getClass())
        {
            return false;
        }

        final SynchronizedIndex<?> that = (SynchronizedIndex<?>) other;
        return Objects.equals(index, that.index)
            && Objects.equals(mutex, that.mutex);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(index, mutex);
    }
}
