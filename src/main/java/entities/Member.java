package entities;

import javax.persistence.*;
import java.util.List;

public class Member {
    @Id
    @GeneratedValue
    private int id;
    private String login;
    private String password;

    @ManyToOne
    private Association association;

    public Member(){}

    public Member(String login, String password, Association association) {
        this.login = login;
        this.password = password;
        this.association = association;
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

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }
}
