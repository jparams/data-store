package com.jparams.store;

public class MemoryReferenceManagerTest
{
    //    private MemoryReferenceManager<String> subject;
    //
    //    @Before
    //    public void setUp()
    //    {
    //        subject = new MemoryReferenceManager<>();
    //    }
    //
    //    @Test
    //    public void testAdd()
    //    {
    //        final Reference<String> reference = subject.on("abc");
    //        assertThat(reference.get()).isEqualTo("abc");
    //    }
    //
    //    @Test
    //    public void testAddDuplicate()
    //    {
    //        final Reference<String> reference1 = subject.on("abc");
    //        final Reference<String> reference2 = subject.on("abc");
    //        assertThat(reference1).isSameAs(reference2);
    //        assertThat(subject.size()).isEqualTo(1);
    //    }
    //
    //    @Test
    //    public void testClear()
    //    {
    //        subject.on("abc");
    //        subject.clear();
    //
    //        final Optional<Reference<String>> reference = subject.findReference("abc");
    //        assertThat(reference).isEmpty();
    //        assertThat(subject.size()).isEqualTo(0);
    //    }
    //
    //    @Test
    //    public void testFindReference()
    //    {
    //        final Reference<String> reference = subject.on("abc");
    //        assertThat(subject.findReference("abc")).hasValue(reference);
    //        assertThat(subject.findReference("abcd")).isEmpty();
    //    }
    //
    //    @Test
    //    public void testRemoveReference()
    //    {
    //        final Reference<String> reference = subject.on("abc");
    //        assertThat(subject.remove("abc")).isEqualTo(reference);
    //        assertThat(subject.remove("abcd")).isNull();
    //    }
    //
    //    @Test
    //    public void testGetReferences()
    //    {
    //        subject.on("abc");
    //
    //        final Collection<Reference<String>> references = subject.getReferences();
    //        assertThat(references).usingFieldByFieldElementComparator().containsExactly(new MemoryReference<>("abc"));
    //
    //        references.clear();
    //        assertThat(subject.getReferences()).isEmpty();
    //    }
    //
    //    @Test
    //    public void testCopy()
    //    {
    //        subject.on("abc");
    //
    //        final ReferenceManager<String> referenceManager = subject.copy();
    //        assertThat(referenceManager).isNotSameAs(subject);
    //        assertThat(referenceManager).isEqualToComparingFieldByFieldRecursively(subject);
    //    }
}