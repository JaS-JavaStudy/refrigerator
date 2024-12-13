package moja.refrigerator.controller.user;

import moja.refrigerator.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("/")
//    public String getMainPage() {
//        return "user Controller";
//    }
}
