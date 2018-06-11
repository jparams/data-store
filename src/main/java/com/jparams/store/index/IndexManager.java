package com.jparams.store.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.jparams.store.reference.Reference;

public abstract class IndexManager<V>
{
    private final Map<String, AbstractIndex<?, V>> indexMap;

    public IndexManager(final Collection<AbstractIndex<?, V>> indexes)
    {
        this.indexMap = new HashMap<>();
        indexes.forEach(index -> indexMap.put(index.getName(), index));
    }

    public <K> AbstractIndex<K, V> createIndex(final String indexName, final IndexDefinition<K, V> indexDefinition, final Collection<Reference<V>> references)
    {
        if (indexMap.containsKey(indexName))
        {
            throw new IllegalArgumentException("An index already exists with this name");
        }

        final AbstractIndex<K, V> newIndex = createIndex(indexName, indexDefinition);
        indexMap.put(indexName, newIndex);
        indexReferences(Collections.singleton(newIndex), references);
        return newIndex;
    }

    public Index<V> getIndex(final String indexName)
    {
        return indexMap.get(indexName);
    }

    public void reindex(final Collection<Reference<V>> references)
    {
        indexReferences(indexMap.values(), references);
    }

    public boolean removeIndex(final String indexName)
    {
        return indexMap.remove(indexName) != null;
    }

    public boolean removeIndex(final Index<V> index)
    {
        if (index.equals(indexMap.get(index.getName())))
        {
            indexMap.remove(index.getName());
            return true;
        }

        return false;
    }

    public void removeReference(final Reference<V> reference)
    {
        indexMap.values().forEach(index -> index.removeIndex(reference));
    }

    public void clear()
    {
        indexMap.values().forEach(AbstractIndex::clear);
    }

    public Collection<Index<V>> getIndexes()
    {
        return Collections.unmodifiableCollection(indexMap.values());
    }

    public IndexManager<V> copy()
    {
        final Set<AbstractIndex<?, V>> copyOfIndexes = indexMap.values().stream().map(AbstractIndex::copy).collect(Collectors.toSet());
        return createCopy(copyOfIndexes);
    }

    protected abstract IndexManager<V> createCopy(Set<AbstractIndex<?, V>> copyOfIndexes);

    protected abstract <K> AbstractIndex<K, V> createIndex(String indexName, IndexDefinition<K, V> indexDefinition);

    private static <T> void indexReferences(final Collection<AbstractIndex<?, T>> indexes, final Collection<Reference<T>> references)
    {
        final List<IndexCreationException> exceptions = new ArrayList<>();

        for (final Reference<T> reference : references)
        {
            for (final AbstractIndex<?, T> index : indexes)
            {
                try
                {
                    index.index(reference);
                }
                catch (final IndexCreationException e)
                {
                    exceptions.add(e);
                }
            }
        }

        if (!exceptions.isEmpty())
        {
            final String message = (exceptions.size() == 1 ? "1 exception" : exceptions.size() + " exceptions") + " occurred during indexing";
            throw new IndexException(message, exceptions);
        }
    }
}
