package tgbot.moviemoodbot.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "botUser")
public class BotUser {

    @Id
    private Long chatId;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "registeredAt")
    @NotNull(message = "can't be null")
    private Timestamp registeredAt;

    @ManyToMany
    @JoinTable(
            name = "film_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "film_id")
    )
    @ToString.Exclude
    private List<Film> films;
}
