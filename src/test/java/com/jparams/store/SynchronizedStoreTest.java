package com.jparams.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import com.jparams.store.comparison.DefaultComparisonPolicy;
import com.jparams.store.index.Index;
import com.jparams.store.index.SynchronizedIndex;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import nl.jqno.equalsverifier.EqualsVerifier;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SynchronizedStoreTest
{
    private SynchronizedStore<String> subject;

    @Mock
    private Store<String> mockStore;

    @Mock
    private Index<String> mockIndex;

    @Before
    public void setUp()
    {
        subject = new SynchronizedStore<>(mockStore);
    }

    @Test
    public void testGetIndexedValues()
    {
        final ArrayList<String> list = new ArrayList<>();
        when(mockStore.get(any(), any())).thenReturn(list);
        assertThat(subject.get("index", "key")).isSameAs(list);
        verify(mockStore).get("index", "key");
    }

    @Test
    public void testGetFirstValue()
    {
        when(mockStore.getFirst(any(), any())).thenReturn("abc");
        assertThat(subject.getFirst("index", "key")).isEqualTo("abc");
        verify(mockStore).getFirst("index", "key");
    }

    @Test
    public void testFindFirstValue()
    {
        when(mockStore.findFirst(any(), any())).thenReturn(Optional.of("abc"));
        assertThat(subject.findFirst("index", "key")).hasValue("abc");
        verify(mockStore).findFirst("index", "key");
    }

    @Test
    public void testIndexWithNameAndProviderAndComparisonPolicy()
    {
        final DefaultComparisonPolicy<String> comparisonPolicy = new DefaultComparisonPolicy<>();
        final KeyProvider<String, String> keyProvider = (val) -> "";
        subject.index("name", keyProvider, comparisonPolicy);
        verify(mockStore).index(eq("name"), same(keyProvider), same(comparisonPolicy));
    }

    @Test
    public void testIndexWithProviderAndComparisonPolicy()
    {
        final DefaultComparisonPolicy<String> comparisonPolicy = new DefaultComparisonPolicy<>();
        final KeyProvider<String, String> keyProvider = (val) -> "";
        subject.index(keyProvider, comparisonPolicy);
        verify(mockStore).index(same(keyProvider), same(comparisonPolicy));
    }

    @Test
    public void testIndexWithNameAndProvider()
    {
        final KeyProvider<String, String> keyProvider = (val) -> "";
        subject.index("name", keyProvider);
        verify(mockStore).index(eq("name"), same(keyProvider));
    }

    @Test
    public void testIndexWithProvider()
    {
        final KeyProvider<String, String> keyProvider = (val) -> "";

        subject.index(keyProvider);

        verify(mockStore).index(same(keyProvider));
    }

    @Test
    public void testMultiIndexWithNameAndProviderAndComparisonPolicy()
    {
        final DefaultComparisonPolicy<String> comparisonPolicy = new DefaultComparisonPolicy<>();
        final KeyProvider<Collection<String>, String> keyProvider = (val) -> Collections.singletonList("");
        subject.multiIndex("name", keyProvider, comparisonPolicy);
        verify(mockStore).multiIndex(eq("name"), same(keyProvider), same(comparisonPolicy));
    }

    @Test
    public void testMultiIndexWithProviderAndComparisonPolicy()
    {
        final DefaultComparisonPolicy<String> comparisonPolicy = new DefaultComparisonPolicy<>();
        final KeyProvider<Collection<String>, String> keyProvider = (val) -> Collections.singletonList("");
        subject.multiIndex(keyProvider, comparisonPolicy);
        verify(mockStore).multiIndex(same(keyProvider), same(comparisonPolicy));
    }

    @Test
    public void testMultiIndexWithNameAndProvider()
    {
        final KeyProvider<Collection<String>, String> keyProvider = (val) -> Collections.singletonList("");
        subject.multiIndex("name", keyProvider);
        verify(mockStore).multiIndex(eq("name"), same(keyProvider));
    }

    @Test
    public void testMultiIndexWithProvider()
    {
        final KeyProvider<Collection<String>, String> keyProvider = (val) -> Collections.singletonList("");

        subject.multiIndex(keyProvider);

        verify(mockStore).multiIndex(same(keyProvider));
    }

    @Test
    public void testGetIndex()
    {
        when(mockStore.getIndex(anyString())).thenReturn(mockIndex);

        assertThat(subject.getIndex("abc")).isInstanceOf(SynchronizedIndex.class)
                                           .extracting(index -> ((SynchronizedIndex<String>) index).getIndex())
                                           .isEqualTo(Collections.singletonList(mockIndex));

        verify(mockStore).getIndex("abc");
    }

    @Test
    public void testGetIndexNotFound()
    {
        when(mockStore.getIndex(anyString())).thenReturn(null);

        assertThat(subject.getIndex("abc")).isNull();

        verify(mockStore).getIndex("abc");
    }

    @Test
    public void testGetIndexes()
    {
        when(mockStore.getIndexes()).thenReturn(Collections.singletonList(mockIndex));

        assertThat(subject.getIndexes()).first()
                                        .isInstanceOf(SynchronizedIndex.class)
                                        .extracting(index -> ((SynchronizedIndex<String>) index).getIndex())
                                        .isEqualTo(Collections.singletonList(mockIndex));

        verify(mockStore).getIndexes();
    }

    @Test
    public void testRemoveIndexByName()
    {
        subject.removeIndex("abc");

        verify(mockStore).removeIndex("abc");
    }

    @Test
    public void testRemoveIndex()
    {
        subject.removeIndex(mockIndex);

        verify(mockStore).removeIndex(mockIndex);
    }

    @Test
    public void testFindIndex()
    {
        when(mockStore.findIndex(any())).thenReturn(Optional.of(mockIndex));

        final Optional<Index<String>> index = subject.findIndex("index");
        assertThat(index).isPresent();
        assertThat(index.orElse(null)).isInstanceOf(SynchronizedIndex.class)
                                      .extracting(idx -> ((SynchronizedIndex<?>) idx).getIndex())
                                      .first()
                                      .isSameAs(mockIndex);
    }

    @Test
    public void testFindIndexReturnsEmpty()
    {
        when(mockStore.findIndex(any())).thenReturn(Optional.empty());

        final Optional<Index<String>> index = subject.findIndex("index");
        assertThat(index).isNotPresent();
    }


    @Test
    public void testRemoveSynchronizedIndex()
    {
        @SuppressWarnings("unchecked") final SynchronizedIndex<String> mockSynchronizedIndex = mock(SynchronizedIndex.class);
        when(mockSynchronizedIndex.getIndex()).thenReturn(mockIndex);

        subject.removeIndex(mockSynchronizedIndex);

        verify(mockStore).removeIndex(mockIndex);
    }

    @Test
    public void testReindex()
    {
        subject.reindex();

        verify(mockStore).reindex();
    }

    @Test
    public void testReindexCollection()
    {
        final List<String> items = Collections.singletonList("abc");
        subject.reindex(items);
        verify(mockStore).reindex(items);
    }

    @Test
    public void testReindexValue()
    {
        subject.reindex("abc");

        verify(mockStore).reindex("abc");
    }

    @Test
    public void testUnmodifiableStore()
    {
        assertThat(subject.unmodifiableStore()).isInstanceOf(UnmodifiableStore.class);
    }

    @Test
    public void testSize()
    {
        when(mockStore.size()).thenReturn(32);

        assertThat(subject.size()).isEqualTo(32);

        verify(mockStore).size();
    }

    @Test
    public void testIsEmpty()
    {
        when(mockStore.isEmpty()).thenReturn(true);

        assertThat(subject.isEmpty()).isTrue();

        verify(mockStore).isEmpty();
    }

    @Test
    public void testContains()
    {
        when(mockStore.contains(anyString())).thenReturn(true);

        assertThat(subject.contains("abc")).isTrue();

        verify(mockStore).contains("abc");
    }

    @Test
    public void testRemoveAllIndexes()
    {
        subject.removeAllIndexes();

        verify(mockStore).removeAllIndexes();
    }

    @Test
    public void testIterator()
    {
        @SuppressWarnings("unchecked") final Iterator<String> mockIterator = mock(Iterator.class);
        when(mockStore.iterator()).thenReturn(mockIterator);

        assertThat(subject.iterator()).isSameAs(mockIterator);

        verify(mockStore).iterator();
    }

    @Test
    public void testToArray()
    {
        final Object[] array = new Object[]{};
        when(mockStore.toArray()).thenReturn(array);
        assertThat(subject.toArray()).isSameAs(array);
        verify(mockStore).toArray();
    }

    @Test
    public void testToArrayOfType()
    {
        final String[] array = new String[]{};
        when(mockStore.toArray(array)).thenReturn(array);
        assertThat(subject.toArray(array)).isSameAs(array);
        verify(mockStore).toArray(array);
    }

    @Test
    public void testAdd()
    {
        subject.add("");

        verify(mockStore).add("");
    }

    @Test
    public void testAddArray()
    {
        final String[] array = {""};
        subject.addAll(array);
        verify(mockStore).addAll(array);
    }

    @Test
    public void testGetStore()
    {
        assertThat(subject.getStore()).isSameAs(mockStore);
    }

    @Test
    public void testGetSynchronizedStore()
    {
        assertThat(subject.synchronizedStore()).isSameAs(subject);
    }

    @Test
    public void testRemove()
    {
        subject.remove("");

        verify(mockStore).remove("");
    }

    @Test
    public void testRemoveIf()
    {
        final Predicate<String> predicate = (val) -> true;
        subject.removeIf(predicate);

        verify(mockStore).removeIf(predicate);
    }

    @Test
    public void testContainsAll()
    {
        when(mockStore.containsAll(anyList())).thenReturn(true);
        assertThat(subject.containsAll(Collections.emptyList())).isEqualTo(true);
        verify(mockStore).containsAll(Collections.emptyList());
    }

    @Test
    public void testAddAll()
    {
        final Set<String> items = Collections.singleton("");

        subject.addAll(items);

        verify(mockStore).addAll(items);
    }

    @Test
    public void testRemoveAll()
    {
        final Set<String> items = Collections.singleton("");

        subject.removeAll(items);

        verify(mockStore).removeAll(items);
    }

    @Test
    public void testRetainAll()
    {
        final Set<String> items = Collections.singleton("");

        subject.retainAll(items);

        verify(mockStore).retainAll(items);
    }

    @Test
    public void testClear()
    {
        subject.clear();

        verify(mockStore).clear();
    }

    @Test
    public void testCopy()
    {
        when(mockStore.copy()).thenReturn(mockStore);
        assertThat(subject.copy()).isSameAs(mockStore);
        verify(mockStore).copy();
    }

    @Test
    public void testToString()
    {
        when(mockStore.toString()).thenReturn("abc");
        assertThat(subject.toString()).isEqualTo("abc");
    }

    @Test
    public void equalsAndHashCodeContract()
    {
        EqualsVerifier.forClass(UnmodifiableStore.class).usingGetClass().verify();
    }
}