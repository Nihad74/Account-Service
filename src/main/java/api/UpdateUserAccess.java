package api;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserAccess {
    @NotEmpty
    private String user;

    @NotEmpty
    private String operation;
}
