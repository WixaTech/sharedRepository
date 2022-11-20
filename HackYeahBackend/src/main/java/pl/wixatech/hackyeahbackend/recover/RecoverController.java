package pl.wixatech.hackyeahbackend.recover;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecoverController {
    private final RecoverService recoverService;

    @PostMapping(path = "/documents/{id}/recover")
    public void recover(@PathVariable Long id) {
        recoverService.recover(id);
    }
}
