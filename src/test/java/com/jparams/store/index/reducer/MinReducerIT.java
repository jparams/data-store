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

public class MinReducerIT
{
    private Store<Person> subject;
    private Index<Person> index;

    @Before
    public void setUp()
    {
        subject = new MemoryStore<>();
        index = subject.index("", IndexDefinition.withKeyMapping(Person::getFirstName).withReducer(new MinReducer<>(Person::getId, true)));
    }

    @Test
    public void testReducer()
    {
        final Person person1 = PersonBuilder.aPerson().withId(1L).withFirstName("James").build();
        final Person person2 = PersonBuilder.aPerson().withId(2L).withFirstName("James").build();
        final Person person3 = PersonBuilder.aPerson().withId(3L).withFirstName("James").build();
        final Person person4 = PersonBuilder.aPerson().withId(4L).withFirstName("James").build();

        subject.add(person2);
        subject.add(person3);
        subject.add(person1);

        assertThat(index.getFirst("James")).isSameAs(person1);

        subject.remove(person1);

        assertThat(index.getFirst("James")).isSameAs(person2);

        subject.add(person4);
        assertThat(index.getFirst("James")).isSameAs(person2);

        subject.add(person1);
        assertThat(index.getFirst("James")).isSameAs(person1);
    }
}