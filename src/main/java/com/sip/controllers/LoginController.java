package com.sip.controllers;


import java.util.List;

import javax.validation.Valid;

import com.sip.entities.Article;
import com.sip.entities.Provider;
import com.sip.entities.Role;
import com.sip.entities.User;
import com.sip.repositories.ArticleRepository;
import com.sip.repositories.ProviderRepository;
import com.sip.repositories.RoleRepository;
import com.sip.repositories.UserRepository;
import com.sip.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class LoginController {
	private final ArticleRepository articleRepository;
	private final ProviderRepository providerRepository;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	 @Autowired
	    public LoginController(ArticleRepository articleRepository, ProviderRepository providerRepository,UserRepository userRepository, RoleRepository roleRepository) {
	        this.articleRepository = articleRepository;
	        this.providerRepository = providerRepository;
	        this.userRepository = userRepository;
	        this.roleRepository = roleRepository;
	    }
	    
    @Autowired
    private UserService userService;
    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }
    
    @RequestMapping(value={"/dashboard"}, method = RequestMethod.GET)
    public ModelAndView goToDashboard(Model model){
        ModelAndView modelAndView = new ModelAndView();
    	long nbrArticles =  articleRepository.count();
    	long nbrProviders =  providerRepository.count();
    	long nbrUsers =  userRepository.count();
    	long nbrRoles =  roleRepository.count();
    	model.addAttribute("nbrArticles", nbrArticles);
    	model.addAttribute("nbrProviders", nbrProviders);
        modelAndView.setViewName("dashboard/dashboard");
        return modelAndView;
    }
    @RequestMapping(value={"/home"}, method = RequestMethod.GET)
    public ModelAndView accueil(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");

        ///
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        System.out.println(user.getEmail()+" "+user.getName()+" "+user.getLastName());

        ////

        return modelAndView;
    }
    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
    	
        ModelAndView modelAndView = new ModelAndView();
        
        User userExists = userService.findUserByEmail(user.getEmail());
        
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        }
        
        else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");
        }
        return modelAndView;
    }
   /* @RequestMapping(value="/admin/home", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }*/
    
    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }
}

