package entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String nom;
    private String categorie;
    @Basic
    private LocalDateTime date;

    @ManyToOne
    private Association association;

    @ManyToMany
    private List<Member> demands;

    public Post() {}

    public Post(String nom, String categorie, Association association) {
        this.nom = nom;
        this.categorie = categorie;
        this.date = LocalDateTime.now();
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
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
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

    public List<Member> getDemands(){ return demands; }

    public void setDemands(List<Member> demands) {this.demands = demands; }
}
