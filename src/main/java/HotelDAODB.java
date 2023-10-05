import jakarta.persistence.EntityManager;

import java.util.List;

public class HotelDAODB <Hotel> extends DAO<Hotel>{
    private static HotelDAODB hotelDAODB;
    private HotelDAODB(){

    }
    public static HotelDAODB getInstance(){
        if(hotelDAODB == null){
            hotelDAODB = new HotelDAODB();
        }
        return hotelDAODB;
    }

    @Override
    public List<Hotel> getall(String entityName) {
        return super.getall(entityName);
    }

    @Override
    public Hotel getById(int id, Class<Hotel> tclass) {
        return super.getById(id, tclass);
    }

    @Override
    public void create(Hotel o) {
        super.create(o);
    }

    @Override
    public void update(Hotel hotel, int id) {
        super.update(hotel, id);
    }

    @Override
    public boolean delete(int id, Class<Hotel> tclass) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            //Native query that removes all rooms with the hotel id
            em.createNativeQuery("DELETE FROM public.room WHERE hotel_id = "+id+";").executeUpdate();
            //Check that the rooms are deleted
            List<Room> rooms = em.createQuery("SELECT o FROM Room o WHERE o.hotel.id = "+id).getResultList();
            if(rooms.size() == 0){
                System.out.println("Rooms with hotel id: "+id+" was deleted");
            }
            else{
                System.out.println("Rooms with hotel id: "+id+" was not deleted");
            }
            Hotel hotel = em.find(tclass, id);
            em.remove(hotel);
            em.getTransaction().commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try(EntityManager em = emf.createEntityManager()){
            Hotel hotel = em.find(tclass, id);
            if(hotel == null){
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
