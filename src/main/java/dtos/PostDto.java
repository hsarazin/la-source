package dtos;

import entities.Association;

import java.time.LocalDateTime;

public class PostDto {

    private String nom;
    private String categorie;

    public PostDto() {}

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getCategorie() {
        return categorie;
    }
}
