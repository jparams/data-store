package com.jparams.store;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jparams.store.comparison.Comparison;
import com.jparams.store.index.AbstractIndex;
import com.jparams.store.index.Index;
import com.jparams.store.index.IndexCreationException;
import com.jparams.store.reference.Reference;
import com.jparams.store.reference.ReferenceManager;

public abstract class AbstractStore<V> extends AbstractCollection<V> implements Store<V>
{
    private final ReferenceManager<V> referenceManager;
    private final Map<String, AbstractIndex<V>> indexMap;

    protected AbstractStore(final ReferenceManager<V> referenceManager)
    {
        this.referenceManager = referenceManager;
        this.indexMap = new HashMap<>();
    }

    protected AbstractStore(final ReferenceManager<V> referenceManager, final Set<AbstractIndex<V>> indexes)
    {
        this.referenceManager = referenceManager;
        this.indexMap = indexes.stream().collect(Collectors.toMap(AbstractIndex::getName, Function.identity()));
    }


    @Override
    public <K> Index<V> index(final String indexName, final KeyProvider<K, V> keyProvider, final Comparison<K> comparison) throws IndexException
    {
        if (indexMap.containsKey(indexName))
        {
            throw new IllegalArgumentException("An index already exists with this name");
        }

        @SuppressWarnings("unchecked") final AbstractIndex<V> newIndex = createIndex(indexName, (KeyProvider<Object, V>) keyProvider, (Comparison<Object>) comparison);
        indexMap.put(indexName, newIndex);
        indexReferences(Collections.singleton(newIndex), referenceManager.getReferences());
        return newIndex;
    }

    @Override
    public Index<V> getIndex(final String indexName)
    {
        return indexMap.get(indexName);
    }

    @Override
    public Collection<Index<V>> getIndexes()
    {
        return Collections.unmodifiableCollection(indexMap.values());
    }

    @Override
    public boolean removeIndex(final Index<V> index)
    {
        if (index.equals(indexMap.get(index.getName())))
        {
            indexMap.remove(index.getName());
            return true;
        }

        return false;
    }

    @Override
    public boolean removeIndex(final String indexName)
    {
        return indexMap.remove(indexName) != null;
    }

    @Override
    public void reindex()
    {
        indexReferences(indexMap.values(), referenceManager.getReferences());
    }

    @Override
    public void reindex(final V item)
    {
        reindex(Collections.singleton(item));
    }

    @Override
    public void reindex(final Collection<V> items)
    {
        indexReferences(indexMap.values(), items.stream()
                                                .map(referenceManager::findReference)
                                                .filter(Optional::isPresent)
                                                .map(Optional::get)
                                                .collect(Collectors.toList()));
    }

    @Override
    public int size()
    {
        return referenceManager.size();
    }

    @Override
    public boolean isEmpty()
    {
        return size() == 0;
    }

    @Override
    public boolean contains(final Object obj)
    {
        return referenceManager.findReference(obj).isPresent();
    }

    @Override
    public Iterator<V> iterator()
    {
        return new StoreIterator(referenceManager.getReferences().iterator());
    }

    @Override
    public boolean addAll(final Collection<? extends V> collection)
    {
        final List<Reference<V>> references = new ArrayList<>();
        boolean changed = false;

        for (final V item : collection)
        {
            final Optional<Reference<V>> existingReference = referenceManager.findReference(item);

            if (existingReference.isPresent())
            {
                references.add(existingReference.get());
                continue;
            }

            references.add(referenceManager.add(item));
            changed = true;
        }

        indexReferences(indexMap.values(), references);
        return changed;
    }

    @Override
    public boolean add(final V item)
    {
        return addAll(Collections.singleton(item));
    }

    @Override
    public boolean remove(final Object obj)
    {
        final Reference<V> reference = referenceManager.remove(obj);

        if (reference != null)
        {
            indexMap.values().forEach(index -> index.removeIndex(reference));
            return true;
        }

        return false;
    }

    @Override
    public void clear()
    {
        referenceManager.clear();
        indexMap.values().forEach(AbstractIndex::clear);
    }

    @Override
    public Store<V> copy()
    {
        return createCopy(referenceManager, indexMap.values());
    }

    protected abstract Store<V> createCopy(final ReferenceManager<V> referenceManager, final Collection<AbstractIndex<V>> indexes);

    protected abstract AbstractIndex<V> createIndex(String indexName, KeyProvider<Object, V> keyProvider, Comparison<Object> comparison);

    private static <T> void indexReferences(final Collection<AbstractIndex<T>> indexes, final Collection<Reference<T>> references)
    {
        final List<IndexCreationException> exceptions = new ArrayList<>();

        for (final Reference<T> reference : references)
        {
            for (final AbstractIndex<T> index : indexes)
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

    private class StoreIterator implements Iterator<V>
    {
        private final Iterator<Reference<V>> iterator;
        private Reference<V> previous;

        StoreIterator(final Iterator<Reference<V>> iterator)
        {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext()
        {
            return iterator.hasNext();
        }

        @Override
        public V next()
        {
            previous = iterator.next();
            return previous.get();
        }

        @Override
        public void remove()
        {
            iterator.remove();
            indexMap.values().forEach(index -> index.removeIndex(previous));
        }
    }
}
