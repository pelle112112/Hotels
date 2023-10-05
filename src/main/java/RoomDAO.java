public class RoomDAO extends DAO{
    private static RoomDAO roomDAO;
    private RoomDAO(){

    }
    public static RoomDAO getInstance(){
        if(roomDAO == null){
            roomDAO = new RoomDAO();
        }
        return roomDAO;
    }
}
