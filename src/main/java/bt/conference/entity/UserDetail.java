package bt.conference.entity;

import com.fierhub.database.annotations.Column;
import com.fierhub.database.annotations.Id;
import com.fierhub.database.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserDetail {
    @Id
    @Column(name = "userId")
    long userId;

    @Column(name = "firstName")
    String firstName;

    @Column(name="lastName")
    String lastName;
}
