package dtos;

import entities.Association;

public class PostDto {

    private int id;
    private String nom;
    private String categorie;

    private Association association;

    private boolean selected;

    public PostDto() {}

    public PostDto(int id, String nom, String categorie) {
        this.id = id;
        this.nom = nom;
        this.categorie = categorie;
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

    public void select(boolean selection){this.selected=selection;
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
