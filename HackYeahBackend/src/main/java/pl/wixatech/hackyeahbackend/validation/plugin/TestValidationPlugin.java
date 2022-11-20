package pl.wixatech.hackyeahbackend.validation.plugin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.wixatech.hackyeahbackend.document.Document;

import java.util.List;


@Component
@Slf4j
public class TestValidationPlugin implements ValidationPlugin{
    @Override
    public ValidationResult validate(Document id) {
        log.debug("Validating {}", id);
        return ValidationResult.builder()
                .valid(true)
                .groupName("Font")
                .messageErrors(List.of())
                .build();
    }

}
