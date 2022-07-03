package it.ca.telegrambotserver.telegramBot.bot.service;

import it.ca.telegrambotserver.telegramBot.bot.entity.Admin;
import it.ca.telegrambotserver.telegramBot.bot.payload.ReqAdmin;
import it.ca.telegrambotserver.telegramBot.bot.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    AdminRepository adminRepository;

    public boolean addAdmin(ReqAdmin reqAdmin) {
        if (!adminRepository.existsByChatIdEquals(reqAdmin.getChatId())) {
            Admin admin = new Admin();
            admin.setFirstName(reqAdmin.getFirstName());
            admin.setUsername(reqAdmin.getUsername());
            admin.setChatId(reqAdmin.getChatId());
            adminRepository.save(admin);
            return true;
        }
        return false;
    }

    public List<ReqAdmin> getAdminList() {
        List<Admin> adminList = adminRepository.findAll();
        List<ReqAdmin> reqAdminList = new ArrayList<>();
        for (Admin admin : adminList) {
            reqAdminList.add(new ReqAdmin(admin.getFirstName(), admin.getUsername(), admin.getChatId()));
        }
        return reqAdminList;
    }

    public boolean deleteAdmin(Long chatId) {
        try {
            adminRepository.deleteById(adminRepository.findAdminByChatId(chatId).getId());
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public boolean isMinAdmin(Long chatId) {
        try {
            adminRepository.findAdminByChatId(chatId);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
