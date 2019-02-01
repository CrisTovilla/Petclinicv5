package org.springframework.samples.petclinic.user;

import java.io.IOException;
import java.util.Collection;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
class UserController {
    private static final String VIEWS_USER_CREATE_OR_UPDATE_FORM = "users/createOrUpdateUserForm";
    private final UserRepository users;
    
    public UserController(UserRepository clinicService){
        this.users = clinicService;
    }
    
    @GetMapping("/users/find")
    public String initFindForm(Map<String, Object> model) {
        model.put("user", new UserEntity());
        return "users/findUsers";
    }
       
    @GetMapping("/users/new")
    public String initCreationForm(Map<String, Object> model) {
        UserEntity user = new UserEntity();
        model.put("user", user);
        return VIEWS_USER_CREATE_OR_UPDATE_FORM;
    }
    
    @PostMapping("/users/new")
    public String processCreationForm(@Valid @ModelAttribute(value="user") UserEntity user, BindingResult result) throws IOException {
        UserEntity userNameExist=this.users.existUserName(user.getUsername().toString());
        UserService rest=new UserService();
        boolean existPostalCode=rest.existPostalCode(user.getPostalcode().toString(),user.getCity().toString());
        
        if (result.hasErrors() || userNameExist!=null || !existPostalCode) {
            if(userNameExist!=null){
                result.rejectValue("username","duplicate");
            }else if(!existPostalCode){
                result.rejectValue("postalcode","notFound");             
            } 
            return VIEWS_USER_CREATE_OR_UPDATE_FORM;
        } 
        else {
            user.setActive(true);
            String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashpw);
            this.users.save(user);     
            return "redirect:/users/" + user.getId();
        }
    }
    
    /**
    * Custom handler for displaying an owner.
    *
    * @param userId the ID of the owner to display
    * @return a ModelMap with the model attributes for the view
    */
    @GetMapping("/users/{userId}")
    public ModelAndView showUser(@PathVariable("userId") int userId) {
        ModelAndView mav = new ModelAndView("users/userDetails");
        UserEntity user=this.users.findById(userId).get();
        mav.addObject("user", this.users.findById(userId).get());
        return mav;
    }
    
    @GetMapping("/users/{userId}/edit")
    public String initUpdateUserForm(@PathVariable("userId") int userId, Model model) {
        UserEntity user = this.users.findById(userId).get();
        model.addAttribute("user",user);
        return VIEWS_USER_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/users/{userId}/edit")
    public String processUpdateUserForm(@Valid @ModelAttribute(value="user") UserEntity user, BindingResult result, @PathVariable("userId") int userId) throws IOException {
        UserEntity userActual=this.users.findById(userId).get();
        UserEntity userNameExist;
        System.out.println(user.getUsername().toString()+"\n"+userActual.getUsername().toString());
        if(user.getUsername().equals(userActual.getUsername())){
            System.out.println("Iguales");
            userNameExist=null;
        }
        else{
            System.out.println("No Iguales");
            userNameExist=this.users.existUserName(user.getUsername().toString());
        }      
        
        UserService rest=new UserService();
        boolean existPostalCode=rest.existPostalCode(user.getPostalcode().toString(),user.getCity().toString());
        
        if (result.hasErrors() || userNameExist!=null || !existPostalCode) {
            if(userNameExist!=null) {
                result.rejectValue("username","duplicate");
            }
            else if(!existPostalCode) {
                result.rejectValue("postalcode","notFound");             
            } 
            return VIEWS_USER_CREATE_OR_UPDATE_FORM;
        } 
        else {
            user.setId(userId);
            user.setActive(true);
            String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashpw);
            this.users.save(user);     
            return "redirect:/users/" + user.getId();
        }
    }
    
    @GetMapping("/users")
    public String processFindForm( @ModelAttribute(value="user") UserEntity user, BindingResult result, Map<String, Object> model) {      
        if (user.getLastName() == null) {
            user.setLastName("");
        }
        
        Collection<UserEntity> results = this.users.findByLastName(user.getLastName().toString());
        
        if (results.isEmpty()) {
            result.rejectValue("lastName", "notFound", "not found");
            return "users/findUsers";
        } 
        else if (results.size() == 1) {          
            user = results.iterator().next();
            return "redirect:/users/" + user.getId();
        } 
        else {
            model.put("selections", results);
            return "users/usersList";
        }
    }
    
    @GetMapping("/users/report")
    public String processReport( Map<String, Object> model) {      
        Collection<UserEntity> results = this.users.findByLastName("");
        model.put("selections", results);
        return "users/usersReport";
    }

    @GetMapping("/users/{userId}/deactivate")
    public String deactivateUser(@ModelAttribute(value="user") UserEntity user, BindingResult result,@PathVariable("userId") int userId){
        user = this.users.findById(userId).get();
        user.setActive(false); 
        this.users.save(user);
        return "redirect:/users?lastName=";
    }
    
    @GetMapping("/users/{userId}/activate")
    public String activateUser(@ModelAttribute(value="user") UserEntity user, BindingResult result,@PathVariable("userId") int userId){
        user = this.users.findById(userId).get();
        user.setActive(true); 
        this.users.save(user);
        return "redirect:/users?lastName=";
    }
    
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView();
        String errorMessage = "Usuario o contraseña incorrectos";
        mav.addObject("errorMsg", errorMessage);
        mav.setViewName("login");
        return mav;
    }
    
    @GetMapping("/logout")
    public ModelAndView logout() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("");
        return mav;
    }
}