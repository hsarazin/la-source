package services;

import entities.Association;
import entities.Member;
import entities.Post;
import exceptions.UserAllreadyExistsException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
public class Facade {
    @PersistenceContext
    private EntityManager em;

    public boolean checkLP(String login,String password) {
        // On va maintenant chercher l'utilisateur dans la BD à partir du login
        try {
            Member member = em.find(Member.class, findIdByLogin(login));
            if (member == null) {
                return false;
            } else {
                return (member.getPassword().equals(password));
            }
        } catch (Exception exception){
            return false;
        }
   }


   public int findIdByLogin(String login) {
        // On va chercher l'id de la personne a partir du login
       try {
           Query q = em.createQuery("select m.id from Member m where m.login =:log");
           q.setParameter("log", login);
           return (int) q.getSingleResult();
       } catch (Exception exception){
           throw exception;
       }
    }

   @Transactional
   public Member createUser(String login,String password, boolean isContact) throws UserAllreadyExistsException {
        try {
            findIdByLogin(login);
        } catch(Exception exception){
            Member member = new Member(login, password, isContact);
            em.persist(member);
            return member;
            }
        throw new UserAllreadyExistsException();

    }

    @Transactional
    public void joinAssociation(String login, int association_id){
        Association association = em.find(Association.class,association_id);
        Member member = retrieveUser(findIdByLogin(login));
        member.setAssociation(association);
    }

    @Transactional
    public void leaveAssociation(String login){
        Member member = retrieveUser(findIdByLogin(login));
        member.setAssociation(null);
    }

   public Member retrieveUser(int id) {
        return em.find(Member.class,id);
   }

   public boolean getIsContact(String login) {
       Member member=em.find(Member.class,findIdByLogin(login));
       return member.getContact();
   }

   public Association getMyAssociation(String login){
        try {
            Member member = em.find(Member.class, findIdByLogin(login));
            return member.getAssociation();
        } catch (Exception exception){
            return null;
        }
   }
   public List<Association> getAllAssociations() {
        Query q = em.createQuery("select a from Association a");
        return q.getResultList();
   }

   public List<Association> getAllAssociationsExcept(Association asso){
        Query q = em.createQuery("select a from Association a where a.nom<>:n").setParameter("n", asso.getNom());
        return q.getResultList();
   }

   public List<Post> getAllPost(){
        Query q = em.createQuery("select p from Post p");
        return q.getResultList();
   }

   public List<Post> getALlPostExcept(Post post){
       Query q = em.createQuery("select p from Post p where p.nom<>:n").setParameter("n", post.getNom());
       return q.getResultList();
   }

   @Transactional
    public void createPost(String nom, String categorie, int idContactPerson){
        Association asso = em.find(Member.class, idContactPerson).getAssociation();
        Post p=new Post(nom, categorie, asso);
        em.persist(p);
    }


}
