import io.javalin.Javalin;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Hibernate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class DAO <T>implements IDAO<T>{
    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();
    @Override
    public List<T> getall(String entityName) {
        List<T> list = new ArrayList<>();
       try(EntityManager em = emf.createEntityManager()){
           list= em.createQuery("SELECT o FROM "+entityName+" o").getResultList();
       }
       return list;
    }

    @Override
    public T getById(int id, Class<T> tclass) {
        try (EntityManager em = emf.createEntityManager()){
            return em.find(tclass, id);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void create(T o) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(o);
            em.getTransaction().commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void update(T t, int id) {
        try (EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.merge(t);
            em.getTransaction().commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean delete(int id, Class<T> tclass) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            T t = em.find(tclass, id);
            em.remove(t);
            em.getTransaction().commit();
        }
        try(EntityManager em = emf.createEntityManager()){
            T t = em.find(tclass, id);
            if(t == null){
                System.out.println("Object with id: "+id+" was deleted");
                return true;
            }
            else{
                System.out.println("Object with id: "+id+" was not deleted");
                return false;
            }
        }

    }


}
