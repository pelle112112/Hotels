import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HotelController {
    public static Handler getAllHotels = ctx -> {
        HotelDAO dao = HotelDAO.getInstance();
        Iterable<Hotel> hotels = dao.getall();
        ctx.json(hotels);
    };

    public static Handler getById= ctx -> {

            String id = ctx.pathParam("id");
            HotelDAO dao = HotelDAO.getInstance();
            Hotel hotel = (Hotel) dao.getById(Integer.parseInt(id));
            if (hotel == null) {
                ctx.status(404);
                ctx.result("Hotel not found");
            } else {
                ctx.json(hotel);
            }
        };

    public static Handler create =  ctx -> {
            Hotel hotel = ctx.bodyAsClass(Hotel.class);
            HotelDAO dao = HotelDAO.getInstance();
            dao.create(hotel);
            ctx.status(201);
            ctx.json(hotel);
        };

    public static Handler update = ctx -> {
            String id = ctx.pathParam("id");
            Hotel hotel = ctx.bodyAsClass(Hotel.class);
            HotelDAO dao = HotelDAO.getInstance();
            Hotel hotelToUpdate = (Hotel) dao.getById(Integer.parseInt(id));
            if (hotelToUpdate == null) {
                ctx.status(404);
                ctx.result("Hotel not added");
            } else {
                hotelToUpdate.setName(hotel.getName());
                hotelToUpdate.setAddress(hotel.getAddress());
                hotelToUpdate.setRoomList(hotel.getRoomList());
                ctx.json(hotelToUpdate);
            }
        };

    public static Handler delete = ctx -> {
            String id = ctx.pathParam("id");
            HotelDAO dao = HotelDAO.getInstance();
            Hotel hotel = (Hotel) dao.getById(Integer.parseInt(id));
            if (hotel == null) {
                ctx.status(404);
                ctx.result("Hotel not found");
            } else {
                dao.delete(Integer.parseInt(id));
                ctx.status(204);
            }
        };
    }

