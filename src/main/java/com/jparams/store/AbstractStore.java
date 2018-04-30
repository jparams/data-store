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
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractStore<T> extends AbstractCollection<T> implements Store<T>
{
    private final ReferenceManager<T> referenceManager;
    private final Map<String, AbstractIndex<T>> indexMap;

    AbstractStore(final ReferenceManager<T> referenceManager)
    {
        this.referenceManager = referenceManager;
        this.indexMap = new HashMap<>();
    }

    AbstractStore(final ReferenceManager<T> referenceManager, final Set<AbstractIndex<T>> indexes)
    {
        this.referenceManager = referenceManager;
        this.indexMap = indexes.stream().collect(Collectors.toMap(AbstractIndex::getName, Function.identity()));
    }

    @Override
    public <K> Index<T> addIndex(final String indexName, final Transformer<T, K> transformer)
    {
        if (indexMap.containsKey(indexName))
        {
            throw new IllegalArgumentException("An index already exists with this name");
        }

        final AbstractIndex<T> newIndex = createIndex(indexName, transformer);
        indexMap.put(indexName, newIndex);
        indexReferences(Collections.singleton(newIndex), referenceManager.getReferences());
        return newIndex;
    }

    @Override
    public <K> Index<T> addIndex(final Transformer<T, K> transformer)
    {
        return addIndex(UUID.randomUUID().toString(), transformer);
    }

    @Override
    public Index<T> getIndex(final String indexName)
    {
        return indexMap.get(indexName);
    }

    @Override
    public Collection<Index<T>> getIndexes()
    {
        return Collections.unmodifiableCollection(indexMap.values());
    }

    @Override
    public boolean removeIndex(final Index<T> index)
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
    public void reindex(final T item)
    {
        reindex(Collections.singleton(item));
    }

    @Override
    public void reindex(final Collection<T> items)
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
    public Iterator<T> iterator()
    {
        return new StoreIterator(referenceManager.getReferences().iterator());
    }

    @Override
    public boolean addAll(final Collection<? extends T> collection)
    {
        final List<Reference<T>> references = new ArrayList<>();
        boolean changed = false;

        for (final T item : collection)
        {
            final Optional<Reference<T>> existingReference = referenceManager.findReference(item);

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
    public boolean add(final T item)
    {
        return addAll(Collections.singleton(item));
    }

    @Override
    public boolean remove(final Object obj)
    {
        final Optional<Reference<T>> reference = referenceManager.findReference(obj);

        if (reference.isPresent())
        {
            indexMap.values().forEach(index -> index.removeIndex(reference.get()));
            return true;
        }

        return false;
    }

    @Override
    public void clear()
    {
        referenceManager.clear();
        reindex();
    }

    @Override
    public Store<T> copy()
    {
        return createCopy(referenceManager, indexMap.values());
    }

    protected abstract Store<T> createCopy(final ReferenceManager<T> referenceManager, final Collection<AbstractIndex<T>> indexes);

    protected abstract AbstractIndex<T> createIndex(final String indexName, final Transformer<T, ?> transformer);

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

    private class StoreIterator implements Iterator<T>
    {
        private final Iterator<Reference<T>> iterator;
        private Reference<T> previous;

        StoreIterator(final Iterator<Reference<T>> iterator)
        {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext()
        {
            return iterator.hasNext();
        }

        @Override
        public T next()
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
