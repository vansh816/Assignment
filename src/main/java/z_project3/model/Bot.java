package z_project3.model;
import jakarta.persistence.*;
import lombok.Data;

    @Data
    @Entity
    @Table(name = "bots")
    public class Bot {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        @Column(name = "persona_description", columnDefinition = "TEXT")
        private String personaDescription;
    }

