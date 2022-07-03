package it.ca.telegrambotserver.telegramBot.bot;

import it.ca.telegrambotserver.telegramBot.bot.entity.Start;
import it.ca.telegrambotserver.telegramBot.bot.entity.User;
import it.ca.telegrambotserver.telegramBot.bot.model.ButtonRegister;
import it.ca.telegrambotserver.telegramBot.bot.model.InlineButton;
import it.ca.telegrambotserver.telegramBot.bot.payload.*;
import it.ca.telegrambotserver.telegramBot.bot.repository.AdminRepository;
import it.ca.telegrambotserver.telegramBot.bot.repository.CourseRepository;
import it.ca.telegrambotserver.telegramBot.bot.repository.StartRepository;
import it.ca.telegrambotserver.telegramBot.bot.repository.UserRepository;
import it.ca.telegrambotserver.telegramBot.bot.service.AdminService;
import it.ca.telegrambotserver.telegramBot.bot.service.CourseService;
import it.ca.telegrambotserver.telegramBot.bot.service.StartService;
import it.ca.telegrambotserver.telegramBot.bot.service.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.util.*;

@Service
public class Register extends TelegramLongPollingBot {

    private final StartRepository startRepository;
    private final StartService startService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final AdminRepository adminRepository;
    private final AdminService adminService;

    private static final InlineButton IB = new InlineButton();
    public static final List<ResComment> comments = new ArrayList<>();

    Map<Long, String> isRegister = new HashMap<>();
    Map<Long, String> lastNameUser = new HashMap<>();
    Map<Long, String> firstNameUser = new HashMap<>();Map<Long, String> courses = new HashMap<>();
    Map<Long, String> registerUser = new HashMap<>();
    Map<Long, String> courseName = new HashMap<>();
    Set<Long> statestika = new HashSet<>();
    List<Long> ids = new ArrayList<>();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            SendMessage sendMessage = new SendMessage();
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            sendMessage.setChatId(chatId.toString());
            String text = message.getText();
            if (message.hasText()) {
                isComment();
                if (text.equals("/start")) {
                    isAdmin(update, sendMessage);
                } else if (text.equals("Kurslar")) {
                    getRegisterMenu(sendMessage);
                    sendMSG(sendMessage, "kurslar");
                } else if (text.equals("bizbilan aloqa")) {
                    sendMSG(sendMessage, "phoneNumber: " + ButtonRegister.PHONE_NUMBER + "\ntelegrem link: " +
                            ButtonRegister.TELEGRAM_LINK);
                } else if (text.equals("statestika")) {
                    sendMSG(sendMessage, "statestika: " + statestika.size());
                } else if (isRegister.get(chatId).equals("firstName")) {
                    isRegister.put(chatId, "lastName");
                    sendMSG(sendMessage, "famliyangizni kiriting");
                    firstNameUser.put(chatId, text);
                } else if (isRegister.get(chatId).equals("lastName")) {
                    isRegister.put(chatId, "phoneNumber");
                    sendMSG(sendMessage, "kiritng");
                    lastNameUser.put(chatId, text);
                    buttonPhoneNumber(sendMessage);
                } else if (isRegister.get(chatId).equals("phoneNumber")) {
                    buttonInfo(sendMessage);
                    isRegister.remove(chatId);
                    getRegisterMenu(sendMessage);
                    sendMSG(sendMessage, "siz muaffaqiyatle ruyxatdan utdingiz!");
                    User user = new User(chatId, firstNameUser.get(chatId), lastNameUser.get(chatId), text,
                            update.getMessage().getFrom().getFirstName(), update.getMessage().getFrom().getLastName(),
                            update.getMessage().getFrom().getUserName(), courses.get(chatId));
                    userService.saveUser(user);
                    userRepository.save(user);
                } else if (isRegister.get(chatId).equals("addAdmin")) {
                    try {
                        Long aLong = Long.valueOf(text);
                        ReqAdmin start = startService.getStart(aLong);
                        if (start == null) {
                            ReqAdmin user = userService.getUser(chatId);
                            if (user == null) {
                                sendMSG(sendMessage, "siz qushmoqchi bulgan user botga obuna emas tekshirib qaytadan urunib kuring!!");
                            } else if (!adminService.addAdmin(user)) sendMSG(sendMessage, "siz qushmoqchi bulgan admin avvaldan mavjud!");
                        } else if (!adminService.addAdmin(start)) sendMSG(sendMessage, "siz qushmoqchi bulgan admin avvaldan mavjud!");
                    } catch (Exception e) {
                        sendMSG(sendMessage, "siz tugri chatId kiritmadingiz qayta urunib kuring!");
                    }
                } else if (isRegister.get(chatId).equals("courseName")) {
                    courseName.put(chatId, text);
                    sendMSG(sendMessage, "course haqida malumot kiriting!");
                    isRegister.put(chatId, "courseInfo");
                } else if (isRegister.get(chatId).equals("courseInfo")) {
                    courseService.createCourse(new ResCourse(courseName.get(chatId), text));
                    sendMSG(sendMessage, "course muaffaqiyatle qushildi");
                    courseName.remove(chatId);
                } else if (isRegister.get(chatId).equals("deleteCourse")) {
                    if (courseService.deleteBotCourse(text)) {
                        sendMSG(sendMessage, "course Muaffaqiyatle uchirldi");
                    }else sendMSG(sendMessage, "siz kiritga course topilmadi tekshirib qaytadan urunib kuring!");
                }

            } else if (message.hasContact()) {
                Contact contact = message.getContact();
                buttonInfo(sendMessage);
                isRegister.remove(chatId);
                User user = new User(chatId, firstNameUser.get(chatId), lastNameUser.get(chatId), contact.getPhoneNumber(),
                        update.getMessage().getFrom().getFirstName(), update.getMessage().getFrom().getLastName(),
                        update.getMessage().getFrom().getUserName(), courses.get(chatId));
                getRegisterMenu(sendMessage);
                sendMSG(sendMessage, "siz muaffaqiyatle ruyxatdan utdingiz!");
                userService.saveUser(user);
                userRepository.save(user);
            } else if (message.hasPhoto()) {
                List<PhotoSize> photo = message.getPhoto();
                switch (isRegister.get(chatId)) {
                    case "start":
                        commendSend(message.getCaption(), photo.get(0).getFileId(), false);
                        break;
                    case "user":
                        commendSend(message.getCaption(), photo.get(0).getFileId(), true);
                        break;
                    case "all":
                        commendSend(message.getCaption(), photo.get(0).getFileId(), true);
                        commendSend(message.getCaption(), photo.get(0).getFileId(), false);
                        break;
                    default:
                        sendMSG(sendMessage, "?");
                        break;
                }
            }
        } else if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            Long id = update.getCallbackQuery().getFrom().getId();
            Message message = update.getCallbackQuery().getMessage();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(id.toString());
            for (ResCourse resCourse : courseService.courseList())
                if (data.equals(resCourse.getName())) {
                    getRegister(sendMessage, id, data, message);
                    sendMSG(sendMessage, resCourse.getInfo());
                    registerUser.put(id, data);
                }
            for (String button : ButtonRegister.ADMIN_TAN)
                if (data.equals(button)) {
                    if (data.equals("back")) break;
                    sendMSG(sendMessage, "rasim bilan text yuboring ikkalasi birgalikda kelsin!");
                    isRegister.put(id, data);
                    break;
                }
            if ("back".equals(data)) {
                delMsg(message);
                registerUser.remove(id);
                isAdmin(update, sendMessage);
            } else if ("register".equals(data)) {
                delMsg(message);
                sendMSG(sendMessage, "ismingizni kiriting");
                isRegister.put(id, "firstName");
            } else if (data.equals("sendAdvertising")) {
                delMsg(message);
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup(getInlineButtonRows(ButtonRegister.ADMIN_TAN));
                sendMessage.setReplyMarkup(markup);
                sendMSG(sendMessage, "junatmoqchi bulgan bulimni tanlang!");
            } else if (data.equals("addAdmin")) {
                sendMSG(sendMessage, "qushmoqchi bulgan admin chatId sini kiriting!");
                isRegister.put(id, "addAdmin");
            } else if (data.equals("adminList")) {
                sendMSG(sendMessage, getAdmins());
            } else if (data.equals("deleteAdmin")) {
                if (adminService.deleteAdmin(id)) {
                    sendMSG(sendMessage, "admin muaffaqiyatle uchirildi!");
                } else sendMSG(sendMessage, "admin topilmadi!");
            } else if (data.equals("addCourse")) {
                sendMSG(sendMessage, "course nomini kiriting!");
                isRegister.put(id, "courseName");
            } else if (data.equals("deleteCourse")) {
                sendMSG(sendMessage, "uchirmoqchi bulgan course nomini kiriting!");
                isRegister.put(id, "deleteCourse");
            }
        }
    }

    //buttons
    public void isAdmin(Update update, SendMessage sendMessage) {
        Long chatId;
        try {
            chatId = update.getMessage().getChatId();
        }catch (Exception e) {
            chatId = update.getCallbackQuery().getFrom().getId();
        }
        if (chatId.toString().equals(ButtonRegister.ADMIN)) {
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup(getInlineButtonRows(ButtonRegister.ADMIN_BUTTON));
            sendMessage.setReplyMarkup(markup);
            sendMSG(sendMessage, "bulimni tanlang!");
        } else if (adminService.isMinAdmin(chatId)){
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup(getInlineButtonRows(ButtonRegister.MIN_ADMIN));
            sendMessage.setReplyMarkup(markup);
            sendMSG(sendMessage, "bulimni tanlang!");
        } else {
            getRegisterMenu(sendMessage);
            sendMSG(sendMessage, "Assalomu alekum " + update.getMessage().getFrom().getFirstName() + " botimizga hush kelipsiz bulimni tanlang!");
            statestika.add(chatId);
            Start start = new Start(chatId, update.getMessage().getFrom().getFirstName(), update.getMessage().getFrom().getUserName());
            startService.saveStart(start);
            startRepository.save(start);
        }
    }

    public void getRegister(SendMessage sendMessage, Long chatId, String data, Message message) {
        delMsg(message);
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        InlineKeyboardMarkup markup;
        if (!userRepository.existsByChatIdAndCourseIgnoreCase(chatId, data)) {
            markup = new InlineKeyboardMarkup(getInlineButtonRows(ButtonRegister.REGISTER));
            courses.put(chatId, data);
        } else markup = new InlineKeyboardMarkup(getInlineButtonRows(ButtonRegister.BACK));

        sendMessage.setReplyMarkup(markup);

    }

    public void getRegisterMenu(SendMessage message) {
        message.setParseMode(ParseMode.MARKDOWN);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(getInlineButtonRows(getCourses()));
        message.setReplyMarkup(markup);
    }

    public void buttonPhoneNumber(SendMessage message) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        message.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("telifon raqamingizni kiriting");
        keyboardButton.setRequestContact(true);
        keyboardFirstRow.add(keyboardButton);
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMSG(message, "Phone number");
    }

    public List<List<InlineKeyboardButton>> getInlineButtonRows(List<String> data) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        int length = data.size() % 2 != 0 ? data.size() - 1 : data.size();
        for (int i = 0; i < length; i += 2) {
            List<InlineKeyboardButton> inlineButton = new ArrayList<>();
            inlineButton.add(IB.getInlineButton(data.get(i), data.get(i)));
            inlineButton.add(IB.getInlineButton(data.get(i + 1), data.get(i + 1)));
            rows.add(inlineButton);
        }
        if (data.size() % 2 != 0) {
            String text = data.get(data.size() - 1);
            rows.add(Collections.singletonList(IB.getInlineButton(text, text)));
        }
        return rows;
    }

    public static void buttonInfo(SendMessage sendMessage) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Kurslar");
        row.add("statestika");
        KeyboardRow row1 = new KeyboardRow();
        row1.add("bizbilan aloqa");
        keyboard.add(row);
        keyboard.add(row1);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        keyboardMarkup.setOneTimeKeyboard(true);
        sendMessage.setReplyMarkup(keyboardMarkup);
    }

    public List<String> getCourses() {
        List<String> courses = new ArrayList<>();
        for (ResCourse resCourse : courseService.courseList()) courses.add(resCourse.getName());
        return courses;
    }

    //message
    public void sendMSG(SendMessage message, String text) {
        try {
            message.setText(text);
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("not execute");
        }
    }

    public void delMsg(Message message) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(message.getChatId().toString());
        deleteMessage.setMessageId(message.getMessageId());
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            System.err.println("no del");
        }
    }

    public void sendPhotoMsg(String chatId, String text, String photo, boolean isFile) {
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(chatId);
        sendPhotoRequest.setCaption(text);
        if (isFile) {
            InputFile file = new InputFile();
            file.setMedia(photo);
            sendPhotoRequest.setPhoto(file);
        }else {
            sendPhotoRequest.setPhoto(new InputFile(new File(photo)));
        }
        try {
            execute(sendPhotoRequest);
        } catch (TelegramApiException e) {
            System.err.println("not execute");
        }
    }

    //comment
    public void commendSend(String text, String photoId, boolean isRegister) {
        if (isRegister) {
            List<ResUsers> odamlar = userService.getOdamlar();
            for (ResUsers resUsers : odamlar) {
                Long chatId2 = resUsers.getChatId();
                if (isId(chatId2)) sendPhotoMsg(chatId2.toString(), text, photoId, true);
            }
        } else {
            List<ResStart> odamlar1 = startService.getStart();
            for (ResStart resStart : odamlar1) {
                if (isId(resStart.getChatId()))
                    sendPhotoMsg(String.valueOf(resStart.getChatId()), text, photoId, true);
            }
        }
    }

    public void isComment() {
        if (userService.isAdd && comments.get(0) != null && comments.get(0) != null) {
            List<ResUsers> odamlar = userService.getOdamlar();
            for (ResUsers resUsers : odamlar) {
                Long chatId2 = resUsers.getChatId();
                if (isId(chatId2))
                    sendPhotoMsg(chatId2.toString(), comments.get(0).getText(), comments.get(0).getRasm(), false);
            }
            userService.isAdd = false;
            comments.remove(0);
            deleteIds();
        }
        if (startService.isAdd1 && comments.get(0) != null && comments.get(0) != null) {
            List<ResStart> odamlar1 = startService.getStart();
            for (ResStart resStart : odamlar1) {
//                        sendPhotoMsg(String.valueOf(chatId), "text","D:\\java.png");
                if (isId(resStart.getChatId()))
                    sendPhotoMsg(String.valueOf(resStart.getChatId()), comments.get(0).getText(), comments.get(0).getRasm(), false);
            }
            startService.isAdd1 = false;
            comments.remove(0);
            deleteIds();
        }
    }

    //unique id;
    public boolean isId(Long id) {
        for (Long outId : ids) if (outId.equals(id)) return false;
        ids.add(id);
        return true;
    }

    //delete
    public void deleteIds() {
        while (ids.size() != 0) for (Long id : ids) ids.remove(id);
    }

    //bots
    @Override
    public String getBotUsername() {
        return ButtonRegister.BOT_USER_NAME;
    }
    @Override
    public String getBotToken() {
        return ButtonRegister.BOT_TOKEN;
    }

    public String getAdmins() {
        String admins = "";
        for (ReqAdmin reqAdmin : adminService.getAdminList()) {
            admins += "firstName: " + reqAdmin.getFirstName() + "\nusername " + reqAdmin.getUsername() + "\nchatId: " + reqAdmin.getChatId() + "\n\n";
        }
        return admins;
    }

    //Constructors
    public AdminRepository getAdminRepository() {
        return adminRepository;
    }

    public CourseRepository getCourseRepository() {
        return courseRepository;
    }

    public AdminService getAdminService() {
        return adminService;
    }

    public Register(StartRepository startRepository, StartService startService, UserRepository userRepository, UserService userService, CourseService courseService, CourseRepository courseRepository, AdminRepository adminRepository, AdminService adminService) {
        this.startRepository = startRepository;
        this.startService = startService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.courseService = courseService;
        this.courseRepository = courseRepository;
        this.adminRepository = adminRepository;
        this.adminService = adminService;
    }
}