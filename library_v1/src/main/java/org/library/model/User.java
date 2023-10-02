package org.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;

/**
 * entity for user
 */
@Entity
@Table
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;

    @Column(name = "is_admin")
    private boolean isAdmin;

    private String fullName;
    private boolean available;

    @OneToMany(mappedBy = "user")
    private List<Borrow> borrows;

    private String phone;
    private String email;
}
