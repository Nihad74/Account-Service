package account.Entity;


import account.api.Role;
import jakarta.persistence.*;

@Entity
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private Role role;


    public Roles(String role) {
        this.role = Role.valueOf(role);
    }
    public Roles() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


    public Role getRole() {
        return role;
    }

    public String toString() {
        return role.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Roles) {
            return ((Roles) obj).getRole().equals(this.getRole());
        }
        return false;
    }
}

