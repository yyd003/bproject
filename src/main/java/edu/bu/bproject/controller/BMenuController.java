package edu.bu.bproject.controller;

import edu.bu.bproject.entity.BMenus;
import edu.bu.bproject.entity.BUsers;
import edu.bu.bproject.repository.BMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/bMenu")
public class BMenuController {
    @Autowired
    BMenuRepository bMenuRepository;
    BUsers bUser;


    @GetMapping("/findAll")
    public List<BMenus> findAll() {
        return bMenuRepository.findAll();
    }

    @GetMapping("/findBMenuBySellerId/{id}")
    public String findBMenuBySellerId(@PathVariable("id") Integer id,
                                      HttpSession httpSession) {
        httpSession.setAttribute("bMenu", bMenuRepository.findById(id));
        bMenuRepository.findBMenuBySellerId(id);
        return "/bMenu/save";
    }

    @GetMapping("/findBMenuById/{id}")
    public String findById(@PathVariable("id") Integer id,
                           HttpSession httpSession) {
        httpSession.setAttribute("bMenu", bMenuRepository.findById(id).get());
        return "/bMenu/save";
    }

    @PostMapping("/save")
    public String save(BMenus bMenu,
//                       @RequestParam("id") Integer id,
//                       @RequestParam("sellerId") Integer sellerId,
//                       @RequestParam("name") String name,
//                       @RequestParam("price") Integer price,
                       HttpSession httpSession) {
//        bMenu = new BMenu();
//        bMenu.setId(id);
//        bMenu.setName(name);
//        bMenu.setSellerId(sellerId);
//        bMenu.setPrice(price);
        System.out.println("Before:" + httpSession.getAttribute("BMenus"));
        bUser = (BUsers) httpSession.getAttribute("loginUser");
        bMenuRepository.save(bMenu);
        httpSession.removeAttribute("bMenu");
        httpSession.setAttribute("BMenus", bMenuRepository.findBMenuBySellerId(bUser.getId()));
        System.out.println("After" + httpSession.getAttribute("BMenus"));
        return "/bMenu";
    }

    @PutMapping("/save")
    public String update(BMenus bMenu, HttpSession httpSession) {
        bUser = (BUsers) httpSession.getAttribute("loginUser");
        bMenuRepository.save(bMenu);
        httpSession.removeAttribute("bMenu");
        httpSession.setAttribute("bMenus", bMenuRepository.findBMenuBySellerId(bUser.getId()));
        return "redirect:/bMenu";
    }

    @GetMapping("/save")
    public String toSave() {
        return "bMenu/save";
    }

    @PutMapping("/update")
    public void update(@RequestBody BMenus bMenus) {
        bMenuRepository.save(bMenus);
    }

    @GetMapping("/deleteById/{id}")
    public String deleteById(@PathVariable("id") Integer id) {
        bMenuRepository.deleteById(id);
        return "redirect:/bMenu";
    }

}
