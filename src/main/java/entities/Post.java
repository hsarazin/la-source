package entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Post {
    @Id
    @GeneratedValue
    private int id;
    private String nom;
    private String Categorie;
    private LocalDateTime date;
    @ManyToOne
    private Association association;

    public Post() {}

    public Post(String nom, String categorie, LocalDateTime date, Association association) {
        this.nom = nom;
        Categorie = categorie;
        this.date = date;
        this.association = association;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCategorie() {
        return Categorie;
    }

    public void setCategorie(String categorie) {
        Categorie = categorie;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }
}
