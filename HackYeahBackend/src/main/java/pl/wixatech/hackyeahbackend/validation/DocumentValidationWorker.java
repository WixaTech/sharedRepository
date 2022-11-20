package pl.wixatech.hackyeahbackend.validation;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;
import pl.wixatech.hackyeahbackend.document.Document;
import pl.wixatech.hackyeahbackend.document.DocumentService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentValidationWorker {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10, new CustomizableThreadFactory("workerThread"));

    private final DocumentService documentService;
    private final ValidatorEngineService validatorEngineService;

    @Scheduled(fixedDelay = 1000)
    public void execute() {
        log.debug("Checking if there is document to parse");
        List<Document> allNewDocuments = documentService.getAllNewDocuments();
        if (allNewDocuments.isEmpty()) {
            log.debug("No new documents to parse");
            return;
        }
        allNewDocuments.forEach(document -> {
            documentService.documentInProgress(document);
            executorService.execute(() -> validatorEngineService.execute(document));
//            parseDocument(document);
        });
    }


    //TODO FIXME to jest przykład jak mozna działac z dokumentem
    private void parseDocument(Document document) {
//        List<PDPage> pages;
        File file = new File(document.getFilePath());
        PDDocument doc = null;
        try (InputStream inputStream = new FileInputStream(file)) {

            doc = PDDocument.load(inputStream);
//        out.println(doc.getDocumentId());
//        doc.close();
//        log.debug("Parsing");
            PDDocumentCatalog catalog = doc.getDocumentCatalog();
            for (int i = 0; i < catalog.getPages().getCount(); i++) {
                PDPage pdPage = catalog.getPages().get(i);
                PDResources resources = pdPage.getResources();
                PDPageContentStream pdPageContentStream = new PDPageContentStream(doc, pdPage, PDPageContentStream.AppendMode.APPEND, true);
                System.out.println(resources.getFontNames());
                pdPageContentStream.beginText();
                pdPageContentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                pdPageContentStream.moveTextPositionByAmount(100, 700);
                pdPageContentStream.drawString("Hello World");
                pdPageContentStream.endText();

// Make sure that the content stream is closed:
                pdPageContentStream.close();

// Save the results and ensure that the document is properly closed:

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (doc != null) {
                    doc.save("Hello World.pdf");
                    doc.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
