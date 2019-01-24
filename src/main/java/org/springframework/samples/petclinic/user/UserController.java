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

import org.springframework.samples.petclinic.owner.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


import java.util.Map;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    
    
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView();
        String errorMessage = "Usuario no autorizado, debe autentificarse";
        mav.addObject("errorMsg", errorMessage);
        mav.setViewName("login");
        return mav;
    }
}







    




