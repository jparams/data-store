package com.jparams.store.comparison.string;

import com.jparams.store.Store;
import com.jparams.store.index.Index;
import com.jparams.store.memory.MemoryStore;
import com.jparams.store.model.Person;
import com.jparams.store.model.PersonBuilder;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CaseInsensitiveComparisonPolicyIT
{
    private Store<Person> subject;
    private Index<Person> index;

    @Before
    public void setUp()
    {
        subject = new MemoryStore<>();
        index = subject.index(Person::getFirstName, new CaseInsensitiveComparisonPolicy());
    }

    @Test
    public void testPolicy()
    {
        final Person person = PersonBuilder.aPerson().withFirstName("John").build();
        subject.add(person);
        assertThat(index.getFirst("JOHN")).isSameAs(person);
    }
}