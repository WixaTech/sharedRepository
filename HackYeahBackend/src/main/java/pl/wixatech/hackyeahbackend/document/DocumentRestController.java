package pl.wixatech.hackyeahbackend.document;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wixatech.hackyeahbackend.document.dto.DocumentDTO;
import pl.wixatech.hackyeahbackend.document.dto.ReportDTO;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentRestController {

  private final DocumentService documentService;

  @GetMapping
  public List<DocumentDTO> getAllDocuments() {
    return documentService.getAllDocuments().stream()
        .map(this::convertToDocumentDTO)
        .collect(Collectors.toList());
  }

  // TODO: return 404 when report invalid
  @GetMapping(value = "/{documentId}/recentReport")
  public ReportDTO getRecentReport(@PathVariable("documentId") Long documentId) {
    final var document = documentService.getById(documentId);
    final var recentReport = documentService.getRecentReport(documentId);
    return new ReportDTO(recentReport.getId(), recentReport.isSuccess(), recentReport.getCreated(), recentReport.getErrorGroups(),
            document.getDocumentMetadata());
  }

  @DeleteMapping()
  public void deleteAll() {
    documentService.deleteAll();
  }

  private DocumentDTO convertToDocumentDTO(Document document) {
    String fileName = getFileName(document);
    return new DocumentDTO(document.getId(), document.getDocumentStatus(), document.getCreated(), fileName,
            document.getReports());
  }

  private String getFileName(Document document) {
    return Paths.get(document.getFilePath()).getFileName().toString();
  }
}
