package com.jparams.store.index;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

import com.jparams.store.KeyProvider;
import com.jparams.store.comparison.string.CaseInsensitiveComparisonPolicy;
import com.jparams.store.memory.MemoryReference;
import com.jparams.store.reference.Reference;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import static org.assertj.core.api.Assertions.assertThat;

public class ReferenceIndexTest
{
    private ReferenceIndex<String, String> subject;
    private AtomicReference<String> prefix;
    private MemoryReference<String> reference;
    private String value;
    private String indexName;

    @Before
    public void setUp() throws IndexCreationException
    {
        prefix = new AtomicReference<>("");

        final KeyProvider<Collection<String>, String> transformer = (obj) -> {
            if ("error".equals(prefix.get() + obj))
            {
                throw new RuntimeException("error");
            }

            return Collections.singletonList(prefix.get() + obj);
        };

        indexName = "index";
        subject = new ReferenceIndex<>(indexName, transformer, new CaseInsensitiveComparisonPolicy());

        value = "JParams";
        reference = new MemoryReference<>(value);
        subject.index(reference);
    }

    @Test
    public void testIndexDuplicate() throws IndexCreationException
    {
        subject.index(new MemoryReference<>("jPaRams"));
        assertThat(subject.get("JPARAMS")).containsExactlyInAnyOrder(value, "jPaRams");
        assertThat(subject.get("jparams")).containsExactlyInAnyOrder(value, "jPaRams");
    }

    @Test(expected = IndexCreationException.class)
    public void testIndexHandlesException() throws IndexCreationException
    {
        subject.index(new MemoryReference<>("error"));
    }

    @Test
    public void testGet()
    {
        assertThat(subject.getFirst("JPARAMS")).isEqualTo(value);
        assertThat(subject.getFirst("jparams")).isEqualTo(value);
        assertThat(subject.getFirst(123)).isNull();
        assertThat(subject.getFirst("k")).isNull();
        assertThat(subject.get("k")).isEmpty();
    }

    @Test
    public void testClear()
    {
        subject.clear();

        assertThat(subject.get("JPARAMS")).isEmpty();
        assertThat(subject.get("jparams")).isEmpty();
    }

    @Test
    public void testRemove() throws IndexCreationException
    {
        subject.index(new MemoryReference<>("jPaRams"));

        subject.removeIndex(reference);

        assertThat(subject.get("JPARAMS")).containsExactly("jPaRams");
        assertThat(subject.get("jparams")).containsExactly("jPaRams");
    }

    @Test
    public void testRemoveUnknownReference()
    {
        subject.removeIndex(new MemoryReference<>("random"));
    }

    @Test
    public void testReindex() throws IndexCreationException
    {
        prefix.set("a");

        subject.index(reference);

        assertThat(subject.getFirst("JPARAMS")).isNull();
        assertThat(subject.getFirst("jparams")).isNull();

        assertThat(subject.getFirst("aJPARAMS")).isEqualTo("JParams");
        assertThat(subject.getFirst("ajparams")).isEqualTo("JParams");
    }

    @Test
    public void testReindexHandlesKeyGenerationException() throws IndexCreationException
    {
        final MemoryReference<String> reference = new MemoryReference<>("rror");
        subject.index(reference);

        prefix.set("e");

        try
        {
            subject.index(reference);
            TestCase.fail("exception should have been thrown");
        }
        catch (final IndexCreationException e)
        {
            assertThat(subject.findFirst("rror")).isPresent();
        }
    }

    @Test(expected = IndexCreationException.class)
    public void testAddHandlesGetReferenceException() throws IndexCreationException
    {
        final Reference<String> reference = () -> {
            throw new RuntimeException();
        };
        subject.index(reference);
    }

    @Test
    public void testGetName()
    {
        assertThat(subject.getName()).isEqualTo(indexName);
    }

    @Test
    public void toToString()
    {
        assertThat(subject.toString()).isEqualTo("Index[name='index']");
    }

    @Test
    public void testFullCopy()
    {
        assertThat(subject.copy()).isEqualToComparingFieldByField(subject);
    }
}