package com.jparams.store.memory;

import java.util.Collection;

import com.jparams.store.AbstractStore;
import com.jparams.store.Store;
import com.jparams.store.index.IndexManager;
import com.jparams.store.index.ReferenceIndexManager;
import com.jparams.store.reference.ReferenceManager;

/**
 * In memory implementation of a {@link Store}
 *
 * @param <V> type of item referenced
 */
public class MemoryStore<V> extends AbstractStore<V>
{
    private MemoryStore(final ReferenceManager<V> referenceManager, final IndexManager<V> indexManager)
    {
        super(indexManager, referenceManager);
    }

    public MemoryStore()
    {
        this(new MemoryReferenceManager<>(), new ReferenceIndexManager<>());
    }

    public MemoryStore(final Collection<V> items)
    {
        this();
        addAll(items);
    }

    @SafeVarargs
    public MemoryStore(final V... items)
    {
        this();
        addAll(items);
    }

    @Override
    protected Store<V> createCopy(final ReferenceManager<V> referenceManager, final IndexManager<V> indexManager)
    {
        return new MemoryStore<>(referenceManager.copy(), indexManager.copy());
    }

    @Override
    public String toString()
    {
        return getReferenceManager().getReferences().toString();
    }

    public static <V> MemoryStoreBuilder<V> newStore()
    {
        return new MemoryStoreBuilder<>();
    }
}
