package bt.conference.model;

import in.bottomhalf.ps.database.annotations.Column;
import in.bottomhalf.ps.database.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    String email;

    long userId;

    String firstName;

    String lastName;

    String token;
}
