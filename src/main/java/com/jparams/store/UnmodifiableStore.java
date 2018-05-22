package com.jparams.store;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

import com.jparams.store.comparison.Comparison;
import com.jparams.store.index.Index;

/**
 * Implementation of a store that cannot be modified
 *
 * @param <T>
 */
class UnmodifiableStore<T> implements Store<T>
{
    private final Store<T> store;

    UnmodifiableStore(final Store<T> store)
    {
        this.store = store;
    }

    @Override
    public <K> Index<T> index(final String indexName, final KeyProvider<K, T> keyProvider, final Comparison<K> comparison) throws IndexException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Index<T> getIndex(final String indexName)
    {
        return store.getIndex(indexName);
    }

    @Override
    public Collection<Index<T>> getIndexes()
    {
        return store.getIndexes();
    }

    @Override
    public boolean removeIndex(final Index<T> index)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIndex(final String indexName)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reindex()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reindex(final Collection<T> items)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reindex(final T item)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Store<T> unmodifiableStore()
    {
        return this;
    }

    @Override
    public int size()
    {
        return store.size();
    }

    @Override
    public boolean isEmpty()
    {
        return store.isEmpty();
    }

    @Override
    public boolean contains(final Object obj)
    {
        return store.contains(obj);
    }

    @Override
    public Iterator<T> iterator()
    {
        return new UnmodifiableIterator<>(store.iterator());
    }

    @Override
    public Object[] toArray()
    {
        return store.toArray();
    }

    @Override
    public <T1> T1[] toArray(final T1[] array)
    {
        return store.toArray(array);
    }

    @Override
    public boolean add(final T item)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(final Object o)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(final Collection<?> collection)
    {
        return store.containsAll(collection);
    }

    @Override
    public boolean addAll(final Collection<? extends T> collection)
    {
        return store.addAll(collection);
    }

    @Override
    public boolean removeAll(final Collection<?> collection)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(final Collection<?> collection)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Store<T> copy()
    {
        return store.copy();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }

        final UnmodifiableStore<?> that = (UnmodifiableStore<?>) obj;
        return Objects.equals(store, that.store);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(store);
    }
}