import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HotelController {
    public static Handler getAllHotels (){
        return ctx -> {
            HotelDAODB daodb = HotelDAODB.getInstance();
            List<Hotel> hotels = daodb.getall("Hotel");
            ctx.json(hotels);
        };
    }


    public static Handler getById (){

            return ctx -> {
            String id = ctx.pathParam("id");
            HotelDAODB daodb = HotelDAODB.getInstance();
            Hotel hotel = (Hotel) daodb.getById(Integer.parseInt(id), Hotel.class);
            if (hotel == null) {
                ctx.status(404);
                ctx.result("Hotel not found");
            } else {
                ctx.json(hotel);
            }
        };
    }

    public static Handler create (){
            return ctx -> {
            Hotel hotel = ctx.bodyAsClass(Hotel.class);
            HotelDAODB daodb = HotelDAODB.getInstance();
            //Create a new Hotel Enitity
            Hotel newHotel = new Hotel(ctx.pathParam("name"), ctx.pathParam("address"));
            daodb.create(newHotel);
            ctx.status(201);
            ctx.json(hotel);
        };
    }

    public static Handler update = ctx -> {
            String id = ctx.pathParam("id");
            Hotel hotel = ctx.bodyAsClass(Hotel.class);
            HotelDAODB daodb = HotelDAODB.getInstance();
            Hotel hotelToUpdate = (Hotel) daodb.getById(Integer.parseInt(id), Hotel.class);
            if (hotelToUpdate == null) {
                ctx.status(404);
                ctx.result("Hotel not added");
            } else {
                hotelToUpdate.setName(hotel.getName());
                hotelToUpdate.setAddress(hotel.getAddress());
                hotelToUpdate.setRooms(hotel.getRooms());
                ctx.json(hotelToUpdate);
            }
        };

    public static Handler delete = ctx -> {
            String id = ctx.pathParam("id");
            HotelDAODB daodb = HotelDAODB.getInstance();
            Hotel hotel = (Hotel) daodb.getById(Integer.parseInt(id), Hotel.class);
            if (hotel == null) {
                ctx.status(404);
                ctx.result("Hotel not found");
            } else {
                daodb.delete(Integer.parseInt(id), Hotel.class);
                ctx.status(204);
            }
        };


    }

