import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String address;
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("hotel")
    private List<Room> rooms = new ArrayList<>();

    public Hotel(String name, String address) {
        this.name = name;
        this.address = address;
    }


}


