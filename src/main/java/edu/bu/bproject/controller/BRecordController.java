package edu.bu.bproject.controller;

import edu.bu.bproject.entity.BRecords;
import edu.bu.bproject.entity.BUsers;
import edu.bu.bproject.repository.BRecordRepository;
import edu.bu.bproject.repository.BUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/bRecord")
public class BRecordController {

    @Autowired
    BRecordRepository bRecordRepository;
    @Autowired
    BUserRepository bUserRepository;

    BRecords bRecord;
    List<BRecords> bRecords;

    @GetMapping("/findAll")
    public List<BRecords> findAll() {
        return bRecordRepository.findAll();
    }

    @GetMapping("/findBRecordBySellerId/{id}")
    public List<BRecords> findBRecordBySellerId(@PathVariable("id") Integer id) {
        return bRecordRepository.findBRecordBySellerId(id);
    }

    @GetMapping("/findBRecordByCustomerId/{id}")
    public List<BRecords> findBRecordByCustomerId(@PathVariable("id") Integer id) {
        return bRecordRepository.findBRecordByCustomerId(id);
    }

    @GetMapping("/findById/{id}")
    public BRecords findById(@PathVariable("id") Integer id) {
        return bRecordRepository.findById(id).get();
    }

    @DeleteMapping("/deleteById/{id}")
    public void deleteById(@PathVariable("id") Integer id) {
        bRecordRepository.deleteById(id);
    }

    @PostMapping("/save")
    public String save(@RequestParam Integer customerId,
                       @RequestParam Integer amount,
                       HttpSession httpSession) {
        System.out.println("$"+amount+"new order!\n"+bUserRepository.findById(customerId).get());
        bRecord = new BRecords();
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        BUsers bUsers = (BUsers) httpSession.getAttribute("loginUser");
        bRecord.setSellerId(bUsers.getId());
        bRecord.setCustomerId(customerId);
        bRecord.setOrderTime(ts);
        bRecord.setAmount(amount);
        bRecord.setComment(bUserRepository.findById(customerId).get().getName() + "'s order from " + bUsers.getName() + " (total: $" + amount + ")");
        bRecordRepository.save(bRecord);
        bUsers = bUserRepository.findById(customerId).get();
        bUsers.setBalance(bUsers.getBalance() - bRecord.getAmount());
        bUserRepository.save(bUsers);
        bUsers = (BUsers) httpSession.getAttribute("loginUser");
        bUsers.setBalance(bUsers.getBalance() + bRecord.getAmount());
        bUserRepository.save(bUsers);
        httpSession.setAttribute("loginUser", bUsers);
        if (bUsers.getType() == 1) {
            bRecords = bRecordRepository.findAll();
            httpSession.setAttribute("BUsers", bUserRepository.findAll());
        } else if (bUsers.getType() == 2) {
            bRecords = bRecordRepository.findBRecordByCustomerId(bUsers.getId());
        } else {
            bRecords = bRecordRepository.findBRecordBySellerId(bUsers.getId());
        }
        httpSession.setAttribute("BRecords", bRecords);
        return "redirect:/bMenu";
    }

    @PostMapping("/addValue")
    public String addValue(@RequestParam Integer sellerId,
                           @RequestParam Integer amount,
                           HttpSession httpSession) {
        bRecord = new BRecords();
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        BUsers bUsers = (BUsers) httpSession.getAttribute("loginUser");
        bRecord.setSellerId(sellerId);
        bRecord.setCustomerId(sellerId);
        bRecord.setOrderTime(ts);
        bRecord.setAmount(amount);
        bRecord.setComment("Add $" + bRecord.getAmount() + " in " + bUsers.getName() + "'s account");
        bRecordRepository.save(bRecord);
        bUsers.setBalance(bUsers.getBalance() + bRecord.getAmount());
        bUserRepository.save(bUsers);
        httpSession.setAttribute("loginUser", bUsers);
        if (bUsers.getType() == 1) {
            bRecords = bRecordRepository.findAll();
            httpSession.setAttribute("BUsers", bUserRepository.findAll());
        } else if (bUsers.getType() == 2) {
            bRecords = bRecordRepository.findBRecordByCustomerId(bUsers.getId());
        } else {
            bRecords = bRecordRepository.findBRecordBySellerId(bUsers.getId());
        }
        httpSession.setAttribute("BRecords", bRecords);
        return "/admin";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam Integer customerId,
                           @RequestParam Integer amount,
                           HttpSession httpSession) {
        bRecord = new BRecords();
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        BUsers bUsers = (BUsers) httpSession.getAttribute("loginUser");
        bRecord.setSellerId(customerId);
        bRecord.setCustomerId(customerId);
        bRecord.setOrderTime(ts);
        bRecord.setAmount(amount);
        bRecord.setComment("Withdraw $" + amount + " from " + bUsers.getName() + "'s account");
        bRecordRepository.save(bRecord);
        bUsers.setBalance(bUsers.getBalance() - bRecord.getAmount());
        bUserRepository.save(bUsers);
        httpSession.setAttribute("loginUser", bUsers);
        if (bUsers.getType() == 1) {
            bRecords = bRecordRepository.findAll();
            httpSession.setAttribute("BUsers", bUserRepository.findAll());
        } else if (bUsers.getType() == 2) {
            bRecords = bRecordRepository.findBRecordByCustomerId(bUsers.getId());
        } else {
            bRecords = bRecordRepository.findBRecordBySellerId(bUsers.getId());
        }
        httpSession.setAttribute("BRecords", bRecords);
        return "/admin";
    }

    @PutMapping("/update")
    public void update(@RequestBody BRecords bRecords) {
        bRecordRepository.save(bRecords);
    }

}
