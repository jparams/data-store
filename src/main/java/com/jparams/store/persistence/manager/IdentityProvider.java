package com.jparams.store.persistence.manager;

import java.util.function.Function;


/**
 * Provides a unique identity for a persisted value in a store
 *
 * @param <V> value type
 */
class IdentityProvider<V>
{
    private final Class<V> valueType;
    private final Function<V, Object> identityFn;

    IdentityProvider(final Class<V> valueType, final Function<V, Object> identityFn)
    {
        this.valueType = valueType;
        this.identityFn = identityFn;
    }

    Object getIdentity(final Object obj)
    {
        if (obj == null || !valueType.isAssignableFrom(obj.getClass()))
        {
            return null;
        }

        @SuppressWarnings("unchecked") final Object identity = identityFn.apply((V) obj);
        return identity;
    }
}
