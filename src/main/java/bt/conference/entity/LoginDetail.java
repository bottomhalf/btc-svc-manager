package bt.conference.entity;

import in.bottomhalf.ps.database.annotations.Column;
import in.bottomhalf.ps.database.annotations.Id;
import in.bottomhalf.ps.database.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "login")
public class LoginDetail {
    @Id
    @Column(name = "loginId")
    long loginId;

    @Column(name = "email")
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "userId")
    long userId;

    @Column(name= "firstName")
    String firstName;

    @Column(name= "lastName")
    String lastName;
}
