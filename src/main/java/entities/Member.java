package entities;

import org.hibernate.type.TrueFalseType;

import javax.persistence.*;
import javax.ws.rs.POST;
import java.util.List;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String login;
    private String password;
    private boolean isContact;
    @ManyToOne
    private Association association;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "demandes")
    private List<Post> demande;


    public Member(){}

    public Member(String login, String password, boolean isContact ) {
        this.login = login;
        this.password = password;
        this.isContact = isContact;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public boolean getContact() {
        return isContact;
    }

    public void setContact(boolean contact) {
        isContact = contact;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public List<Post> getDemande() { return demande; }

    public void setDemande(List<Post> demande) { this.demande = demande; }

    public List<Post> addDemande(Post post){
        this.demande.add(post);
        System.out.println("from member "+ this.demande);
        return this.demande;
    }
}
