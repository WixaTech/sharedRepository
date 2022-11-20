package pl.wixatech.hackyeahbackend.validation.plugin.document;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.stereotype.Component;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationPluginWithInput;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NoFormsPlugin implements ValidationPluginWithInput {

    public static final String FORM = "form";

    @Override
    public ValidationResult validate(PDDocument doc) {
        boolean valid = isValid(doc);
        return ValidationResult.builder()
                .valid(valid)
                .messageErrors(valid ? Collections.emptyList() : List.of("Forms should not be included"))
                .groupName(FORM)
                .build();
    }

    private boolean isValid(PDDocument doc) {
        List<PDField> fields = getPdFields(doc);
        if (fields.size() == 1) {
            String fullyQualifiedName = fields.get(0).getFullyQualifiedName();
            return fullyQualifiedName.contains("Signature");
        }
        return fields.isEmpty();
    }

    private List<PDField> getPdFields(PDDocument doc) {
        PDDocumentCatalog docCatalog = doc.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        List<PDField> fields = new ArrayList<>();
        if (acroForm != null) {
            fields = acroForm.getFields();
        }
        return fields;
    }
}


