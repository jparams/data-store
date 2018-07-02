package com.jparams.store.persistence;

import java.util.Collection;

import com.jparams.store.index.IndexDefinition;
import com.jparams.store.index.KeyMapper;
import com.jparams.store.persistence.manager.PersistenceManager;

/**
 * Builder for a memory store
 *
 * @param <V> value type
 */
public final class PersistenceStoreBuilder<V>
{
    private final PersistenceStore<V> store;

    PersistenceStoreBuilder(final PersistenceManager<V> persistenceManager)
    {
        store = new PersistenceStore<>(persistenceManager);
    }

    public final PersistenceStoreBuilder<V> withValue(final V value)
    {
        store.add(value);
        return this;
    }

    public final PersistenceStoreBuilder<V> withValues(final Collection<V> values)
    {
        store.addAll(values);
        return this;
    }

    @SafeVarargs
    public final PersistenceStoreBuilder<V> withValues(final V... values)
    {
        store.addAll(values);
        return this;
    }

    public final <K> PersistenceStoreBuilder<V> withIndex(final String indexName, final KeyMapper<K, V> keyMapper)
    {
        store.index(indexName, keyMapper);
        return this;
    }

    public final <K> PersistenceStoreBuilder<V> withIndex(final String indexName, final IndexDefinition<K, V> indexDefinition)
    {
        store.index(indexName, indexDefinition);
        return this;
    }

    public final PersistenceStore<V> build()
    {
        return store;
    }
}
