package com.jparams.store;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReferenceIndexTest
{
    private ReferenceIndex<String> subject;
    private AtomicReference<String> prefix;
    private MemoryReference<String> reference;
    private Transformer<String, Object> transformer;
    private String value;
    private String indexName;

    @Before
    public void setUp()
    {
        prefix = new AtomicReference<>("");
        transformer = (obj) -> Keys.create(prefix.get() + obj.toUpperCase(), prefix.get() + obj.toLowerCase());
        indexName = "index";
        subject = new ReferenceIndex<>(indexName, transformer);

        value = "JParams";
        reference = new MemoryReference<>(value);
        subject.add(reference);
    }

    @Test
    public void testAddDuplicate()
    {
        subject.add(new MemoryReference<>("jPaRams"));
        assertThat(subject.get("JPARAMS")).containsExactlyInAnyOrder(value, "jPaRams");
        assertThat(subject.get("jparams")).containsExactlyInAnyOrder(value, "jPaRams");
    }

    @Test
    public void testGet()
    {
        assertThat(subject.getFirst("JPARAMS")).isEqualTo(value);
        assertThat(subject.getFirst("jparams")).isEqualTo(value);
        assertThat(subject.getFirst("k")).isNull();
        assertThat(subject.get("k")).isEmpty();
    }

    @Test
    public void testRemove()
    {
        subject.add(new MemoryReference<>("jPaRams"));

        subject.remove(reference);

        assertThat(subject.get("JPARAMS")).containsExactly("jPaRams");
        assertThat(subject.get("jparams")).containsExactly("jPaRams");
    }

    @Test
    public void testRemoveUnknownReference()
    {
        subject.remove(new MemoryReference<>("random"));
    }

    @Test
    public void testReindex()
    {
        prefix.set("a");

        subject.reindex(reference);

        assertThat(subject.getFirst("JPARAMS")).isNull();
        assertThat(subject.getFirst("jparams")).isNull();

        assertThat(subject.getFirst("aJPARAMS")).isEqualTo("JParams");
        assertThat(subject.getFirst("ajparams")).isEqualTo("JParams");
    }

    @Test
    public void testGetName()
    {
        assertThat(subject.getName()).isEqualTo(indexName);
    }

    @Test
    public void testEmptyCopy()
    {
        assertThat(subject.copy(false)).isEqualToComparingFieldByField(new ReferenceIndex<>(indexName, transformer));
    }

    @Test
    public void testFullCopy()
    {
        assertThat(subject.copy(true)).isEqualToComparingFieldByField(subject);
    }
}