import io.javalin.http.Handler;

import java.util.List;

public class RoomController {
    public static Handler getAllRooms (){
           return ctx -> {
                ctx.json(ctx.bodyAsClass(Room.class));
            };
    }
    public static Handler getById (List <Room> roomList){
        return ctx -> {
            String id = ctx.pathParam("id");
            Room room = roomList.stream().filter(r -> r.getId() == Integer.parseInt(id)).findFirst().orElse(null);
            if (room == null) {
                ctx.status(404);
                ctx.result("Room not found");
            } else {
                ctx.json(room);
            }
        };
    }
    public static Handler create (List <Room> roomList){
        return ctx -> {
            Room room = ctx.bodyAsClass(Room.class);
            roomList.add(room);
            ctx.status(201);
            ctx.json(room);
        };
    }
    public static Handler update (List<Room> roomList){
        return ctx -> {
            String id = ctx.pathParam("id");
            Room room = ctx.bodyAsClass(Room.class);
            Room roomToUpdate = roomList.stream().filter(r -> r.getId() == Integer.parseInt(id)).findFirst().orElse(null);
            if (roomToUpdate == null) {
                ctx.status(404);
                ctx.result("Room not added");
            } else {
                roomToUpdate.setRoomNumber(room.getRoomNumber());
                roomToUpdate.setPrice(room.getPrice());
                roomToUpdate.setHotelId(room.getHotelId());
                ctx.json(roomToUpdate);
            }
        };
    }

    public static Handler delete (List<Room> roomList){
        return ctx -> {
            String id = ctx.pathParam("id");
            Room room = roomList.stream().filter(r -> r.getId() == Integer.parseInt(id)).findFirst().orElse(null);
            if (room == null) {
                ctx.status(404);
                ctx.result("Room not found");
            } else {
                roomList.remove(room);
                ctx.status(204);
            }
        };
    }
}
