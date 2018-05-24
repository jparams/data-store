package com.jparams.store.memory;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import com.jparams.store.AbstractStore;
import com.jparams.store.KeyProvider;
import com.jparams.store.Store;
import com.jparams.store.comparison.Comparison;
import com.jparams.store.identity.HashCodeIdentityProvider;
import com.jparams.store.index.AbstractIndex;
import com.jparams.store.index.ReferenceIndex;
import com.jparams.store.reference.DefaultReferenceManager;
import com.jparams.store.reference.ReferenceManager;

/**
 * In memory implementation of a {@link Store}
 *
 * @param <V> type of item referenced
 */
public class MemoryStore<V> extends AbstractStore<V>
{
    public MemoryStore()
    {
        super(new DefaultReferenceManager<>(new HashCodeIdentityProvider(), new MemoryReferenceFactory<>()));
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

    private MemoryStore(final ReferenceManager<V> referenceManager, final Set<AbstractIndex<?, V>> indexes)
    {
        super(referenceManager, indexes);
    }

    @Override
    protected Store<V> createCopy(final ReferenceManager<V> referenceManager, final Collection<AbstractIndex<?, V>> indexes)
    {
        final ReferenceManager<V> copyOfReferenceManager = referenceManager.copy();
        final Set<AbstractIndex<?, V>> copyOfIndexes = indexes.stream().map(AbstractIndex::copy).collect(Collectors.toSet());
        return new MemoryStore<>(copyOfReferenceManager, copyOfIndexes);
    }

    @Override
    protected <K> AbstractIndex<K, V> createIndex(final String indexName, final KeyProvider<K, V> keyProvider, final Comparison<K> comparison)
    {
        return new ReferenceIndex<>(indexName, keyProvider, comparison);
    }
}
