package controllers;

import dtos.MemberDto;
import exceptions.UserAllreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
            model.addAttribute("courant", memberDto.getLogin());
            model.addAttribute("username", memberDto.getLogin());
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
            facade.createUser(memberDto.getLogin(),memberDto.getPassword());
        } catch (UserAllreadyExistsException e) {
            result.addError(new ObjectError("user","Ce login n'est pas disponible."));
            return "login";
        }
        model.addAttribute("courant", memberDto.getLogin());
        model.addAttribute("username", memberDto.getLogin());
        return "welcome";
    }

    @RequestMapping("logout")
    public String logout(SessionStatus status,Model model) {
        status.setComplete();
        model.addAttribute("courant", null);
        model.addAttribute(new MemberDto());
        return "login";
    }

}
