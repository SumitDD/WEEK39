package facades;

import DTO.PersonDTO;
import DTO.PersonsDTO;
import utils.EMF_Creator;
import entities.Person;
import exceptions.PersonNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasProperty;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    Person p1 = new Person("Jon", "Bertelsen", "27394632");
    Person p2 = new Person("Phillip", "Andersen", "42913009");

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonFacade.getFacadeExample(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void testGetPersonById() throws PersonNotFoundException {
        long id = 14;
        try {
            PersonDTO person = facade.getPerson(id);
            String expectedFirstName = "";
            assertEquals(expectedFirstName, person.getfName(), "Expects Phillip");
            assert false;
        } catch (PersonNotFoundException e) {
            assert true;
        }

    }

    @Test
    public void testGetAllPersons() {
        PersonsDTO persons = facade.getAllPersons();
        assertEquals(2, persons.getAll().size(), "Expects the size of two");
        assertThat(persons.getAll(), everyItem(hasProperty("fName")));
    }

    @Test
    public void testGetCount() {
        long count = facade.getCount();
        assertEquals(count, 2);

    }

    @Test
    public void testDeletePerson() throws PersonNotFoundException {
        PersonDTO person = facade.deletePerson(p1.getId());
        String expName = "Jon";
        assertEquals(person.getfName(), expName);

    }

    @Test
    public void testEditPerson() throws PersonNotFoundException {
        PersonDTO person = new PersonDTO(p1);
        person.setfName("newFName");
        PersonDTO editPerson = facade.editPerson(person);
        String expFname = "newFName";
        assertEquals(editPerson.getfName(), expFname);

    }

    @Test
    public void testAddPerson() {
        PersonDTO person = facade.addPerson("Hans", "Larsen", "12345678");
        PersonsDTO persons = facade.getAllPersons();
        assertEquals(3, persons.getAll().size());
    }

    @Test
    public void negativeTestDeletePerson() throws PersonNotFoundException {
        long id = 14;
        try {
            PersonDTO person = facade.deletePerson(id);
            assertEquals("Jon", person.getfName());
            assert false;
        } catch (PersonNotFoundException e) {
            assert true;
        }

    }

    @Test
    public void negativeEditPerson() throws PersonNotFoundException {
        Person p3 = new Person("Sumit", "Dey", "2630");
        long id = 6;
        try {
            p3.setId(id);
            PersonDTO personDTO = new PersonDTO(p3);
            personDTO.setfName("Christian");
            PersonDTO personEdited = facade.editPerson(personDTO);
            assert false;
        } catch (PersonNotFoundException e) {
            assert true;
        }

    }

}
