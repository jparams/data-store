package com.jparams.store;

import java.util.Collections;

import com.jparams.store.index.Index;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import nl.jqno.equalsverifier.EqualsVerifier;
import static org.assertj.core.api.Assertions.assertThat;
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

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexWithNameAndProviderAndComparison()
    {
        subject.index(null, null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexWithNameAndProvider()
    {
        subject.index("", null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIndexProvider()
    {
        subject.index(null);
    }

    @Test
    public void testGetIndex()
    {
        when(mockStore.getIndex(anyString())).thenReturn(mockIndex);

        assertThat(subject.getIndex("abc")).isSameAs(mockIndex);

        verify(mockStore).getIndex("abc");
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
    }

    @Test
    public void testToArray1()
    {
    }

    @Test
    public void testAdd()
    {
    }

    @Test
    public void testRemove()
    {
    }

    @Test
    public void testContainsAll()
    {
    }

    @Test
    public void testAddAll()
    {
    }

    @Test
    public void testRemoveAll()
    {
    }

    @Test
    public void testRetainAll()
    {
    }

    @Test
    public void testClear()
    {
    }

    @Test
    public void testCopy()
    {
    }

    @Test
    public void equalsAndHashCodeContract()
    {
        EqualsVerifier.forClass(UnmodifiableStore.class).usingGetClass().verify();
    }
}