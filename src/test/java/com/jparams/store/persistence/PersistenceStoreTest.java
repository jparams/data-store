package com.jparams.store.persistence;

import java.nio.file.Path;

import com.jparams.store.model.Person;
import com.jparams.store.model.PersonBuilder;
import com.jparams.store.persistence.manager.PersistenceManager;
import com.jparams.store.persistence.manager.serializable.SerializablePersistenceManager;

import org.assertj.core.util.Files;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link PersistenceStore}
 */
public class PersistenceStoreTest
{
    private PersistenceStore<Person> subject;
    private Person person1;

    /**
     * set up
     */
    @Before
    public void setUp()
    {
        final Path path = Files.newTemporaryFolder().toPath();
        final PersistenceManager<Person> personPersistenceManager = new SerializablePersistenceManager<>(Person.class, Person::getId, 0, path);
        subject = new PersistenceStore<>(personPersistenceManager);
        subject.index("firstName", Person::getFirstName);
        person1 = PersonBuilder.aPerson().withId(1L).withFirstName("John").withLastName("Johnson").build();
        subject.add(person1);
        subject.add(PersonBuilder.aPerson().withId(2L).withFirstName("Bob").withLastName("Roberts").build());
        System.gc();
    }

    /**
     * testAdd
     */
    @Test
    public void testAdd()
    {
        final Person person = PersonBuilder.aPerson().withId(1L).withFirstName("John").withLastName("Johnson").build();
        subject.add(person);

        assertThat(subject.getFirst("firstName", "John")).isEqualTo(person1);

        final Person person2 = subject.getFirst("firstName", "Bob");
        assertThat(person2.getFirstName()).isEqualTo("Bob");
    }
}