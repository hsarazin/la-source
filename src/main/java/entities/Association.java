package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Association {
    @Id
    @GeneratedValue
    private int id;
    private String nom;
    private int idPersonneContact;
    public Association() {}


}
