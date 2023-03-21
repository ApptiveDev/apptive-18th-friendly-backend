package apptive.team1.friendly.domain.user.data.entity.profile;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Nation {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
