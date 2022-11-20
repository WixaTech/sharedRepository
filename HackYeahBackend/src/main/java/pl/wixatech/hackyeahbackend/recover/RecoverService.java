package pl.wixatech.hackyeahbackend.recover;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDNonTerminalField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTerminalField;
import org.springframework.stereotype.Service;
import pl.wixatech.hackyeahbackend.configuration.ConfigService;
import pl.wixatech.hackyeahbackend.configuration.ConfigValidation;
import pl.wixatech.hackyeahbackend.configuration.ValidationField;
import pl.wixatech.hackyeahbackend.document.Document;
import pl.wixatech.hackyeahbackend.document.DocumentService;
import pl.wixatech.hackyeahbackend.document.DocumentStatus;
import pl.wixatech.hackyeahbackend.validation.ValidationFieldMapper;
import pl.wixatech.hackyeahbackend.validation.plugin.FileNameValidationPlugin;
import pl.wixatech.hackyeahbackend.validation.report.ErrorGroup;
import pl.wixatech.hackyeahbackend.validation.report.Report;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static pl.wixatech.hackyeahbackend.validation.plugin.FileNameValidationPlugin.GROUP_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecoverService {

    private final DocumentService documentService;
    private final ConfigService configService;
    private final ValidationFieldMapper validationFieldMapper;


    public void recover(Long documentId) {
        Document document = documentService.getById(documentId);
        Report report = documentService.getRecentReport(documentId);
        Optional<ErrorGroup> maybeErrorWithFilename = report.getErrorGroups().stream().filter(errorGroup -> errorGroup.getGroupName().equals(GROUP_NAME)).findAny();
        maybeErrorWithFilename.ifPresent(errorGroup -> recoverFileName(errorGroup.getMessages(), document));
    }

    @SneakyThrows
    private void recoverFileName(List<String> messages, Document document) {
        ConfigValidation configValidationByGroup = configService.getConfigValidationByGroup(GROUP_NAME);
        if (messages.stream().anyMatch(m -> m.contains("forbidden characters"))) {
            ValidationField validationField = configValidationByGroup.getValidationFieldByName().get(FileNameValidationPlugin.FORBIDDEN_CHARS);
            List<String> charList = (List<String>) validationFieldMapper.mapValue(validationField);

            File file = new File(document.getFilePath());
            String name = file.getName();
            String replace = name;
            for (String character : charList) {
                replace = replace.replace(character, "");
            }

            ifFileExistDelete(replace);

            Files.move(Path.of(document.getFilePath()), Path.of(replace));
            document.setFilePath(replace);

            PDDocument dc = PDDocument.load(new File(replace));

            removeSignature(dc);
            dc.save(replace);
            dc.close();

        }
        document.setDocumentStatus(DocumentStatus.UNVERIFIED);

        documentService.save(document);
    }

    private void ifFileExistDelete(String replace) {
        File f = new File(replace);
        f.delete();
    }

    void removeSignature(PDDocument document) throws IOException {
        PDDocumentCatalog documentCatalog = document.getDocumentCatalog();
        PDAcroForm acroForm = documentCatalog.getAcroForm();

        if (acroForm == null) {
            log.error("No form defined.");
            return;
        }

        PDField targetField = null;

        for (PDField field : acroForm.getFieldTree()) {
            if ("Signature1".equals(field.getFullyQualifiedName())) {
                targetField = field;
                break;
            }
        }
        if (targetField == null) {
            log.error("Form does not contain field with given name.");
            return;
        }

        PDNonTerminalField parentField = targetField.getParent();
        if (parentField != null) {
            List<PDField> childFields = parentField.getChildren();
            boolean removed = false;
            for (PDField field : childFields) {
                if (field.getCOSObject().equals(targetField.getCOSObject())) {
                    removed = childFields.remove(field);
                    parentField.setChildren(childFields);
                    break;
                }
            }
            if (!removed)
                log.error("Inconsistent form definition: Parent field does not reference the target field.");
        } else {
            List<PDField> rootFields = acroForm.getFields();
            boolean removed = false;
            for (PDField field : rootFields) {
                if (field.getCOSObject().equals(targetField.getCOSObject())) {
                    removed = rootFields.remove(field);
                    break;
                }
            }
            if (!removed)
                log.error("Inconsistent form definition: Root fields do not include the target field.");
        }

        removeWidgets(targetField);

    }

    void removeWidgets(PDField targetField) throws IOException {
        if (targetField instanceof PDTerminalField) {
            List<PDAnnotationWidget> widgets = targetField.getWidgets();
            for (PDAnnotationWidget widget : widgets) {
                PDPage page = widget.getPage();
                if (page != null) {
                    List<PDAnnotation> annotations = page.getAnnotations();
                    boolean removed = false;
                    for (PDAnnotation annotation : annotations) {
                        if (annotation.getCOSObject().equals(widget.getCOSObject())) {
                            removed = annotations.remove(annotation);
                            break;
                        }
                    }
                    if (!removed)
                        log.error("Inconsistent annotation definition: Page annotations do not include the target widget.");
                } else {
                    log.error("Widget annotation does not have an associated page; cannot remove widget.");
                }
            }
        } else if (targetField instanceof PDNonTerminalField) {
            List<PDField> childFields = ((PDNonTerminalField) targetField).getChildren();
            for (PDField field : childFields)
                removeWidgets(field);
        } else {
            log.error("Target field is neither terminal nor non-terminal; cannot remove widgets.");
        }
    }

}
