package z_project3.model;
import jakarta.persistence.*;
import lombok.Data;

    @Data
    @Entity
    @Table(name = "users")
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String username;

        @Column(name = "is_premium")
        private boolean isPremium;

}
