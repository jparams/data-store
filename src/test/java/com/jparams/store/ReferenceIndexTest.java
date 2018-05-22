package com.jparams.store;

public class ReferenceIndexTest
{
    //    private ReferenceIndex<String> subject;
    //    private AtomicReference<String> prefix;
    //    private MemoryReference<String> reference;
    //    private KeyProvider<String, Object> transformer;
    //    private String value;
    //    private String indexName;
    //
    //    @Before
    //    public void setUp() throws IndexCreationException
    //    {
    //        prefix = new AtomicReference<>("");
    //        transformer = (obj) -> {
    //            if ("error".equals(prefix.get() + obj))
    //            {
    //                throw new RuntimeException("error");
    //            }
    //
    //            return Key.onEach(prefix.get() + obj.toUpperCase(), prefix.get() + obj.toLowerCase());
    //        };
    //
    //        indexName = "index";
    //        subject = new ReferenceIndex<>(indexName, transformer);
    //
    //        value = "JParams";
    //        reference = new MemoryReference<>(value);
    //        subject.index(reference);
    //    }
    //
    //    @Test
    //    public void testIndexDuplicate() throws IndexCreationException
    //    {
    //        subject.index(new MemoryReference<>("jPaRams"));
    //        assertThat(subject.get("JPARAMS")).containsExactlyInAnyOrder(value, "jPaRams");
    //        assertThat(subject.get("jparams")).containsExactlyInAnyOrder(value, "jPaRams");
    //    }
    //
    //    @Test(expected = IndexCreationException.class)
    //    public void testIndexHandlesException() throws IndexCreationException
    //    {
    //        subject.index(new MemoryReference<>("error"));
    //    }
    //
    //    @Test
    //    public void testGet()
    //    {
    //        assertThat(subject.getFirst("JPARAMS")).isEqualTo(value);
    //        assertThat(subject.getFirst("jparams")).isEqualTo(value);
    //        assertThat(subject.getFirst("k")).isNull();
    //        assertThat(subject.get("k")).isEmpty();
    //    }
    //
    //    @Test
    //    public void testRemove() throws IndexCreationException
    //    {
    //        subject.index(new MemoryReference<>("jPaRams"));
    //
    //        subject.removeIndex(reference);
    //
    //        assertThat(subject.get("JPARAMS")).containsExactly("jPaRams");
    //        assertThat(subject.get("jparams")).containsExactly("jPaRams");
    //    }
    //
    //    @Test
    //    public void testRemoveUnknownReference()
    //    {
    //        subject.removeIndex(new MemoryReference<>("random"));
    //    }
    //
    //    @Test
    //    public void testReindex() throws IndexCreationException
    //    {
    //        prefix.set("a");
    //
    //        subject.index(reference);
    //
    //        assertThat(subject.getFirst("JPARAMS")).isNull();
    //        assertThat(subject.getFirst("jparams")).isNull();
    //
    //        assertThat(subject.getFirst("aJPARAMS")).isEqualTo("JParams");
    //        assertThat(subject.getFirst("ajparams")).isEqualTo("JParams");
    //    }
    //
    //    @Test
    //    public void testReindexHandlesKeyGenerationException() throws IndexCreationException
    //    {
    //        final MemoryReference<String> reference = new MemoryReference<>("rror");
    //        subject.index(reference);
    //
    //        prefix.set("e");
    //
    //        try
    //        {
    //            subject.index(reference);
    //            fail("exception should have been thrown");
    //        }
    //        catch (final IndexCreationException e)
    //        {
    //            assertThat(subject.findFirst("rror")).isPresent();
    //        }
    //    }
    //
    //    @Test(expected = IndexCreationException.class)
    //    public void testAddHandlesGetReferenceException() throws IndexCreationException
    //    {
    //        final Reference<String> reference = () -> {
    //            throw new RuntimeException();
    //        };
    //        subject.index(reference);
    //    }
    //
    //    @Test
    //    public void testGetName()
    //    {
    //        assertThat(subject.getName()).isEqualTo(indexName);
    //    }
    //
    //    @Test
    //    public void testFullCopy()
    //    {
    //        assertThat(subject.copy()).isEqualToComparingFieldByField(subject);
    //    }
}