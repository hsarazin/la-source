package entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Association {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String nom;

    @OneToOne
    private Member contactMember;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Member> members = new java.util.ArrayList<>();

    @OneToMany
    private List<Post> posts;

    public Association() {}

    public Association(String nom, Member contactMember) {
        this.nom = nom;
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

    public List<Member> getMembers() { return members; }

    public void setMembers(List<Member> members) { this.members = members; }

    public Member getContactMember() { return contactMember; }

    public void setContactMember(Member contactMember) { this.contactMember = contactMember; }

    public List<Post> getPosts() { return posts; }

    public void setPosts(List<Post> posts) { this.posts = posts; }

    public void addMember(Member member){
        this.members.add(member);
    }
    @Override
    public String toString() {
        return nom;
    }
}
