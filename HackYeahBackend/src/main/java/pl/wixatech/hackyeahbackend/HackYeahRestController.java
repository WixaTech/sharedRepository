package pl.wixatech.hackyeahbackend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qr")
public class HackYeahRestController {

  @GetMapping(value = "/test") 
  public String test() {
    return "test endpoint";
  }
}
