package com.jparams.store;

import java.util.Collection;
import java.util.Optional;

/**
 * The reference managed is internal to a store and should not be exposed outside the class. It is responsible
 * for sotoring, managing and creating references to objects held with a store.
 *
 * @param <T>
 */
public interface ReferenceManager<T>
{
    /**
     * Return all references as a collection. This MUST return a modifiable collection.
     *
     * @return modifiable reference collection
     */
    Collection<Reference<T>> getReferences();

    /**
     * Find a reference matching the item if one exists
     *
     * @param item
     * @return reference
     */
    Optional<Reference<T>> findReference(Object item);

    /**
     * Total number of references held by the manahed
     *
     * @return
     */
    int size();

    /**
     * clear all references held
     */
    void clear();

    /**
     * Create a new reference for the given object. If a reference already exists for this item, return
     * existing reference without adding duplicate.
     *
     * @param obj
     * @return reference
     */
    Reference<T> add(T obj);

    /**
     * Create a copy of the reference manager
     *
     * @return copy
     */
    ReferenceManager<T> copy();
}
