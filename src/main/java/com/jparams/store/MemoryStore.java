package com.jparams.store;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * In memory implementation of a {@link Store}
 *
 * @param <T>
 */
public class MemoryStore<T> extends AbstractStore<T>
{
    public MemoryStore()
    {
        super(new MemoryReferenceManager<>());
    }

    private MemoryStore(final ReferenceManager<T> referenceManager, final Set<AbstractIndex<T>> indexes)
    {
        super(referenceManager, indexes);
    }

    @Override
    protected Store<T> createCopy(final ReferenceManager<T> referenceManager, final Collection<AbstractIndex<T>> indexes)
    {
        final ReferenceManager<T> copyOfReferenceManager = referenceManager.copy();
        final Set<AbstractIndex<T>> copyOfIndexes = indexes.stream().map(index -> index.copy(true)).collect(Collectors.toSet());
        return new MemoryStore<>(copyOfReferenceManager, copyOfIndexes);
    }

    @Override
    protected AbstractIndex<T> createIndex(final String indexName, final Transformer<T, ?> transformer)
    {
        return new ReferenceIndex<>(indexName, transformer);
    }
}
