package com.jparams.store.persistence;

import com.jparams.store.AbstractStore;
import com.jparams.store.Store;
import com.jparams.store.index.IndexManager;
import com.jparams.store.index.ReferenceIndexManager;
import com.jparams.store.persistence.manager.PersistenceManager;
import com.jparams.store.reference.ReferenceManager;

public class PersistenceStore<V> extends AbstractStore<V>
{
    private PersistenceStore(final IndexManager<V> indexManager, final ReferenceManager<V> referenceManager)
    {
        super(indexManager, referenceManager);
    }

    public PersistenceStore(final PersistenceManager<V> persistenceManager)
    {
        this(new ReferenceIndexManager<>(), new PersistenceReferenceManager<>(persistenceManager));
    }

    @Override
    protected Store<V> createCopy(final ReferenceManager<V> referenceManager, final IndexManager<V> indexManager)
    {
        return new PersistenceStore<>(indexManager, referenceManager);
    }

    @Override
    public String toString()
    {
        return getReferenceManager().getReferences().toString();
    }

    public static <V> PersistenceStoreBuilder<V> newStore(final PersistenceManager<V> persistenceManager)
    {
        return new PersistenceStoreBuilder<>(persistenceManager);
    }
}
