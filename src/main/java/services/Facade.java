package services;

import dtos.PostDto;
import entities.Association;
import entities.Member;
import entities.Post;
import exceptions.AssociationAlreadyExistException;
import exceptions.PostAlreadyExistsException;
import exceptions.PostAlreayAskedException;
import exceptions.UserAllreadyExistsException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
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
    public Association createAssociation(String login, String association_nom) throws AssociationAlreadyExistException{
        try{
            Association association =  getAssociation(association_nom);
        } catch(Exception exception) {
            Member member = em.find(Member.class, findIdByLogin(login));
            Association association = new Association(association_nom, member);
            em.persist(association);
            association.setContactMember(member);
            member.setContact(true);
            member.setAssociation(association);
            System.out.println(association);
            System.out.println("la nouvelle asso est: " + member.getAssociation());
            return association;
        }
        throw new AssociationAlreadyExistException();

    }

    @Transactional
    public void joinAssociation(String login, int association_id){
        Association association = em.find(Association.class,association_id);
        Member member = retrieveUser(findIdByLogin(login));
        member.setAssociation(association);
        association.addMember(member);
        Member contact = association.getContactMember();
        List<Post> demands = contact.getDemande();
        for(Post demand: demands){
            member.addDemande(demand);
        }
    }

    @Transactional
    public void leaveAssociation(String login){
        Member member = retrieveUser(findIdByLogin(login));
        if (!member.getContact()) {
            Association association = member.getAssociation();
            member.setAssociation(null);
            association.removeMember(member);
        }
        member.setDemande(null);
    }

   public Member retrieveUser(int id) {
        return em.find(Member.class,id);
   }

   public boolean getIsContact(String login) {
       Member member=em.find(Member.class,findIdByLogin(login));
       return member.getContact();
   }

   public Member getMyContact(String login){
        Association association = getMyAssociation(login);
        if(association.getContactMember()!=null){
            return association.getContactMember();
        }
        System.out.println(association.getNom());
        Query q = em.createQuery("select c from Member c where c.association.nom like :a and c.isContact = TRUE ").setParameter("a", association.getNom());
        return (Member) q.getSingleResult();
   }

   public Association getMyAssociation(String login){
        try {
            Member member = em.find(Member.class, findIdByLogin(login));
            return member.getAssociation();
        } catch (Exception exception){
            return null;
        }
   }
   public Association getAssociation(String nom) {
        try{
            Query q = em.createQuery("select a from Association a where a.nom=:n").setParameter("n",nom);
            return (Association) q.getSingleResult();
        } catch (Exception exception){
            System.out.println(exception);
            throw exception;
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

   public Post getPost(String post){
        Query q = em.createQuery("select p from Post p where p.nom =:pnom").setParameter("pnom", post);
        return (Post) q.getSingleResult();
   }

   public List<PostDto> getAllPost(String login){
        Query q = em.createQuery("select p from Post p");
        List<PostDto> postDtos = new ArrayList<>();
        List<Post> posts = q.getResultList();
        Member member = em.find(Member.class,findIdByLogin(login));
        for (Post post : posts){
            PostDto postDto = new PostDto();
            postDto.setNom(post.getNom());
            postDto.setCategorie(post.getCategorie());
            postDto.setAssociation(post.getAssociation());

            postDto.select(false);
            for (Post post1 : member.getDemande()) {
                if(post1.getId()==post.getId()){
                    postDto.select(true);
                }
            }

            postDtos.add(postDto);

        }
        return postDtos;
   }

   public List<Post> getALlPostExcept(Post post){
       Query q = em.createQuery("select p from Post p where p.nom<>:n").setParameter("n", post.getNom());
       return q.getResultList();
   }

   @Transactional
    public void createPost(String nom, String categorie, int idContactPerson){
        Association association = em.find(Member.class, idContactPerson).getAssociation();
        Post post=new Post(nom, categorie, association);
        em.persist(post);

    }

    @Transactional
    public void addPost (int post_id, int member_id) throws PostAlreayAskedException{
        Member member = em.find(Member.class, member_id);
        Post post = em.find(Post.class, post_id);
        List<Post> demandes = member.getDemande();
        if(demandes.contains(post)){
            throw new PostAlreayAskedException();
        }
        Association association = member.getAssociation();
        association.getContactMember().addDemande(post);
        for(Member association_member : association.getMembers()){
            association_member.addDemande(post);

        }
    }
    public void demand(int post_id,String login){
        System.out.println("DEMANDE");
        Member member = em.find(Member.class, findIdByLogin(login));
        Post post = em.find(Post.class, post_id);
        post.addDemands(member);
        System.out.println("DEMANDES : "+ post.getDemands());
    }

    public List<Post> getAllDemands(String login) {
        Member member = em.find(Member.class,findIdByLogin(login));
        System.out.println("Facade getAllDemandes :" + member.getDemande());
        return member.getDemande();
    }
}
