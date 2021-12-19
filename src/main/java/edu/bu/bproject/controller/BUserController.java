package edu.bu.bproject.controller;

import edu.bu.bproject.entity.BUsers;
import edu.bu.bproject.repository.BUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/bUser")
public class BUserController {
    
    @Autowired
    public BUserRepository bUserRepository;

    @GetMapping("/findById/{id}")
    public String findById(@PathVariable("id") Integer id,
                           HttpSession httpSession) {
        httpSession.setAttribute("bUser", bUserRepository.findById(id).get());
        return "bUser/save";
    }

    @GetMapping("/findBUserByName/{name}")
    public List<BUsers> findBUserByName(@PathVariable("name") String name) {
        return bUserRepository.findBUserByName(name);
    }

    @GetMapping("/findAll")
    public List<BUsers> findAll() {
        return bUserRepository.findAll();
    }

    @PostMapping("/save")
    public String addUser(BUsers BUser, HttpSession httpSession) {
        BUser.setBalance(0);
        bUserRepository.save(BUser);
        httpSession.setAttribute("BUsers", bUserRepository.findAll());
        return "redirect:/bUser";
    }

    @GetMapping("/save")
    public String toSavePage() {
        return "/bUser/save";
    }

    @PutMapping("/save")
    public String update(@RequestBody BUsers bUser, HttpSession httpSession) {
        bUserRepository.save(bUser);
        httpSession.removeAttribute("bUser");
        httpSession.setAttribute("BUsers", bUserRepository.findAll());
        return "/bUser";
    }

    @GetMapping("deleteById/{id}")
    public String deleteById(@PathVariable("id") Integer id) {
        bUserRepository.deleteById(id);
        return "redirect:/bUser";
    }
}
