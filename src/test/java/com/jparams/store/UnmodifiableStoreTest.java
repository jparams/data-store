package com.jparams.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import com.jparams.store.index.Index;
import com.jparams.store.index.IndexDefinition;
import com.jparams.store.index.KeyMapper;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UnmodifiableStoreTest
{
    private UnmodifiableStore<String> subject;

    @Mock
    private Store<String> mockStore;

    @Mock
    private Index<String> mockIndex;

    @Before
    public void setUp()
    {
        subject = new UnmodifiableStore<>(mockStore);
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

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexWithMapper()
    {
        subject.index((KeyMapper<?, String>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexWithNameAndMapper()
    {
        subject.index("", (KeyMapper<?, String>) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexWithNameAndDefinition()
    {
        subject.index("", IndexDefinition.withKeyMapping(str -> str));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIndex()
    {
        subject.index(IndexDefinition.withKeyMapping(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAllIndexes()
    {
        subject.removeAllIndexes();
    }

    @Test
    public void testGetIndex()
    {
        when(mockStore.getIndex(anyString())).thenReturn(mockIndex);

        assertThat(subject.getIndex("abc")).isSameAs(mockIndex);

        verify(mockStore).getIndex("abc");
    }

    @Test
    public void testFindIndex()
    {
        when(mockStore.findIndex(anyString())).thenReturn(Optional.of(mockIndex));

        assertThat(subject.findIndex("abc")).hasValue(mockIndex);

        verify(mockStore).findIndex("abc");
    }

    @Test
    public void testGetIndexes()
    {
        when(mockStore.getIndexes()).thenReturn(Collections.singletonList(mockIndex));

        assertThat(subject.getIndexes()).containsExactly(mockIndex);

        verify(mockStore).getIndexes();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveIndexByName()
    {
        subject.removeIndex("abc");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveIndex()
    {
        subject.removeIndex(mockIndex);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReindex()
    {
        subject.reindex();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReindexCollection()
    {
        subject.reindex(Collections.singletonList("abc"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReindexValue()
    {
        subject.reindex("abc");
    }

    @Test
    public void testUnmodifiableStore()
    {
        assertThat(subject.unmodifiableStore()).isSameAs(subject);
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
    public void testIterator()
    {
        assertThat(subject.iterator()).isExactlyInstanceOf(UnmodifiableIterator.class);

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

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd()
    {
        subject.add("");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove()
    {
        subject.remove("");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveIf()
    {
        subject.removeIf(String::isEmpty);
    }

    @Test
    public void testContainsAll()
    {
        when(mockStore.containsAll(anyList())).thenReturn(true);
        assertThat(subject.containsAll(Collections.emptyList())).isEqualTo(true);
        verify(mockStore).containsAll(Collections.emptyList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll()
    {
        subject.addAll(Collections.singleton(""));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddArray()
    {
        subject.addAll(new String[]{});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAll()
    {
        subject.removeAll(Collections.singleton(""));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAll()
    {
        subject.retainAll(Collections.singleton(""));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear()
    {
        subject.clear();
    }

    @Test
    public void testCopy()
    {
        when(mockStore.copy()).thenReturn(mockStore);
        assertThat(subject.copy()).isSameAs(mockStore);
        verify(mockStore).copy();
    }

    @Test
    public void testSynchronizedStore()
    {
        when(mockStore.synchronizedStore()).thenReturn(mockStore);
        when(mockStore.unmodifiableStore()).thenReturn(mockStore);
        assertThat(subject.synchronizedStore()).isSameAs(mockStore);
        verify(mockStore).synchronizedStore();
        verify(mockStore).unmodifiableStore();
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