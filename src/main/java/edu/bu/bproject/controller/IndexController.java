package edu.bu.bproject.controller;

import edu.bu.bproject.entity.BMenus;
import edu.bu.bproject.entity.BRecords;
import edu.bu.bproject.entity.BUsers;
import edu.bu.bproject.repository.BMenuRepository;
import edu.bu.bproject.repository.BRecordRepository;
import edu.bu.bproject.repository.BUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    public BUserRepository bUserRepository;
    @Autowired
    public BRecordRepository bRecordRepository;
    @Autowired
    public BMenuRepository bMenuRepository;

    BUsers bUsers;


    @GetMapping("/addValue")
    public String addValue() {
        return "bRecord/addValue";
    }

    @GetMapping("/withdraw")
    public String withdraw() {
        return "bRecord/addValue";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/bUser")
    public String userManager(HttpSession httpSession) {
        httpSession.setAttribute("BUsers", bUserRepository.findAll());
        return "/bUser";
    }

    @GetMapping("/bMenu")
    public String orderBuilder(HttpSession httpSession) {
        bUsers = (BUsers)httpSession.getAttribute("loginUser");
        httpSession.setAttribute("BMenus",bMenuRepository.findBMenuBySellerId(bUsers.getId()));
        return "/bMenu";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model,
                        HttpSession httpSession) {
        List<BUsers> bUsers = bUserRepository.findBUserByName(username);
        BUsers user = null;
        List<BRecords> bRecords = null;
        if (!bUsers.isEmpty()) {
            for (BUsers users : bUsers) {
                user = users;
            }
        }
        if (password.equals(user.getPassword())) {

            httpSession.setAttribute("loginUser", user);
            if (user.getType() == 1) {
                bRecords = bRecordRepository.findAll();
                bUsers = bUserRepository.findAll();
                httpSession.setAttribute("BUsers", bUsers);
            } else if (user.getType() == 2) {
                bRecords = bRecordRepository.findBRecordByCustomerId(user.getId());
            } else {
                bRecords = bRecordRepository.findBRecordBySellerId(user.getId());
                List<BMenus> BMenus = bMenuRepository.findBMenuBySellerId(user.getId());
                httpSession.setAttribute("BMenus", BMenus);
            }
            httpSession.setAttribute("BRecords", bRecords);
            return "admin";
        } else {
            model.addAttribute("msg", "wrong username or password");
            return "login";
        }
    }
}
