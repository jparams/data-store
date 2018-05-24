package com.jparams.store;

import java.util.Collection;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link MemoryReferenceManager}
 */
public class MemoryReferenceManagerTest
{

    private MemoryReferenceManager<String> subject;

    /**
     * set up
     */
    @Before
    public void setUp()
    {
        subject = new MemoryReferenceManager<>();
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