package pl.wixatech.hackyeahbackend.validation.plugin;

import org.apache.pdfbox.pdmodel.PDDocument;

public interface ValidationPluginWithInput {
    ValidationResult validate(PDDocument doc);

    default int getPriority() {
        return Integer.MAX_VALUE;
    }
}
