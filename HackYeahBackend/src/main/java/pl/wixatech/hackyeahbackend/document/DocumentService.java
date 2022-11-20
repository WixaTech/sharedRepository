package pl.wixatech.hackyeahbackend.document;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationResult;
import pl.wixatech.hackyeahbackend.validation.report.Report;
import pl.wixatech.hackyeahbackend.validation.report.ReportService;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DocumentService {

  private final ReportService reportService;
  private final DocumentMetadataService metadataService;

  private final DocumentRepository documentRepository;

  @Transactional
  public void saveDocument(final String contentType, String filePath) {
    documentRepository.save(new Document(contentType, filePath));
  }

  @Transactional(readOnly = true)
  public List<Document> getAllDocuments() {
    return documentRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Report getRecentReport(Long documentId) {
    Comparator<Report> comparator = Comparator.comparing(Report::getCreated);

    return documentRepository.findById(documentId).orElseThrow().getReports().stream()
        .max(comparator).orElseThrow();
  }

  @Transactional(readOnly = true)
  public Document getById(long l) {
    return documentRepository.findById(l).orElseThrow();
  }

  @Transactional(readOnly = true)
  public List<Document> getAllNewDocuments() {
    return documentRepository.findAllByDocumentStatus(DocumentStatus.UNVERIFIED);
  }

  @Transactional
  public void documentInProgress(Document document) {
    document.setParseStartAt(Instant.now()); // TODO: check if it's set
    document.setDocumentStatus(DocumentStatus.IN_PROGRESS);
  }

  @Transactional
  public Report addReportToDocument(Document document, List<ValidationResult> validationResults) {
    final var documentInSession = documentRepository.findById(document.getId()).orElseThrow();
    boolean validationFailed = validationResults.stream().anyMatch(validationResult -> !validationResult.isValid());

    if (validationFailed) {
      validationFailed(documentInSession);
    } else {
      completed(documentInSession);
    }

    final var report = reportService.createReport(validationResults);
    documentInSession.getReports().add(report);
    documentRepository.save(documentInSession);

    return report;
  }

  @Transactional
  public Document addMetadataToDocument(Document document, PDDocument doc) {
    final var metadata = metadataService.getMetadataFromDoc(doc);
    document.setDocumentMetadata(metadata);
    return documentRepository.save(document);
  }

  private void validationFailed(Document document) {
    document.setDocumentStatus(DocumentStatus.INVALID);
  }

  private void completed(Document document) {
    document.setDocumentStatus(DocumentStatus.VALID);
  }

  public void error(Document document) {
    document.setDocumentStatus(DocumentStatus.PROCESSING_ERROR);
  }

  public void save(Document document) {
    documentRepository.save(document);
  }

  public void deleteAll() {
    documentRepository.deleteAll();

  }
}
