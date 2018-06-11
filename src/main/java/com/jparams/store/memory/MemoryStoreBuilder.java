package com.jparams.store.memory;

import java.util.Collection;

import com.jparams.store.index.IndexDefinition;
import com.jparams.store.index.KeyMapper;

/**
 * Builder for a memory store
 *
 * @param <V> value type
 */
public final class MemoryStoreBuilder<V>
{
    private final MemoryStore<V> store;

    MemoryStoreBuilder()
    {
        store = new MemoryStore<>();
    }

    public final MemoryStoreBuilder<V> withValue(final V value)
    {
        store.add(value);
        return this;
    }

    public final MemoryStoreBuilder<V> withValues(final Collection<V> values)
    {
        store.addAll(values);
        return this;
    }

    @SafeVarargs
    public final MemoryStoreBuilder<V> withValues(final V... values)
    {
        store.addAll(values);
        return this;
    }

    public final <K> MemoryStoreBuilder<V> withIndex(final String indexName, final KeyMapper<K, V> keyMapper)
    {
        store.index(indexName, keyMapper);
        return this;
    }

    public final <K> MemoryStoreBuilder<V> withIndex(final String indexName, final IndexDefinition<K, V> indexDefinition)
    {
        store.index(indexName, indexDefinition);
        return this;
    }

    public final MemoryStore<V> build()
    {
        return store;
    }
}
