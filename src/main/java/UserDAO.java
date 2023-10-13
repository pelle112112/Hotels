import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;

import java.util.List;

public class UserDAO {

    private static UserDAO userDAO;
    private UserDAO(){

    }
    public static UserDAO getInstance(){
        if(userDAO == null){
            userDAO = new UserDAO();
        }
        return userDAO;
    }

    EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();

    public User createUser(String username, String password){
        User user;
        return user = new User(username, password);
    }

    public User getUserByusername(String username){
        try(EntityManager em = emf.createEntityManager()){
            User user = em.find(User.class, username);
            if(user == null) {
                throw new EntityNotFoundException("No user found with username: " + username);
            }
            user.getRoles().size();
            return user;
        }
    }
    public List<User> getAllUsers(){
        List<User> list;
        try(EntityManager em = emf.createEntityManager()){
            list= em.createQuery("SELECT o FROM User o").getResultList();
        }
        return list;
    }

    public List<Role> getAllRoles(){
        List<Role> list;
        try(EntityManager em = emf.createEntityManager()){
            list= em.createQuery("SELECT o FROM Role o").getResultList();
        }
        return list;
    }
    public void addRoleToUser(String username, String roleName){
        try(EntityManager em = emf.createEntityManager()){
            User user = em.find(User.class, username);
            Role role = em.find(Role.class, roleName);
            user.getRoles().add(role);
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        }
    }
    public boolean deleteUser(String username){
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            User user = em.find(User.class, username);
            em.remove(user);
            em.getTransaction().commit();
        }
        try(EntityManager em = emf.createEntityManager()){
            User user = em.find(User.class, username);
            if(user == null){
                return true;
            }
            else{
                return false;
            }
        }
    }
    public boolean authenticateUser(String username, String password) {
        User user = getUserByusername(username);
        return user.verifyPassword(password);

    }


    public static void main(String[] args) {
        //Create a new user and encrypt the password
        User usernew = new User("user1", "test123");
        //check the password
        System.out.println("Password for user: " + usernew.getPassword());
    }

}
