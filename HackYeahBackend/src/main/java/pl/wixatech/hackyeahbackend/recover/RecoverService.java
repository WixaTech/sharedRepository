package pl.wixatech.hackyeahbackend.recover;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static pl.wixatech.hackyeahbackend.validation.plugin.FileNameValidationPlugin.GROUP_NAME;

@Service
@RequiredArgsConstructor
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

            Files.move(Path.of(document.getFilePath()), Path.of(replace));
            document.setFilePath(replace);

        }
        document.setDocumentStatus(DocumentStatus.UNVERIFIED);

        documentService.save(document);
    }

}
