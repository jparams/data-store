package com.jparams.store.persistence.manager;

import java.util.function.Function;

public abstract class AbstractPersistenceManager<V> implements PersistenceManager<V>
{
    private final Cache<V> cache;
    private final IdentityProvider<V> identityProvider;

    public AbstractPersistenceManager(final Class<V> valueType, final Function<V, Object> identityFn, final long maxCacheSize)
    {
        this.cache = new Cache<>(maxCacheSize);
        this.identityProvider = new IdentityProvider<>(valueType, identityFn);
    }

    @Override
    public void remove(final Object identity)
    {
        cache.remove(identity);
        removeByIdentity(identity);
    }

    @Override
    public V get(final Object identity)
    {
        final V cachedValue = cache.get(identity);

        if (cachedValue != null)
        {
            return cachedValue;
        }

        final V persistedValue = getByIdentity(identity);

        if (persistedValue == null)
        {
            return null;
        }

        cache.add(identity, persistedValue);
        return persistedValue;
    }

    @Override
    public Object persist(final V item)
    {
        final Object identify = identify(item);

        if (identify == null)
        {
            return null;
        }

        // remove previously persisted item
        remove(identify);

        // persist and cache
        persist(identify, item);
        cache.add(identify, item);
        return identify;
    }

    @Override
    public Object identify(final Object value)
    {
        return identityProvider.getIdentity(value);
    }

    protected abstract void persist(Object identify, V item);

    protected abstract void removeByIdentity(Object identity);

    protected abstract V getByIdentity(Object identity);
}
