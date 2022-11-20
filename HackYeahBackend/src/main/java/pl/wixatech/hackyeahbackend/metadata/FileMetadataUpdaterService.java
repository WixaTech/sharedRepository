package pl.wixatech.hackyeahbackend.metadata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.stereotype.Service;
import pl.wixatech.hackyeahbackend.document.Document;
import pl.wixatech.hackyeahbackend.document.DocumentService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static pl.wixatech.hackyeahbackend.document.DocumentMetadataService.ADDRESSEE_CITY;
import static pl.wixatech.hackyeahbackend.document.DocumentMetadataService.ADDRESSEE_NAME;
import static pl.wixatech.hackyeahbackend.document.DocumentMetadataService.ADDRESSEE_POST_CODE;
import static pl.wixatech.hackyeahbackend.document.DocumentMetadataService.ADDRESSEE_STREET;
import static pl.wixatech.hackyeahbackend.document.DocumentMetadataService.ADDRESSEE_SURNAME;
import static pl.wixatech.hackyeahbackend.document.DocumentMetadataService.CASE_NUMBER;
import static pl.wixatech.hackyeahbackend.document.DocumentMetadataService.SENDER_CITY;
import static pl.wixatech.hackyeahbackend.document.DocumentMetadataService.SENDER_NAME;
import static pl.wixatech.hackyeahbackend.document.DocumentMetadataService.SENDER_POST_CODE;
import static pl.wixatech.hackyeahbackend.document.DocumentMetadataService.SENDER_STREET;
import static pl.wixatech.hackyeahbackend.document.DocumentMetadataService.SENDER_SURNAME;
import static pl.wixatech.hackyeahbackend.document.DocumentMetadataService.UNP;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileMetadataUpdaterService {
    private final DocumentService documentService;

    public void updateMetadata(Document document) {
        try (PDDocument doc = findPdDocument(document)) {
            if (doc == null) {
                log.error("Document is null");
                return;
            }

            PDDocumentInformation info = doc.getDocumentInformation();
            if (info == null) {
                info = new PDDocumentInformation();
            }

            final var documentMetadata = document.getDocumentMetadata();

            info.setCustomMetadataValue(ADDRESSEE_NAME, documentMetadata.getOrDefault(ADDRESSEE_NAME, ""));
            info.setCustomMetadataValue(ADDRESSEE_SURNAME, documentMetadata.getOrDefault(ADDRESSEE_SURNAME, ""));
            info.setCustomMetadataValue(ADDRESSEE_STREET, documentMetadata.getOrDefault(ADDRESSEE_STREET, ""));
            info.setCustomMetadataValue(ADDRESSEE_POST_CODE, documentMetadata.getOrDefault(ADDRESSEE_POST_CODE, ""));
            info.setCustomMetadataValue(ADDRESSEE_CITY, documentMetadata.getOrDefault(ADDRESSEE_CITY, ""));

            info.setCustomMetadataValue(SENDER_NAME, documentMetadata.getOrDefault(SENDER_NAME, ""));
            info.setCustomMetadataValue(SENDER_SURNAME, documentMetadata.getOrDefault(SENDER_SURNAME, ""));
            info.setCustomMetadataValue(SENDER_STREET, "test");
            info.setCustomMetadataValue(SENDER_POST_CODE, "test");
            info.setCustomMetadataValue(SENDER_CITY, "test");

            info.setCustomMetadataValue(UNP, documentMetadata.getOrDefault(UNP, ""));
            info.setCustomMetadataValue(CASE_NUMBER, documentMetadata.getOrDefault(CASE_NUMBER, ""));

            // TODO: podpis???
            info.setCustomMetadataValue("signature_data", "test");

            doc.save(document.getFilePath());
            log.info("Metadata added to file: {}", document.getFilePath());

        } catch (IOException e) {
            documentService.error(document);
            throw new RuntimeException(e);
        }
    }

    private PDDocument findPdDocument(Document document) {
        PDDocument doc = null;
        File file = new File(document.getFilePath());
        try (InputStream inputStream = new FileInputStream(file)) {

            doc = PDDocument.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return doc;
    }
}
