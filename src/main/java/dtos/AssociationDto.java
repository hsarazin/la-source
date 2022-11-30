package dtos;

import entities.Member;

public class AssociationDto {
    private int id;
    private String nom;
    private Member contactMember;


    public AssociationDto(){}

    public AssociationDto(int id, String nom, Member member){
        this.id = id;
        this.nom = nom;
        this.contactMember = member;
    }


    public String getNom(){
        return nom;
    }

    public void setNom(String nom){
        this.nom = nom;
    }
    public Member getContactMember(){
        return contactMember;
    }
    public void setContactMember(Member member){
        this.contactMember = member;
    }

    public int getId(){
        return id;
    }

}
