package account.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"id","name", "lastname", "email", "roles"})

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "service_db")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    @JsonProperty("lastname")
    private String lastName;

    @NotEmpty
    @Email
    @Column(unique = true)
    @Pattern(regexp ="^[A-Za-z0-9+_.-]+@acme.com$")
    private String email;

    @Getter
    @NotEmpty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min =12, message = "Password length must be at least 12 chars minimum!")
    private String password;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    @JsonProperty(access= JsonProperty.Access.READ_ONLY)
    private List<Roles> roles = new ArrayList<>();

    @OneToMany(mappedBy ="user")
    @JsonIgnore
    private List<Salary> salaries = new ArrayList<>();

    @JsonIgnore
    @Column(columnDefinition = "boolean default false")
    private boolean isBlocked;

    @JsonIgnore
    @Column(columnDefinition = "integer default 0")
    private int failedLoginAttempts;


    public void setPassword(String password) {
        this.password = password;
    }

    public String toString (){
        return "Name: " + name + " Lastname: " + lastName + " Email: " + email + " Role " + roles.toString();
    }

}
