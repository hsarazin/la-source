package services;

import entities.Association;
import entities.Member;
import exceptions.UserAllreadyExistsException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
public class Facade {
    @PersistenceContext
    private EntityManager em;

    public boolean checkLP(String login,String password) {
        // On va maintenant chercher l'utilisateur dans la BD à partir du login
        Member member=em.find(Member.class,login);
        if (member==null) {
            return false;
        } else {
            return (member.getPassword().equals(password));
        }
   }

   @Transactional
   public Member createUser(String login,String password) throws UserAllreadyExistsException {
       Member member=em.find(Member.class,login);
        if (member!=null) {
            throw new UserAllreadyExistsException();
        }
       member =new Member(login,password, new Association());
        em.persist(member);
        return member;
   }

   public Member retrieveUser(String login) {
        return em.find(Member.class,login);
   }

}
