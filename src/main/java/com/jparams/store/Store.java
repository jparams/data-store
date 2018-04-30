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
     * @throws IndexException thrown if the new index failed with exceptions.
     */
    <K> Index<T> addIndex(String indexName, Transformer<T, K> valueToKeysTransformer) throws IndexException;

    /**
     * Create and register a new index with this store. This will generate an index with a generated name, to specify an index name, call {@link Store#addIndex(String, Transformer)}
     *
     * @param valueToKeysTransformer function to transform a value to one or more keys
     * @param <K>                    key type
     * @return index
     * @throws IndexException thrown if the new index failed with exceptions.
     */
    <K> Index<T> addIndex(Transformer<T, K> valueToKeysTransformer) throws IndexException;

    /**
     * Find index with name. This is the same as {@link Store#findIndex(String)}, but returns a null instead of an optional if an index cannot be found.
     *
     * @param indexName name of index to lookup
     * @return index
     */
    Index<T> getIndex(String indexName);

    /**
     * Get all indexes
     *
     * @return indexes
     */
    Collection<Index<T>> getIndexes();

    /**
     * Remove all indexes associated with this store
     */
    default void clearIndexes()
    {
        getIndexes().forEach(this::removeIndex);
    }

    /**
     * Find index with name. This is the same as {@link Store#getIndex(String)}, but returns an optional instead of a null if an index cannot be found.
     *
     * @param indexName name of index to lookup
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
     * Clear the existing indexes and reindex the entire store. This can be a slow operation
     * depending on the number of items in the store and total number of indexes.
     *
     * @throws IndexException thrown if one or more indexes failed with exceptions.
     */
    void reindex() throws IndexException;

    /**
     * Reindex a collection of items. This method will need to be called anytime a change
     * is made to items stored within the store that causes its indexes to become out
     * of date.
     *
     * @param items items to reindex
     * @throws IndexException thrown if one or more indexes failed with exceptions.
     */
    void reindex(Collection<T> items) throws IndexException;

    /**
     * Reindex a particular item. This method will need to be called anytime a change
     * is made to an item stored within the store that causes its indexes to become out
     * of date.
     *
     * @param item item to reindex
     * @throws IndexException thrown if one or more indexes failed with exceptions.
     */
    void reindex(final T item) throws IndexException;

    /**
     * Adds an item to the store and indexes it. If the item already exists in the store,
     * it will be reindexed.
     *
     * @param item item to add
     * @return true if item did not previously exist in the store.
     * @throws IndexException thrown if one or more indexes failed with exceptions.
     */
    @Override
    boolean add(T item) throws IndexException;

    /**
     * Adds all item sto the store and indexes them. If an item already exists in the store,
     * it will be reindexed.
     *
     * @param items items to add
     * @return true if one or more items did not previously exist in the store.
     * @throws IndexException thrown if one or more indexes failed with exceptions.
     */
    @Override
    boolean addAll(Collection<? extends T> items) throws IndexException;

    /**
     * Create a copy of this store. This can be an expensive operation depending
     * on the number of items and indexes present. The copied store will be fully
     * independent from this store. Any changes made to the copy will not reflect back
     * onto this store.
     *
     * @return copy
     */
    Store<T> copy();

    /**
     * Returns an unmodifiable view of this store. This method allows
     * modules to provide users with "read-only" access to internal
     * collections. Query operations on the returned store "read through"
     * the backed store, and attempts to modify the returned
     * store, whether direct or via its iterator, result in an
     * <tt>UnsupportedOperationException</tt>.<p>
     *
     * @return an unmodifiable view of this store.
     */
    default Store<T> unmodifiableStore()
    {
        return new UnmodifiableStore<>(this);
    }

    /**
     * Returns a synchronized (thread-safe) map Store backed by this store.
     * In order to guarantee serial access, it is critical that
     * <strong>all</strong> access to the backing store is accomplished
     * through the returned store.<p> Any references held to {@link Index}es
     * should be discarded. Call {@link Store#getIndex(String)} on the synchronized
     * store to obtain synchronized indexes.
     *
     * It is imperative that the user manually synchronize on the returned
     * Store when iterating over it:
     * <pre>
     *  Store store = oldStore.synchronizedStore();
     *      ...
     *  synchronized (store) {
     *      Iterator i = store.iterator(); // Must be in synchronized block
     *      while (i.hasNext())
     *          foo(i.next());
     *  }
     * </pre>
     * Failure to follow this advice may result in non-deterministic behavior.
     *
     * @return synchronized store
     */
    default Store<T> synchronizedStore()
    {
        if (this instanceof SynchronizedStore)
        {
            final Store<T> store = ((SynchronizedStore<T>) this).getStore();
            return new SynchronizedStore<>(store);
        }

        return new SynchronizedStore<>(this);
    }
}
