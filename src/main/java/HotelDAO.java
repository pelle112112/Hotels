import io.javalin.Javalin;

import java.util.*;

public class HotelDAO extends DAO {
    private List<Hotel> list = Arrays.asList(
            new Hotel(1, "Four Seasons", "Vesterbrogade 5", Arrays.asList(
                    new Room(1, 1, 1, 1000),
                    new Room(2, 1, 42, 400),
                    new Room(3, 1, 543, 2025)
            )),
            new Hotel(2, "Motel B", "Motorvej 4", Arrays.asList(
                    new Room(4, 2, 43, 525),
                    new Room(5, 2, 289, 690),
                    new Room(6, 2, 329, 1010)
            ))
    );
    private static HotelDAO hotelDAO;
    private HotelDAO(){

    }
    public static HotelDAO getInstance(){
        if(hotelDAO == null){
            hotelDAO = new HotelDAO();
        }
        return hotelDAO;
    }



    public void getRoomsByHotelId(Javalin app, int id){
       app.get("/api/hotels/{id}", ctx -> {
           ctx.json(ctx.bodyAsClass(Hotel.class));
       });
    }

    @Override
    public Iterable getall() {
        return list;
    }

    @Override

    public Object getById(int id) {
        return list.stream()
                .filter(o -> o.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void delete(int id) {
        List<Hotel> testList = new ArrayList<>();
        testList.addAll(list);
        testList.stream()
                .filter(Objects::nonNull)
                .filter(hotel -> hotel.getId() == id)
                .findFirst()
                .ifPresent(testList::remove);
        list = testList;
        }

}
