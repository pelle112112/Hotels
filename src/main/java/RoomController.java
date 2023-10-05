import io.javalin.http.Handler;

import java.util.List;

public class RoomController {
    public static Handler getAllRooms (){
           return ctx -> {
               RoomDAO daodb = RoomDAO.getInstance();
               List<Room> rooms = daodb.getall("Room");
               ctx.json(rooms);
            };
    }
    public static Handler getById (){

        return ctx -> {
            String id = ctx.pathParam("id");
            RoomDAO daodb = RoomDAO.getInstance();
            Room room = (Room) daodb.getById(Integer.parseInt(id), Room.class);
            if (room == null) {
                ctx.status(404);
                ctx.result("Room not found");
            } else {
                ctx.json(room);
            }
        };
    }

    public static Handler create (){
        return ctx -> {
            Room room = ctx.bodyAsClass(Room.class);
            RoomDAO daodb = RoomDAO.getInstance();
            //Create a new room Enitity
            int roomNumber = Integer.parseInt(ctx.pathParam("roomnumber"));
            int price = Integer.parseInt(ctx.pathParam("price"));
            Room newRoom = new Room(roomNumber, price);
            daodb.create(newRoom);
            ctx.status(201);
            ctx.json(room);
        };
    }

    public static Handler update = ctx -> {
        String id = ctx.pathParam("id");
        Room room = ctx.bodyAsClass(Room.class);
        RoomDAO daodb = RoomDAO.getInstance();
        Room room1 = (Room) daodb.getById(Integer.parseInt(id), Room.class);
        if (room1 == null) {
            ctx.status(404);
            ctx.result("Room not added");
        } else {
            room1.setPrice(room.getPrice());
            room1.setRoomNumber(room.getRoomNumber());
            ctx.json(room1);

        }
    };

    public static Handler delete = ctx -> {
        String id = ctx.pathParam("id");
        RoomDAO daodb = RoomDAO.getInstance();
        Room room = (Room) daodb.getById(Integer.parseInt(id), Room.class);
        if (room == null) {
            ctx.status(404);
            ctx.result("Room not found");
        } else {
            daodb.delete(Integer.parseInt(id), Room.class);
            ctx.status(204);
        }
    };
}
