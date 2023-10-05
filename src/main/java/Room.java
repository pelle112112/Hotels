import jakarta.persistence.*;
import jakarta.persistence.criteria.Fetch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Hotel hotel;
    private int roomNumber;
    private int price;

    public Room(int roomNumber, int price) {
        this.roomNumber = roomNumber;
        this.price = price;
    }
}
