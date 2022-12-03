package dtos;

import entities.Association;

import java.time.LocalDateTime;

public class PostDto {

    private String nom;
    private String categorie;

    private Association association;

    private boolean selected;

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

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public void select(boolean selection){
        selected=selection;
    }

    public boolean isSelected(){
        return this.selected;
    }

    @Override
    public String toString() {
        return "PostDto{" +
                "nom='" + nom + '\'' +
                ", categorie='" + categorie + '\'' +
                ", association=" + association +
                ", selected=" + selected +
                '}';
    }
}
