import io.javalin.Javalin;

public class Routes {
    //This class contains all the routes for the application and implements the following routes with json

    public static void main(String[] args) {
        //GET /api/hotels
        Javalin app = Javalin.create().start(7000);
        app.get("/api/hotels", HotelController.getAllHotels);
        //GET /api/hotels/{id}
       app.get("/api/hotel/{id}", HotelController.getById);
        //POST /api/hotel
        app.post("/api/hotel", HotelController.create);
        //PUT /api/hotel/{id}
        app.put("/api/hotel/{id}", HotelController.update);
        //DELETE /api/hotel/{id}
        app.delete("/api/hotel/{id}", HotelController.delete);

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
