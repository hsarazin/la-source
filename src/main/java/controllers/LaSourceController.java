package controllers;

import dtos.MemberDto;
import dtos.PostDto;
import entities.Association;
import entities.Member;
import exceptions.UserAllreadyExistsException;
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

    @PostMapping("login")
    public String checkLP(MemberDto memberDto, BindingResult result, Model model){
        if (facade.checkLP(memberDto.getLogin(), memberDto.getPassword())) {
            // on place courant dans le modèle, mais il s'agit d'un attribut de session, il se retrouve ainsi conservé en session

            String login = memberDto.getLogin();
            Association my_association = facade.getMyAssociation(login);
            model.addAttribute("courant", login);
            model.addAttribute("username", login);
            if(my_association!=null) {
                model.addAttribute("my_association", my_association.getNom());
            }
            addElemIfContact(model, memberDto.getLogin());
            return "welcome";
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

        model.addAttribute("courant", login);
        model.addAttribute("username", login);
        model.addAttribute("my_association",facade.getMyAssociation(login).getNom());
        addElemIfContact(model, memberDto.getLogin());
        return "welcome";
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
        model.addAttribute("username",courant);
        model.addAttribute("my_association",facade.getMyAssociation(courant).getNom());
        model.addAttribute("posts", facade.getAllPost());
        model.addAttribute("associations", facade.getAllAssociations());
        return "welcome";
    }

    @RequestMapping("demande")
    public String demandePost(@SessionAttribute String courant, PostDto postDto){
        System.out.println(postDto.getNom());
        //WIP
        return "welcome";
    }

    private void addElemIfContact(Model model,String courant) {
        model.addAttribute("contact", facade.getIsContact(courant));
        model.addAttribute("posts", facade.getAllPost());
        model.addAttribute("associations", facade.getAllAssociations());
    }
}
