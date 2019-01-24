/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.user;

import java.util.Collection;
import org.springframework.samples.petclinic.owner.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
//@RequestMapping("users")
class UserController {
    private static final String VIEWS_USER_CREATE_OR_UPDATE_FORM = "users/createOrUpdateUserForm";
    private final UserRepository users;
    
    public UserController(UserRepository clinicService){
        this.users=clinicService;
        
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
    public String processCreationForm(@Valid @ModelAttribute(value="user") UserEntity user, BindingResult result) {
        
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return VIEWS_USER_CREATE_OR_UPDATE_FORM;
        } else {
            user.setActive(true);
            this.users.save(user);     
            System.out.println("YAMERO User"+user.getId());
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
    public ModelAndView showOwner(@PathVariable("userId") int userId) {
        ModelAndView mav = new ModelAndView("users/userDetails");
        UserEntity user=this.users.findById(userId).get();
        mav.addObject("user", this.users.findById(userId).get());
        return mav;
    }
    
    
    @GetMapping("/users/{userId}/edit")
    public String initUpdateOwnerForm(@PathVariable("userId") int userId, Model model) {
        UserEntity user = this.users.findById(userId).get();
        model.addAttribute("user",user);
        return VIEWS_USER_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/users/{userId}/edit")
    public String processUpdateOwnerForm(@Valid @ModelAttribute(value="user") UserEntity user, BindingResult result, @PathVariable("userId") int userId) {
        if (result.hasErrors()) {
            return VIEWS_USER_CREATE_OR_UPDATE_FORM;
        } else {
            user.setId(userId);
            this.users.save(user);
            return "redirect:/users/{userId}";
        }
    }
    
    @GetMapping("/users")
    public String processFindForm( @ModelAttribute(value="user") UserEntity user, BindingResult result, Map<String, Object> model) {
        System.out.println("ASDASD");
        // allow parameterless GET request for /owners to return all records
        if (user.getLastName() == null) {
            user.setLastName(""); // empty string signifies broadest possible search
        }
       
        // find owners by last name
        Collection<UserEntity> results = this.users.findByLastName(user.getLastName().toString());
        if (results.isEmpty()) {
            // no owners found
            result.rejectValue("lastName", "notFound", "not found");
            return "users/findUsers";
        } else if (results.size() == 1) {
            // 1 owner found
            user = results.iterator().next();
            return "redirect:/users/" + user.getId();
        } else {
            // multiple owners found
            model.put("selections", results);
            return "users/usersList";
        }
    }

    
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView();
        String errorMessage = "Usuario no autorizado, debe autentificarse";
        mav.addObject("errorMsg", errorMessage);
        mav.setViewName("login");
        return mav;
    }
}







    




