package com.jparams.store;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

import com.jparams.store.comparison.ComparisonPolicy;
import com.jparams.store.index.Index;
import com.jparams.store.index.IndexException;

/**
 * Implementation of a store that cannot be modified
 *
 * @param <V> value type
 */
public class UnmodifiableStore<V> implements Store<V>
{
    private final Store<V> store;

    public UnmodifiableStore(final Store<V> store)
    {
        this.store = store;
    }

    @Override
    public <K> Index<V> multiIndex(final String indexName, final KeyProvider<Collection<K>, V> keyProvider) throws IndexException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K> Index<V> multiIndex(final KeyProvider<Collection<K>, V> keyProvider, final ComparisonPolicy<K> comparisonPolicy) throws IndexException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K> Index<V> multiIndex(final KeyProvider<Collection<K>, V> keyProvider) throws IndexException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K> Index<V> index(final String indexName, final KeyProvider<K, V> keyProvider, final ComparisonPolicy<K> comparisonPolicy) throws IndexException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K> Index<V> index(final String indexName, final KeyProvider<K, V> keyProvider) throws IndexException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K> Index<V> index(final KeyProvider<K, V> keyProvider, final ComparisonPolicy<K> comparisonPolicy) throws IndexException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <K> Index<V> index(final KeyProvider<K, V> keyProvider) throws IndexException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAllIndexes()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Index<V>> findIndex(final String indexName)
    {
        return store.findIndex(indexName);
    }

    @Override
    public boolean addAll(final V[] items) throws IndexException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Store<V> synchronizedStore()
    {
        return store.synchronizedStore().unmodifiableStore();
    }

    @Override
    public <K> Index<V> multiIndex(final String indexName, final KeyProvider<Collection<K>, V> keyProvider, final ComparisonPolicy<K> comparisonPolicy) throws IndexException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Index<V> getIndex(final String indexName)
    {
        return store.getIndex(indexName);
    }

    @Override
    public Collection<Index<V>> getIndexes()
    {
        return store.getIndexes();
    }

    @Override
    public boolean removeIndex(final Index<V> index)
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
    public void reindex(final Collection<V> items)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reindex(final V item)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Store<V> unmodifiableStore()
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
    public Iterator<V> iterator()
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
    public boolean add(final V item)
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
    public boolean addAll(final Collection<? extends V> collection)
    {
        throw new UnsupportedOperationException();
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
    public Store<V> copy()
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

    @Override
    public String toString()
    {
        return store.toString();
    }
}