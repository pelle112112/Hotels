import io.javalin.Javalin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Hibernate;

import java.util.Arrays;
import java.util.List;

public class Routes {
    //This class contains all the routes for the application and implements the following routes with json

    public static void main(String[] args) {
        //Create a list of hotels
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();
        /*
        try(EntityManager em = emf.createEntityManager()){

            //em.getTransaction().begin();
            //em.createNativeQuery("INSERT INTO public.hotel (address,name) VALUES ('address1','Motel B');").executeUpdate();
            //em.createNativeQuery("INSERT INTO public.hotel (address,name) VALUES ('address2','Four seasons');").executeUpdate();
            //em.createNativeQuery("INSERT INTO public.room (price,roomnumber,hotel_id) VALUES ('1000','42','1');").executeUpdate();
            //em.createNativeQuery("INSERT INTO public.room (price,roomnumber,hotel_id) VALUES ('400','43','2');").executeUpdate();
            //em.getTransaction().commit();

        }

         */
        //GET /api/hotels
        HotelDAODB daodb = HotelDAODB.getInstance();
        List<Hotel> hotels = daodb.getall("Hotel");
        Javalin app = Javalin.create().start(7000);
        app.get("/api/hotels", HotelController.getAllHotels());
        //GET /api/hotels/{id}
        app.get("/api/hotel/{id}", HotelController.getById());
        //POST /api/hotel
        app.post("/api/hotel", HotelController.create());
        //PUT /api/hotel/{id}
        app.put("/api/hotel/{id}", HotelController.update);
        //DELETE /api/hotel/{id}
        app.delete("/api/hotel/{id}", HotelController.delete);
        //GET /api/hotel/{id}/rooms
        app.get("/api/hotel/{id}/rooms", RoomController.getAllRooms());
        //GET /api/hotel/{id}/rooms/{roomNumber}
        app.get("/api/hotel/{id}/rooms/{roomNumber}", RoomController.getById());
        //POST /api/hotel/{id}/rooms
        app.post("/api/hotel/{id}/rooms", RoomController.create());
        //PUT /api/hotel/{id}/rooms/{roomNumber}
        app.put("/api/hotel/{id}/rooms/{roomNumber}", RoomController.update);
        //DELETE /api/hotel/{id}/rooms/{roomNumber}
        app.delete("/api/hotel/{id}/rooms/{roomNumber}", RoomController.delete);

        //Implement app.error() to handle 404 nnot found errors
        app.error(404, ctx -> {
            ctx.result("Page not found");
        });
        //Implement app.exception() to handle illegalstateException when posting or updating a hotel or room with incorrect JSOn request body
        app.exception(IllegalStateException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.result("Illegal state exception");
        });


    }

}
