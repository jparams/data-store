package com.jparams.store;

import java.util.Collection;
import java.util.Optional;

/**
 * Store of data
 *
 * @param <T> data type
 */
public interface Store<T> extends Collection<T>
{
    /**
     * Create and register a new index with this store
     *
     * @param indexName              unique index name for this store
     * @param valueToKeysTransformer function to transform a value to one or more keys
     * @param <K>                    key type
     * @return index
     */
    <K> Index<T> addIndex(String indexName, Transformer<T, K> valueToKeysTransformer);

    /**
     * Create and register a new index with this store. This will generate an index with a generated name, to specify an index name, call {@link Store#addIndex(String, Transformer)}
     *
     * @param valueToKeysTransformer function to transform a value to one or more keys
     * @param <K>                    key type
     * @return index
     */
    <K> Index<T> addIndex(Transformer<T, K> valueToKeysTransformer);

    /**
     * Find index with name. This is the same as {@link Store#findIndex(String)}, but returns a null instead of an optional if an index cannot be found.
     *
     * @param indexName
     * @return index
     */
    Index<T> getIndex(String indexName);

    /**
     * Find index with name. This is the same as {@link Store#getIndex(String)}, but returns an optional instead of a null if an index cannot be found.
     *
     * @param indexName
     * @return index optional
     */
    default Optional<Index<T>> findIndex(final String indexName)
    {
        return Optional.ofNullable(getIndex(indexName));
    }

    /**
     * Remove an index from store
     *
     * @param indexName index to remove
     * @return true if removed successfully
     */
    boolean removeIndex(String indexName);

    /**
     * Remove an index from store
     *
     * @param index index to remove
     * @return true if removed successfully
     */
    boolean removeIndex(Index<T> index);

    /**
     * Clear the existing indexes and reindex. This can be a slow operation depending on the number of items in the store and total number of indexes
     */
    void reindex();

    /**
     * Reindex a particular item
     */
    void reindex(T item);

    /**
     * Create a copy of this store. This can be an expensive operation depending on the number of items and indexes present
     *
     * @return copy
     */
    Store<T> copy();

    /**
     * Create an unmodifiable store
     *
     * @return unmodifiable store
     */
    default Store<T> unmodifiableStore()
    {
        return new UnmodifiableStore<>(this);
    }
}
