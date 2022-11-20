package pl.wixatech.hackyeahbackend.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import pl.wixatech.hackyeahbackend.document.Document;
import pl.wixatech.hackyeahbackend.document.DocumentService;
import pl.wixatech.hackyeahbackend.document.DocumentStatus;
import pl.wixatech.hackyeahbackend.metadata.FileMetadataUpdaterService;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationPlugin;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationPluginWithInput;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidatorEngineService {
    private final List<ValidationPlugin> validationPluginList;
    private final List<ValidationPluginWithInput> validationWithDocPluginList;
    private final DocumentService documentService;
    private final FileMetadataUpdaterService fileMetadataUpdaterService;
    private final FormatValidator formatValidator;
    public void execute(Document document) {
        //TODO add validation for checking formats

        ValidationResult firstValidate = formatValidator.validate(document);
        if (!firstValidate.isValid()) {
            documentService.addReportToDocument(document, List.of(firstValidate));
            return;
        }


        List<ValidationResult> validationResults = validationPluginList.stream()
            .sorted(Comparator.comparing(ValidationPlugin::getPriority))
            .map(validationPlugin -> validationPlugin.validate(document))
            .collect(Collectors.toList());

        List<ValidationResult> validationResultsWithDocument;

        // TODO: check if its restricted

        try (PDDocument doc = findPdDocument(document)) {
            if (doc == null) {
                log.error("Document is null");
            }

            validationResultsWithDocument = validationWithDocPluginList.stream()
                .sorted(Comparator.comparing(ValidationPluginWithInput::getPriority))
                .map(validationPluginWithInput -> validationPluginWithInput.validate(doc))
                .toList();
            validationResults.addAll(validationResultsWithDocument);

        } catch (IOException e) {
            error(document);
            return;
        }

        documentService.addReportToDocument(document, validationResults);
        Document byId = documentService.getById(document.getId());
        if (byId.getDocumentStatus().equals(DocumentStatus.VALID)) {
            try (PDDocument doc = findPdDocument(byId)) {
                documentService.addMetadataToDocument(byId, doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileMetadataUpdaterService.updateMetadata(byId);
        }
    }

    private PDDocument findPdDocument(Document document) throws IOException {
        PDDocument doc = null;
        File file = new File(document.getFilePath());
        try (InputStream inputStream = new FileInputStream(file)) {

            doc = PDDocument.load(inputStream);
        } catch (IOException e) {
            throw new IOException();
        }
        return doc;
    }

    private void error(Document document) {
        documentService.error(document);
        documentService.addReportToDocument(document, List.of(ValidationResult.builder()
                .valid(false)
                .groupName("fileCoruption")
                .messageErrors(List.of("This file is corrupted"))
                .build()));
        return;
    }
}
