package com.jparams.store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import com.jparams.store.comparison.ComparisonPolicy;
import com.jparams.store.comparison.DefaultComparisonPolicy;
import com.jparams.store.index.Index;
import com.jparams.store.index.IndexException;

/**
 * Store of data
 *
 * @param <V> value type
 */
public interface Store<V> extends Collection<V>
{
    /**
     * Register a new index with this store, mapping a single value to a collection of indexed keys
     *
     * @param indexName        unique index name for this store
     * @param keyProvider      function to provide a value to one or more keys to index on
     * @param comparisonPolicy comparison strategy
     * @param <K>              key type
     * @return index
     * @throws IndexException thrown if the new index failed with exceptions.
     */
    <K> Index<V> multiIndex(String indexName, KeyProvider<Collection<K>, V> keyProvider, ComparisonPolicy<K> comparisonPolicy) throws IndexException;

    /**
     * Register a new index with this store, mapping a single value to a collection of indexed keys
     *
     * @param indexName   unique index name for this store
     * @param keyProvider function to provide a value to one or more keys
     * @param <K>         key type
     * @return index
     * @throws IndexException thrown if the new index failed with exceptions.
     */
    default <K> Index<V> multiIndex(final String indexName, final KeyProvider<Collection<K>, V> keyProvider) throws IndexException
    {
        return multiIndex(indexName, keyProvider, new DefaultComparisonPolicy<>());
    }

    /**
     * Register a new index with this store, mapping a single value to a collection of indexed keys
     *
     * @param keyProvider      function to provide a value to one or more keys
     * @param comparisonPolicy comparison strategy
     * @param <K>              key type
     * @return index
     * @throws IndexException thrown if the new index failed with exceptions.
     */
    default <K> Index<V> multiIndex(final KeyProvider<Collection<K>, V> keyProvider, final ComparisonPolicy<K> comparisonPolicy) throws IndexException
    {
        return multiIndex(UUID.randomUUID().toString(), keyProvider, comparisonPolicy);
    }

    /**
     * Register a new index with this store, mapping a single value to a collection of indexed keys
     *
     * @param keyProvider function to provide a value to one or more keys
     * @param <K>         key type
     * @return index
     * @throws IndexException thrown if the new index failed with exceptions.
     */
    default <K> Index<V> multiIndex(final KeyProvider<Collection<K>, V> keyProvider) throws IndexException
    {
        return multiIndex(UUID.randomUUID().toString(), keyProvider, new DefaultComparisonPolicy<>());
    }

    /**
     * Register a new index with this store
     *
     * @param indexName        unique index name for this store
     * @param keyProvider      function to provide a value to one or more keys to index on
     * @param comparisonPolicy comparison strategy
     * @param <K>              key type
     * @return index
     * @throws IndexException thrown if the new index failed with exceptions.
     */
    default <K> Index<V> index(final String indexName, final KeyProvider<K, V> keyProvider, final ComparisonPolicy<K> comparisonPolicy) throws IndexException
    {
        return multiIndex(indexName, value -> Collections.singletonList(keyProvider.provide(value)), comparisonPolicy);
    }

    /**
     * Register a new index with this store
     *
     * @param indexName   unique index name for this store
     * @param keyProvider function to provide a value to one or more keys
     * @param <K>         key type
     * @return index
     * @throws IndexException thrown if the new index failed with exceptions.
     */
    default <K> Index<V> index(final String indexName, final KeyProvider<K, V> keyProvider) throws IndexException
    {
        return index(indexName, keyProvider, new DefaultComparisonPolicy<>());
    }

    /**
     * Register a new index with this store.
     *
     * @param keyProvider      function to provide a value to one or more keys
     * @param comparisonPolicy comparison strategy
     * @param <K>              key type
     * @return index
     * @throws IndexException thrown if the new index failed with exceptions.
     */
    default <K> Index<V> index(final KeyProvider<K, V> keyProvider, final ComparisonPolicy<K> comparisonPolicy) throws IndexException
    {
        return index(UUID.randomUUID().toString(), keyProvider, comparisonPolicy);
    }

    /**
     * Create and register a new index with this store.
     *
     * @param keyProvider function to provide a value to one or more keys
     * @param <K>         key type
     * @return index
     * @throws IndexException thrown if the new index failed with exceptions.
     */
    default <K> Index<V> index(final KeyProvider<K, V> keyProvider) throws IndexException
    {
        return index(UUID.randomUUID().toString(), keyProvider, new DefaultComparisonPolicy<>());
    }

    /**
     * Find index with name. This is the same as {@link Store#findIndex(String)}, but returns a null instead of an optional if an index cannot be found.
     *
     * @param indexName name of index to lookup
     * @return index
     */
    Index<V> getIndex(String indexName);

    /**
     * Get all indexes
     *
     * @return indexes
     */
    Collection<Index<V>> getIndexes();

    /**
     * Remove all indexes associated with this store
     */
    default void removeAllIndexes()
    {
        new ArrayList<>(getIndexes()).forEach(this::removeIndex);
    }

    /**
     * Find index with name. This is the same as {@link Store#getIndex(String)}, but returns an optional instead of a null if an index cannot be found.
     *
     * @param indexName name of index to lookup
     * @return index optional
     */
    default Optional<Index<V>> findIndex(final String indexName)
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
    boolean removeIndex(Index<V> index);

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
    void reindex(Collection<V> items) throws IndexException;

    /**
     * Reindex a particular item. This method will need to be called anytime a change
     * is made to an item stored within the store that causes its indexes to become out
     * of date.
     *
     * @param item item to reindex
     * @throws IndexException thrown if one or more indexes failed with exceptions.
     */
    void reindex(final V item) throws IndexException;

    /**
     * Adds an item to the store and indexes it. If the item already exists in the store,
     * it will be reindexed.
     *
     * @param item item to on
     * @return true if item did not previously exist in the store.
     * @throws IndexException thrown if one or more indexes failed with exceptions.
     */
    @Override
    boolean add(V item) throws IndexException;

    /**
     * Adds all item to the store and indexes them. If an item already exists in the store,
     * it will be reindexed.
     *
     * @param items items to on
     * @return true if one or more items did not previously exist in the store.
     * @throws IndexException thrown if one or more indexes failed with exceptions.
     */
    @Override
    boolean addAll(Collection<? extends V> items) throws IndexException;

    /**
     * Adds all item to the store and indexes them. If an item already exists in the store,
     * it will be re-indexed.
     *
     * @param items items to on
     * @return true if one or more items did not previously exist in the store.
     * @throws IndexException thrown if one or more indexes failed with exceptions.
     */
    default boolean addAll(final V[] items) throws IndexException
    {
        return addAll(Arrays.asList(items));
    }

    /**
     * Create a copy of this store. This can be an expensive operation depending
     * on the number of items and indexes present. The copied store will be fully
     * independent from this store. Any changes made to the copy will not reflect back
     * onto this store.
     *
     * @return copy
     */
    Store<V> copy();

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
    default Store<V> unmodifiableStore()
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
    default Store<V> synchronizedStore()
    {
        return new SynchronizedStore<>(this);
    }
}
