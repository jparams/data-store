package com.jparams.store.index.reducer;

import com.jparams.store.Store;
import com.jparams.store.index.Index;
import com.jparams.store.index.IndexDefinition;
import com.jparams.store.memory.MemoryStore;
import com.jparams.store.model.Person;
import com.jparams.store.model.PersonBuilder;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link FirstReducer}
 */
public class FirstReducerIT
{
    private Store<Person> subject;
    private Index<Person> index;

    @Before
    public void setUp()
    {
        subject = new MemoryStore<>();
        index = subject.index("", IndexDefinition.withKeyMapping(Person::getFirstName).withReducer(new FirstReducer<>()));
    }

    @Test
    public void testReducer()
    {
        final Person person1 = PersonBuilder.aPerson().withId(1L).withFirstName("Bob").build();
        final Person person2 = PersonBuilder.aPerson().withId(2L).withFirstName("Bob").build();
        final Person person3 = PersonBuilder.aPerson().withId(3L).withFirstName("Bob").build();

        subject.add(person3);
        subject.add(person2);
        subject.add(person1);

        assertThat(index.getFirst("Bob")).isSameAs(person3);
    }
}