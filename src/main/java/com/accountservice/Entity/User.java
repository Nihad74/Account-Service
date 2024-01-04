package com.accountservice.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "userDB")
public class User {
    @NotEmpty
    private String name;

    @NotEmpty
    @JsonProperty("lastname")
    private String lastName;

    @NotEmpty
    @Email
    @Pattern(regexp ="^[A-Za-z0-9+_.-]+@acme.com$")
    private String email;

    @NotEmpty
    @JsonIgnore
    private String password;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

}
