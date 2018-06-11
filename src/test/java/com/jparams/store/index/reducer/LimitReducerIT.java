package com.jparams.store.index.reducer;

import com.jparams.store.Store;
import com.jparams.store.index.Index;
import com.jparams.store.index.IndexDefinition;
import com.jparams.store.index.reducer.LimitReducer.Retain;
import com.jparams.store.memory.MemoryStore;
import com.jparams.store.model.Person;
import com.jparams.store.model.PersonBuilder;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LimitReducerIT
{
    private Store<Person> subject;
    private Index<Person> index;

    @Before
    public void setUp()
    {
        subject = new MemoryStore<>();
        index = subject.index("", IndexDefinition.withKeyMapping(Person::getFirstName).withReducer(new LimitReducer<>(2, Retain.OLDEST)));
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

        assertThat(index.get("Bob")).containsExactly(person3, person2);

        subject.remove(person2);

        assertThat(index.get("Bob")).containsExactly(person3, person1);

        subject.add(person2);

        assertThat(index.get("Bob")).containsExactly(person3, person1);
    }
}