package com.jparams.store.reference;

import java.util.Collection;
import java.util.Optional;

import com.jparams.store.identity.IdentityProvider;
import com.jparams.store.memory.MemoryReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultReferenceManagerTest
{
    private DefaultReferenceManager<String> subject;

    @Mock
    private IdentityProvider mockIdentityProvider;

    @Mock
    private ReferenceFactory<String> mockReferenceFactory;

    @Before
    public void setUp()
    {
        subject = new DefaultReferenceManager<>(mockIdentityProvider, mockReferenceFactory);
        when(mockIdentityProvider.getIdentity(any())).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(mockReferenceFactory.createReference(any())).thenAnswer(invocation -> new MemoryReference<>(invocation.getArguments()[0]));
    }

    @Test
    public void testAddHandlesNull()
    {
        final Reference<String> reference = subject.add(null);
        assertThat(reference).isNull();
    }

    @Test
    public void testAdd()
    {
        final Reference<String> reference = subject.add("abc");
        assertThat(reference.get()).isEqualTo("abc");
    }

    @Test
    public void testAddDuplicate()
    {
        final Reference<String> reference1 = subject.add("abc");
        final Reference<String> reference2 = subject.add("abc");
        assertThat(reference1).isSameAs(reference2);
        assertThat(subject.size()).isEqualTo(1);
    }

    @Test
    public void testClear()
    {
        subject.add("abc");
        subject.clear();

        final Optional<Reference<String>> reference = subject.findReference("abc");
        assertThat(reference).isEmpty();
        assertThat(subject.size()).isEqualTo(0);
    }

    @Test
    public void testFindReference()
    {
        final Reference<String> reference = subject.add("abc");
        assertThat(subject.findReference("abc")).hasValue(reference);
        assertThat(subject.findReference("abcd")).isEmpty();
    }

    @Test
    public void testFindReferenceHandlesNull()
    {
        assertThat(subject.findReference(null)).isEmpty();
    }

    @Test
    public void testSize()
    {
        subject.add("abc");
        assertThat(subject.size()).isEqualTo(1);
    }

    @Test
    public void testRemoveReference()
    {
        final Reference<String> reference = subject.add("abc");
        assertThat(subject.remove("abc")).isEqualTo(reference);
        assertThat(subject.remove("abcd")).isNull();
    }

    @Test
    public void testRemoveHandlesNull()
    {
        assertThat(subject.remove(null)).isNull();
    }

    @Test
    public void testGetReferences()
    {
        subject.add("abc");

        final Collection<Reference<String>> references = subject.getReferences();
        assertThat(references).usingFieldByFieldElementComparator().containsExactly(new MemoryReference<>("abc"));

        references.clear();
        assertThat(subject.getReferences()).isEmpty();
    }

    @Test
    public void testCopy()
    {
        subject.add("abc");

        final ReferenceManager<String> referenceManager = subject.copy();
        assertThat(referenceManager).isNotSameAs(subject);
        assertThat(referenceManager).isEqualToComparingFieldByFieldRecursively(subject);
    }
}