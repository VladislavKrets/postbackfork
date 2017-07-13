package online.omnia.postback.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by lollipop on 13.07.2017.
 */
@Controller
public class PostbackController {

    @RequestMapping(value = "/{postback_url}", method = RequestMethod.GET)
    public String sendPostback(@PathVariable(name = "postback_url") String postbackUrl, Model model){
        System.out.println(postbackUrl);
        return "SendPostback";
    }
}
