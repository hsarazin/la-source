package controllers;

import dtos.AssociationDto;
import dtos.MemberDto;
import dtos.PostDto;
import entities.Association;
import entities.Member;
import entities.Post;
import exceptions.AssociationAlreadyExistException;
import exceptions.PostAlreadyExistsException;
import exceptions.PostAlreayAskedException;
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
        model.addAttribute("posts", facade.getAllPost(login));
        model.addAttribute("demandes", facade.getAllDemands(login));
        model.addAttribute("myPosts", facade.getMyPost(login));
        if(my_association!=null) {
            model.addAttribute("my_association", my_association);
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
        Post post = facade.getPost(postDto.getNom());
        Member contact = facade.getMyContact(courant);
        try {
            facade.addPost(post.getId(), contact.getId());
            facade.demand(post.getId(),courant);
        } catch(PostAlreayAskedException exception){
            return fragment("post",model,courant);
        }

        System.out.println("Facade from contoller" + facade.getAllDemands(contact.getLogin()));
        System.out.println("Controller" + contact.getDemande());
        return fragment("post",model,courant);
    }

    @RequestMapping("valider")
    public String validerPost(PostDto postDto, @SessionAttribute String courant, Model model){
        /**Post post = new Post(postDto.getNom(), postDto.getCategorie(), postDto.getAssociation());
        Member contact = facade.getMyContact(courant);
        contact.getDemande().add(post);
        loadWelcome(courant,model);*/
        loadWelcome(courant,model);
        return "welcome";
    }

    @RequestMapping("post/create")
    public String postCreate(PostDto postDto,BindingResult result, Model model, @SessionAttribute String courant){
        facade.createPost(postDto.getNom(),postDto.getCategorie(), facade.findIdByLogin(courant));
        return loadWelcome(courant,model);
    }

    @RequestMapping("post/delete")
    public String postDelete(PostDto postDto,BindingResult result, Model model, @SessionAttribute String courant){
        Post post = facade.getPost(postDto.getNom());
        facade.deletePost(post.getId());
        return loadWelcome(courant,model);
    }


    private void addElemIfContact(Model model,String courant) {
        model.addAttribute("contact", facade.getIsContact(courant));
        model.addAttribute("posts", facade.getAllPost(courant));
        model.addAttribute("associations", facade.getAllAssociations());
        model.addAttribute("demandes", facade.getAllDemands(courant));
        model.addAttribute("myPosts", facade.getMyPost(courant));
    }
}
