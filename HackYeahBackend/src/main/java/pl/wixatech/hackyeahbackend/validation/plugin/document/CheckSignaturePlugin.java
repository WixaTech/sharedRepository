package pl.wixatech.hackyeahbackend.validation.plugin.document;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.springframework.stereotype.Component;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationPluginWithInput;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationResult;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CheckSignaturePlugin implements ValidationPluginWithInput {

    public static final String SIGNATURE = "signature";

    @Override
    public ValidationResult validate(PDDocument doc) {
        Optional<PDSignatureField> signature = null;
        try {
            signature = doc.getSignatureFields().stream().findFirst();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean present = signature.isPresent();
        return ValidationResult.builder()
                .valid(present)
                .messageErrors(present ? Collections.emptyList() : List.of("No signature included"))
                .groupName(SIGNATURE)
                .build();
    }
}
