package com.jparams.store;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

class SynchronizedStore<T> implements Store<T>
{
    private final Store<T> store;
    private final Object mutex;

    SynchronizedStore(final Store<T> store)
    {
        this.store = store;
        this.mutex = this;
    }

    @Override
    public <K> Index<T> addIndex(final String indexName, final Transformer<T, K> valueToKeysTransformer)
    {
        synchronized (mutex)
        {
            return new SynchronizedIndex<>(store.addIndex(indexName, valueToKeysTransformer), mutex);
        }
    }

    @Override
    public <K> Index<T> addIndex(final Transformer<T, K> valueToKeysTransformer)
    {
        synchronized (mutex)
        {
            return new SynchronizedIndex<>(store.addIndex(valueToKeysTransformer), mutex);
        }
    }

    @Override
    public Index<T> getIndex(final String indexName)
    {
        synchronized (mutex)
        {
            return store.getIndex(indexName);
        }
    }

    @Override
    public boolean removeIndex(final Index<T> index)
    {
        synchronized (mutex)
        {
            return store.removeIndex(index);
        }
    }

    @Override
    public boolean removeIndex(final String indexName)
    {
        synchronized (mutex)
        {
            return store.removeIndex(indexName);
        }
    }

    @Override
    public void reindex()
    {
        synchronized (mutex)
        {
            store.reindex();
        }
    }

    @Override
    public void reindex(final T item)
    {
        synchronized (mutex)
        {
            store.reindex(item);
        }
    }

    @Override
    public Store<T> copy()
    {
        synchronized (mutex)
        {
            return store.copy();
        }
    }

    @Override
    public int size()
    {
        synchronized (mutex)
        {
            return store.size();
        }
    }

    @Override
    public boolean isEmpty()
    {
        synchronized (mutex)
        {
            return store.isEmpty();
        }
    }

    @Override
    public boolean contains(final Object obj)
    {
        synchronized (mutex)
        {
            return store.contains(obj);
        }
    }

    @Override
    public Iterator<T> iterator()
    {
        return store.iterator();
    }

    @Override
    public Object[] toArray()
    {
        synchronized (mutex)
        {
            return store.toArray();
        }
    }

    @Override
    public <T1> T1[] toArray(final T1[] array)
    {
        synchronized (mutex)
        {
            return store.toArray(array);
        }
    }

    @Override
    public boolean add(final T item)
    {
        synchronized (mutex)
        {
            return store.add(item);
        }
    }

    @Override
    public boolean remove(final Object obj)
    {
        synchronized (mutex)
        {
            return store.remove(obj);
        }
    }

    @Override
    public boolean containsAll(final Collection<?> collection)
    {
        synchronized (mutex)
        {
            return store.containsAll(collection);
        }
    }

    @Override
    public boolean addAll(final Collection<? extends T> collection)
    {
        synchronized (mutex)
        {
            return store.addAll(collection);
        }
    }

    @Override
    public boolean removeAll(final Collection<?> collection)
    {
        synchronized (mutex)
        {
            return store.removeAll(collection);
        }
    }

    @Override
    public boolean removeIf(final Predicate<? super T> filter)
    {
        synchronized (mutex)
        {
            return store.removeIf(filter);
        }
    }

    @Override
    public boolean retainAll(final Collection<?> collection)
    {
        synchronized (mutex)
        {
            return store.retainAll(collection);
        }
    }

    @Override
    public void clear()
    {
        synchronized (mutex)
        {
            store.clear();
        }
    }
}
