package tgbot.moviemoodbot.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "genre")
public class Genre {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "can't be empty")
    @Size(max = 30, message = "can't be more than 30 characters")
    private String name;
}
