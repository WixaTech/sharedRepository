package pl.wixatech.hackyeahbackend.validation.plugin.document;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.stereotype.Component;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationPluginWithInput;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationResult;

import java.awt.Rectangle;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FileContentValidationPlugin implements ValidationPluginWithInput {

  public static String GROUP_NAME = "fileContent";

  // TODO: figure out extracting date
//    public static final List<String> EXPECTED_CONTENT =
//        List.of("UNP", "Sprawa", "Znak sprawy", "Kontakt", "Załączniki", "Korenspondencję otrzymują");
//    

  public static final List<ExpectedContent> FIRST_PAGE_EXPECTED_CONTENT =
      List.of(new ExpectedContent("UNP", new Rectangle(0, 130, 300, 70)),
          new ExpectedContent("rok", new Rectangle(400, 80, 300, 80)),
          new ExpectedContent("Sprawa", new Rectangle(0, 200, 300, 20)),
          new ExpectedContent("Znak sprawy", new Rectangle(0, 210, 300, 20)),
          new ExpectedContent("Kontakt", new Rectangle(0, 230, 300, 80)),
          new ExpectedContent("e-mail", new Rectangle(0, 780, 490, 110)),
          new ExpectedContent("ePUAP", new Rectangle(0, 780, 490, 110))
      );

//  adresat_1
//  Rectangle rect = new Rectangle(300, 300, 300, 80);

  private static record ExpectedContent(String expectedName, Rectangle rectangle) {

  }

  @SneakyThrows
  @Override
  public ValidationResult validate(PDDocument doc) {
    final var errorMessages = FIRST_PAGE_EXPECTED_CONTENT.stream()
        .map(expectedContent -> checkContentOfRegion(expectedContent, doc))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());

    if (!errorMessages.isEmpty()) {
      return ValidationResult.builder()
          .valid(false)
          .groupName(GROUP_NAME)
          .messageErrors(errorMessages)
          .build();
    }

    return ValidationResult.builder()
        .valid(true)
        .groupName(GROUP_NAME)
        .messageErrors(List.of())
        .build();
  }

  @SneakyThrows
  private Optional<String> checkContentOfRegion(ExpectedContent expectedContent, PDDocument doc) {
    PDFTextStripperByArea stripper = new PDFTextStripperByArea();
    stripper.setSortByPosition(true);
    Rectangle rect = expectedContent.rectangle;

    // add the defined rectangle to the area defined above
    stripper.addRegion("class1", rect);

    // extract the page in which the area has to be extracted from
    PDPage firstPage = doc.getPage(0);

    // extract content from the page
    stripper.extractRegions(firstPage);
    String actualTextContent = stripper.getTextForRegion("class1");

    return actualTextContent.contains(expectedContent.expectedName) ? Optional.empty()
        : Optional.of(String.format("Expected content: %s is missing", expectedContent.expectedName));
  }

}
