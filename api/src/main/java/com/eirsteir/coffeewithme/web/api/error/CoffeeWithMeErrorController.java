package com.eirsteir.coffeewithme.web.api.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;

@Controller
public class CoffeeWithMeErrorController implements ErrorController {

    // TODO: 01.05.2020 Commenting this out until it is needed
//    @RequestMapping("/error")
//    @ResponseBody
//    public String error(HttpServletRequest request) {
//        String message = (String) request.getSession().getAttribute("error.message");
//        request.getSession().removeAttribute("error.message");
//        return message;
//    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
