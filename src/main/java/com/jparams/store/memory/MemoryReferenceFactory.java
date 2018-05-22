package com.jparams.store.memory;

import com.jparams.store.reference.Reference;
import com.jparams.store.reference.ReferenceFactory;

/**
 * Factory for creating in memory references
 *
 * @param <T>
 */
public class MemoryReferenceFactory<T> implements ReferenceFactory<T>
{
    @Override
    public Reference<T> createReference(final T obj)
    {
        return new MemoryReference<>(obj);
    }
}
