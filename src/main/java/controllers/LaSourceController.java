package controllers;

import dtos.AssociationDto;
import dtos.MemberDto;
import dtos.PostDto;
import entities.Association;
import entities.Member;
import entities.Post;
import exceptions.AssociationAlreadyExistException;
import exceptions.UserAllreadyExistsException;
import org.h2.engine.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import services.Facade;

@Controller
@SessionAttributes("courant")
@RequestMapping("/")
public class LaSourceController {
    @Autowired
    private Facade facade;
    @RequestMapping("")
    public String toLogin(Model model) {
        //ici on doit renvoyer un User du fait traitement avec modelAttribute et path côté jsp
        model.addAttribute(new MemberDto());
        return("login");
    }

    public String loadWelcome(String login, Model model){
        Association my_association = facade.getMyAssociation(login);
        model.addAttribute("courant",login);
        model.addAttribute("username",login);
        if(my_association!=null) {
            model.addAttribute("my_association", my_association.getNom());
        }
        addElemIfContact(model, login);
        return "welcome";
    }

    @PostMapping("login")
    public String checkLP(MemberDto memberDto, BindingResult result, Model model){
        if (facade.checkLP(memberDto.getLogin(), memberDto.getPassword())) {
            // on place courant dans le modèle, mais il s'agit d'un attribut de session, il se retrouve ainsi conservé en session

            String login = memberDto.getLogin();
            return loadWelcome(login,model);
        } else {
            // on crée à la volée un "ObjectError" : erreur globale dans l'objet (ici l'objet c'est l'instance de user où transitent les infos de login)
            result.addError(new ObjectError("user","Les informations saisies ne correspondent pas à un utilisateur connu."));
            System.out.println(result.hasErrors());
            // le user du model est renvoyé tel quel à la jsp, et on préserve les valeurs saisies (comment réinitialiser ?)
            return "login";
        }
    }

    @PostMapping("register")
    public String register(MemberDto memberDto,BindingResult result, Model model){
        try {
            facade.createUser(memberDto.getLogin(),memberDto.getPassword(), memberDto.getIsContact());
        } catch (UserAllreadyExistsException e) {
            result.addError(new ObjectError("user","Ce login n'est pas disponible."));
            return "login";
        }
        String login = memberDto.getLogin();

        return loadWelcome(login,model);
    }

    @RequestMapping("logout")
    public String logout(SessionStatus status,Model model) {
        status.setComplete();
        model.addAttribute("courant", null);
        model.addAttribute(new MemberDto());
        return "login";
    }

    @RequestMapping("testCP")
    public String testCP(SessionStatus status, @SessionAttribute String courant, Model model) {
        Member m =facade.retrieveUser(facade.findIdByLogin(courant));
        if (m.getContact()){
            status.setComplete();
            model.addAttribute("courant", null);
            model.addAttribute(new MemberDto());
            return "login";
        }
        return "welcome";
    }

    @RequestMapping("fragment")
    public String fragment(String fragment, Model model, @SessionAttribute String courant){
        model.addAttribute("fragment", fragment);
        model.addAttribute(new AssociationDto());
        return loadWelcome(courant,model);
    }

    @RequestMapping("join")
    public String join(int association_id, Model model, @SessionAttribute String courant){
        facade.joinAssociation(courant, association_id);
        return loadWelcome(courant,model);
    }

    @RequestMapping("association/create")
    public String createAssociation(AssociationDto associationDto, BindingResult result, Model model, @SessionAttribute String courant){
        try {
            System.out.println(associationDto.getNom());
            Association association =  facade.createAssociation(courant,associationDto.getNom());
            System.out.println(association);
        } catch (AssociationAlreadyExistException e) {
        result.addError(new ObjectError("nom","Cette association existe déjà !"));
        model.addAttribute(new AssociationDto());
        }
        return loadWelcome(courant, model);
    }

    @RequestMapping("leave")
    public String leave(Model model, @SessionAttribute String courant){
        facade.leaveAssociation(courant);
        return loadWelcome(courant,model);
    }


    @RequestMapping("demande")
    public String demandePost(PostDto postDto, @SessionAttribute String courant, Model model){
        System.out.println("coucou0");
        Post post = new Post(postDto.getNom(), postDto.getCategorie(), postDto.getAssociation());
        System.out.println("coucou1");
        /**Member contact = facade.getMyContact(courant);
        System.out.println("contact =" + contact.getLogin());
        contact.getDemande().add(post);
        System.out.println("coucou2");
        System.out.println("Le contact a maintenant comme demandes" + contact.getDemande());**/
        loadWelcome(courant,model);
        return "welcome";
    }

    private void addElemIfContact(Model model,String courant) {
        model.addAttribute("contact", facade.getIsContact(courant));
        model.addAttribute("posts", facade.getAllPost());
        model.addAttribute("associations", facade.getAllAssociations());
    }
}
