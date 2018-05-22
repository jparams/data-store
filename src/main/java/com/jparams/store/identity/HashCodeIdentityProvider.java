package com.jparams.store.identity;

/**
 * Provides object identity using {@link System#identityHashCode(Object)}
 */
public class HashCodeIdentityProvider implements IdentityProvider
{
    @Override
    public Object getIdentity(final Object obj)
    {
        if (obj == null)
        {
            return null;
        }

        return System.identityHashCode(obj);
    }
}
