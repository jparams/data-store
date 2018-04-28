package com.jparams.store;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

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
    public <K> Index<T> addIndex(final String indexName, final Transformer<T, K> valueToKeysTransformer)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K> Index<T> addIndex(final Transformer<T, K> valueToKeysTransformer)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Index<T> getIndex(final String indexName)
    {
        return store.getIndex(indexName);
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
        return null;
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
    public boolean add(final T t)
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
