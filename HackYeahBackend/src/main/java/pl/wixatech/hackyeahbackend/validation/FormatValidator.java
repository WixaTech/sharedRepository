package pl.wixatech.hackyeahbackend.validation;

import org.springframework.stereotype.Component;
import pl.wixatech.hackyeahbackend.document.Document;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class FormatValidator {
    public ValidationResult validate(Document document) {
        String probeContentType = "";
        try {
            probeContentType = Files.probeContentType(Path.of(document.getFilePath()));
            System.out.println(probeContentType);
        } catch (IOException e) {
            return ValidationResult.builder()
                    .valid(false)
                    .messageErrors(List.of("Fatal error"))
                    .groupName("fatal")
                    .build();
        }
        boolean valid = probeContentType.equals("application/pdf");
        return ValidationResult.builder()
                .valid(valid)
                .messageErrors(List.of("Invalid format of file"))
                .groupName("fileFormat")
                .build();
    }
}
