package com.jparams.store.identity;

/**
 * Provides an identity for an object
 */
public interface IdentityProvider
{
    /**
     * Get object identity
     *
     * @param obj object
     * @return identity
     */
    Object getIdentity(Object obj);
}
