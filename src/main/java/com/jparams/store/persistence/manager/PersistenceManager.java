package com.jparams.store.persistence.manager;

import java.util.Collection;

public interface PersistenceManager<V>
{
    /**
     * Returns all identities
     *
     * @return identities
     */
    Collection<Object> getIdentities();

    /**
     * Remove value by identity
     */
    void remove(Object identity);

    /**
     * Get value by identity or return nul if not found
     *
     * @param identity identity to lookup
     * @return value or null
     */
    V get(Object identity);

    /**
     * Persist an object and return its identity
     *
     * @param item item to persist
     * @return identity
     */
    Object persist(V item);

    /**
     * Return the identity of the given value if exists within persistence
     *
     * @param value identity to lookup
     * @return identity
     */
    Object identify(Object value);
}
