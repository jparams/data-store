package com.jparams.store.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.jparams.store.index.reducer.Reducer;
import com.jparams.store.reference.Reference;

public class References<K, V>
{
    private final K key;
    private final Reducer<K, V> reducer;
    private final Set<Reference<V>> references;
    private Collection<Reference<V>> reducedReferences;

    private References(final K key, final Set<Reference<V>> references, final Collection<Reference<V>> reducedReferences, final Reducer<K, V> reducer)
    {
        this.key = key;
        this.references = new LinkedHashSet<>(references);
        this.reducedReferences = new ArrayList<>(reducedReferences);
        this.reducer = reducer;
    }

    public References(final K key, final Reference<V> reference, final Reducer<K, V> reducer)
    {
        this(key, Collections.singleton(reference), Collections.emptySet(), reducer);
        reduce();
    }

    public void add(final Reference<V> reference)
    {
        references.add(reference);
        reduce();
    }

    public void remove(final Reference<V> reference)
    {
        references.remove(reference);
        reduce();
    }

    public List<V> getAll()
    {
        return reducedReferences.stream().map(Reference::get).collect(Collectors.toList());
    }

    public boolean isEmpty()
    {
        return references.isEmpty();
    }

    public Optional<V> findFirst()
    {
        return reducedReferences.stream().map(Reference::get).findFirst();
    }

    public References<K, V> copy()
    {
        return new References<>(key, references, reducedReferences, reducer);
    }

    private void reduce()
    {
        if (reducer == null)
        {
            reducedReferences = references;
            return;
        }

        final List<Element<V>> elements = references.stream().map(Element::new).collect(Collectors.toList());
        reducer.reduce(key, elements);
        reducedReferences = elements.stream().filter(element -> !element.isRemoved()).map(Element::getReference).collect(Collectors.toList());
    }
}
