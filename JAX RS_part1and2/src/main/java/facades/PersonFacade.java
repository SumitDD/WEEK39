package facades;

import DTO.PersonDTO;
import DTO.PersonsDTO;
import entities.Person;
import exceptions.PersonNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    public static PersonFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public long getCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long count = (long) em.createQuery("SELECT COUNT(p) FROM Person p").getSingleResult();
            return count;
        } finally {
            em.close();
        }

    }

    @Override
    public PersonDTO addPerson(String fName, String lName, String phone) {
        EntityManager em = emf.createEntityManager();

        try {
            Person p = new Person(fName, lName, phone);

            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();

            return new PersonDTO(p);

        } finally {

            em.close();
        }
    }

    @Override
    public PersonDTO deletePerson(long id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();

        try {
            Person person = em.find(Person.class, id);
            if (person == null) {
                throw new PersonNotFoundException("Person does not exist");
            }
            em.getTransaction().begin();
            em.remove(person);
            em.getTransaction().commit();

            return new PersonDTO(person);
        } finally {
            em.close();
        }

    }

    @Override
    public PersonDTO getPerson(long id) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();

        try {
            Person person = em.find(Person.class, id);
            if(id==13){
                System.out.println(13/0);
            }
            if (person == null) {
                throw new PersonNotFoundException("Person does not exist");
            }

            return new PersonDTO(person);
        } finally {
            em.close();
        }
    }

    @Override
    public PersonsDTO getAllPersons() {
        EntityManager em = emf.createEntityManager();

        try {
            Query query = em.createNamedQuery("Person.getAllPersons", Person.class);
            List<Person> persons = query.getResultList();

            return new PersonsDTO(persons);

        } finally {
            em.close();
        }

    }

    @Override
    public PersonDTO editPerson(PersonDTO p) throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        Person person = em.find(Person.class, p.getId());
       
        if (person == null) {
            throw new PersonNotFoundException("Person does not exist");
        }
       

        person.setFirstName(p.getfName());
        person.setLastName(p.getlName());
        person.setPhone(p.getPhone());

        try {
            em.getTransaction().begin();

            em.merge(person);

            em.getTransaction().commit();

            return new PersonDTO(person);

        } finally {
            em.close();

        }

    }

}
