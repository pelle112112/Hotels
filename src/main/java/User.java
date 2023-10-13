import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    private String username;
    private String password;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_username",referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"))
    Set<Role> roles;

    //Generate a new salt based on the weather
    public void setNewSalt(){
        this.password = BCrypt.hashpw(this.password, BCrypt.gensalt());
    }
    public User(String username, String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        this.username = username;
        this.password = hashed;
    }
    public boolean verifyPassword(String pw){
        return BCrypt.checkpw(pw, this.password);
    }
    public String getRolesAsString(){
        return roles.stream().map(role->role.getRole()).collect(Collectors.toSet()).toString();
    }

    public User(String username, Set<Role> roles) {
        this.username = username;
        this.roles = roles;
    }
}


