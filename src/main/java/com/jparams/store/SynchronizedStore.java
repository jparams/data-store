package com.jparams.store;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.jparams.store.comparison.ComparisonPolicy;
import com.jparams.store.index.Index;
import com.jparams.store.index.IndexException;
import com.jparams.store.index.SynchronizedIndex;

/**
 * Synchronized (thread-safe) store backed by given store.
 *
 * @param <V> value type
 */
public class SynchronizedStore<V> implements Store<V>
{
    private final Store<V> store;
    private final Object mutex;

    public SynchronizedStore(final Store<V> store)
    {
        this.store = store;
        this.mutex = this;
    }

    @Override
    public <K> Index<V> multiIndex(final String indexName, final KeyProvider<Collection<K>, V> keyProvider, final ComparisonPolicy<K> comparisonPolicy) throws IndexException
    {
        synchronized (mutex)
        {
            return new SynchronizedIndex<>(store.multiIndex(indexName, keyProvider, comparisonPolicy), mutex);
        }
    }

    @Override
    public <K> Index<V> multiIndex(final String indexName, final KeyProvider<Collection<K>, V> keyProvider) throws IndexException
    {
        synchronized (mutex)
        {
            return new SynchronizedIndex<>(store.multiIndex(indexName, keyProvider), mutex);
        }
    }

    @Override
    public <K> Index<V> multiIndex(final KeyProvider<Collection<K>, V> keyProvider, final ComparisonPolicy<K> comparisonPolicy) throws IndexException
    {
        synchronized (mutex)
        {
            return new SynchronizedIndex<>(store.multiIndex(keyProvider, comparisonPolicy), mutex);
        }
    }

    @Override
    public <K> Index<V> multiIndex(final KeyProvider<Collection<K>, V> keyProvider) throws IndexException
    {
        synchronized (mutex)
        {
            return new SynchronizedIndex<>(store.multiIndex(keyProvider), mutex);
        }
    }

    @Override
    public <K> Index<V> index(final String indexName, final KeyProvider<K, V> keyProvider, final ComparisonPolicy<K> comparisonPolicy) throws IndexException
    {
        synchronized (mutex)
        {
            return new SynchronizedIndex<>(store.index(indexName, keyProvider, comparisonPolicy), mutex);
        }
    }

    @Override
    public <K> Index<V> index(final String indexName, final KeyProvider<K, V> keyProvider) throws IndexException
    {
        synchronized (mutex)
        {
            return new SynchronizedIndex<>(store.index(indexName, keyProvider), mutex);
        }
    }

    @Override
    public <K> Index<V> index(final KeyProvider<K, V> keyProvider, final ComparisonPolicy<K> comparisonPolicy) throws IndexException
    {
        synchronized (mutex)
        {
            return new SynchronizedIndex<>(store.index(keyProvider, comparisonPolicy), mutex);
        }
    }

    @Override
    public <K> Index<V> index(final KeyProvider<K, V> keyProvider) throws IndexException
    {
        synchronized (mutex)
        {
            return new SynchronizedIndex<>(store.index(keyProvider), mutex);
        }
    }

    @Override
    public Index<V> getIndex(final String indexName)
    {
        synchronized (mutex)
        {
            final Index<V> index = store.getIndex(indexName);

            if (index == null)
            {
                return null;
            }

            return new SynchronizedIndex<>(index, mutex);
        }
    }

    @Override
    public Collection<Index<V>> getIndexes()
    {
        synchronized (mutex)
        {
            return store.getIndexes().stream().map(index -> new SynchronizedIndex<>(index, mutex)).collect(Collectors.toList());
        }
    }

    @Override
    public void removeAllIndexes()
    {
        synchronized (mutex)
        {
            store.removeAllIndexes();
        }
    }

    @Override
    public Optional<Index<V>> findIndex(final String indexName)
    {
        synchronized (mutex)
        {
            return store.findIndex(indexName).map(index -> new SynchronizedIndex<>(index, mutex));
        }
    }

    @Override
    public boolean removeIndex(final Index<V> index)
    {
        synchronized (mutex)
        {
            if (index instanceof SynchronizedIndex)
            {
                return store.removeIndex(((SynchronizedIndex<V>) index).getIndex());
            }
            else
            {
                return store.removeIndex(index);
            }
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
    public void reindex(final Collection<V> items)
    {
        synchronized (mutex)
        {
            store.reindex(items);
        }
    }

    @Override
    public void reindex(final V item)
    {
        synchronized (mutex)
        {
            store.reindex(item);
        }
    }

    @Override
    public Store<V> copy()
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
    public Iterator<V> iterator()
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
    public boolean add(final V item)
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
    public boolean addAll(final Collection<? extends V> collection)
    {
        synchronized (mutex)
        {
            return store.addAll(collection);
        }
    }

    @Override
    public boolean addAll(final V[] items) throws IndexException
    {
        synchronized (mutex)
        {
            return store.addAll(items);
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
    public boolean removeIf(final Predicate<? super V> filter)
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

    public Store<V> getStore()
    {
        return store;
    }

    @Override
    public Store<V> synchronizedStore()
    {
        return this;
    }

    @Override
    public String toString()
    {
        return store.toString();
    }
}
