package entities;

import DTO.PersonDTO;
import facades.PersonFacade;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class Tester {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();

        Person p1 = new Person("Phillip", "Andersen", "26352151");

        em.getTransaction().begin();
        em.persist(p1);
        em.getTransaction().commit();

        Query query = em.createNamedQuery("Person.getPersonById", Person.class);
        query.setParameter("id", 1);
        Person person = (Person) query.getSingleResult();

        System.out.println(person.getFirstName());

        PersonFacade fa = PersonFacade.getFacadeExample(emf);

        PersonDTO p3 = fa.addPerson("Hej", "Ole", "hshahsdsa");
        
        System.out.println(p3.getfName() +  p3.getlName() +   p3.getId() + p3.getPhone());

    }

}
