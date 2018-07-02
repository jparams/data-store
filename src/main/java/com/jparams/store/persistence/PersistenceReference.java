package com.jparams.store.persistence;

import java.lang.ref.WeakReference;

import com.jparams.store.persistence.manager.PersistenceManager;
import com.jparams.store.reference.Reference;

/**
 * Reference to a stored item in memory
 *
 * @param <T> reference type
 */
public class PersistenceReference<T> implements Reference<T>
{
    private final PersistenceManager<T> persistenceManager;
    private final Object identity;
    private final WeakReference<T> weakReference;

    public PersistenceReference(final PersistenceManager<T> persistenceManager, final Object identity, final T weakReference)
    {
        this.persistenceManager = persistenceManager;
        this.identity = identity;
        this.weakReference = new WeakReference<>(weakReference);
    }

    @Override
    public T get()
    {
        final T value = weakReference.get();

        if (value != null)
        {
            return value;
        }

        return persistenceManager.get(identity);
    }

    @Override
    public String toString()
    {
        final T value = weakReference.get();

        if (value == null)
        {
            return "Reference[" + identity + ']';
        }

        return String.valueOf(value);
    }
}
