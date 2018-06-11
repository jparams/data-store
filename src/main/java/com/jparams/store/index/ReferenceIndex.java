package com.jparams.store.index;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.jparams.store.index.comparison.ComparisonPolicy;
import com.jparams.store.index.reducer.Reducer;
import com.jparams.store.reference.Reference;

/**
 * Maintains indexes against references to stored items
 *
 * @param <V> value type
 */
public class ReferenceIndex<K, V> extends AbstractIndex<K, V>
{
    private final Map<K, References<K, V>> keyToReferencesMap;
    private final Map<Reference<V>, Set<K>> referenceToKeysMap;

    private ReferenceIndex(final String indexName, final KeyMapper<Collection<K>, V> keyMapper, final Reducer<K, V> reducer, final ComparisonPolicy<K> comparisonPolicy, final Map<K, References<K, V>> keyToReferencesMap, final Map<Reference<V>, Set<K>> referenceToKeysMap)
    {
        super(indexName, keyMapper, reducer, comparisonPolicy);
        this.keyToReferencesMap = keyToReferencesMap;
        this.referenceToKeysMap = referenceToKeysMap;
    }

    public ReferenceIndex(final String indexName, final KeyMapper<Collection<K>, V> keyMapper, final Reducer<K, V> reducer, final ComparisonPolicy<K> comparisonPolicy)
    {
        this(indexName, keyMapper, reducer, comparisonPolicy, new HashMap<>(), new HashMap<>());
    }

    @Override
    public Optional<V> findFirst(final Object key)
    {
        final K comparableKey = getComparableKey(key);
        final References<K, V> references = keyToReferencesMap.get(comparableKey);

        if (references == null)
        {
            return Optional.empty();
        }

        return references.findFirst();
    }

    @Override
    public List<V> get(final Object key)
    {
        final K comparableKey = getComparableKey(key);
        final References<K, V> references = keyToReferencesMap.get(comparableKey);

        if (references == null)
        {
            return Collections.emptyList();
        }

        return references.getAll();
    }

    @Override
    public void index(final Reference<V> reference) throws IndexCreationException
    {
        final Set<K> keys = generateKeys(reference);

        removeIndex(reference);

        if (!keys.isEmpty())
        {
            referenceToKeysMap.put(reference, Collections.unmodifiableSet(keys));
            keys.forEach(key -> keyToReferencesMap.computeIfAbsent(key, ignore -> new References<>(key, reference, getReducer())).add(reference));
        }
    }

    @Override
    public void removeIndex(final Reference<V> reference)
    {
        final Set<K> keys = referenceToKeysMap.get(reference);

        if (keys == null)
        {
            return;
        }

        for (final K key : keys)
        {
            final References<K, V> references = keyToReferencesMap.get(key);

            if (reference != null)
            {
                references.remove(reference);

                if (references.isEmpty())
                {
                    keyToReferencesMap.remove(key);
                }
            }
        }

        referenceToKeysMap.remove(reference);
    }

    @Override
    protected AbstractIndex<K, V> copy(final String name, final KeyMapper<Collection<K>, V> keyMapper, final Reducer<K, V> reducer, final ComparisonPolicy<K> comparisonPolicy)
    {
        final Map<K, References<K, V>> keyToReferencesMapCopy = keyToReferencesMap.entrySet()
                                                                                  .stream()
                                                                                  .collect(Collectors.toMap(Entry::getKey, entry -> entry.getValue().copy()));

        final Map<Reference<V>, Set<K>> referenceToKeysMapCopy = new HashMap<>(referenceToKeysMap);

        return new ReferenceIndex<>(name, keyMapper, reducer, comparisonPolicy, keyToReferencesMapCopy, referenceToKeysMapCopy);
    }

    @Override
    public void clear()
    {
        keyToReferencesMap.clear();
        referenceToKeysMap.clear();
    }
}
