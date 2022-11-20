package pl.wixatech.hackyeahbackend.document;

import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DocumentMetadataService {

  public static final String ADDRESSEE_NAME = "recipient_name";
  public static final String ADDRESSEE_SURNAME = "recipient_surname";
  public static final String ADDRESSEE_STREET = "recipient_street";
  public static final String ADDRESSEE_POST_CODE = "recipient_post_code";
  public static final String ADDRESSEE_CITY = "recipient_city";
  public static final String SENDER_NAME = "sender_name";
  public static final String SENDER_SURNAME = "sender_surname";
  public static final String SENDER_STREET = "sender_street";
  public static final String SENDER_POST_CODE = "sender_post_code";
  public static final String SENDER_CITY = "sender_city";
  public static final String UNP = "unp";
  public static final String CASE_NUMBER = "case_number";

  private static final Set<String> expectedFooterMetadata = Set.of("e-mail: ", "ePUAP ");
  public static final String SIGNATURE_EXTENSION = "signature_extension";
  public static final String SIGNATURE_DATE = "signature_date";


  public Map<String, String> getMetadataFromDoc(PDDocument pdfDocument) {
    Map<String, String> footerMetadata = extractMetadataFromFooter(pdfDocument);
    Map<String, String> headerMetadata = extractMetadataFromHeader(pdfDocument);
    Map<String, String> otherMetadata = extractOtherMetadata(pdfDocument);
    Map<String, String> signatureMetadata = extractSignatureMetadata(pdfDocument);

    final var resultMetadata = new HashMap<String, String>();
    resultMetadata.putAll(footerMetadata);
    resultMetadata.putAll(headerMetadata);
    resultMetadata.putAll(otherMetadata);
    resultMetadata.putAll(signatureMetadata);

    return resultMetadata;
  }

  private Map<String, String> extractSignatureMetadata(PDDocument pdfDocument) {
    Map<String, String> toReturn = new HashMap<>();
    try {
      PDSignatureField pdSignatureField = pdfDocument.getSignatureFields().stream().findFirst().orElseThrow();
      PDSignature value = pdSignatureField.getValue();
      if (value == null) {
        return new HashMap<>();
      }
      toReturn.put(SIGNATURE_EXTENSION, value.getSubFilter());
      Calendar signDate = value.getSignDate();
      if (signDate != null) {
        Date time = signDate.getTime();
        toReturn.put(SIGNATURE_DATE, time.toString());
      }

    } catch (IOException e) {
      return new HashMap<>();
    }

    return toReturn;
  }


  // adresat (Imię i nazwisko/nazwa, adres pocztowy, NIP/PESEL)
  // nadawca ( nazwa, adres pocztowy, e-mail, skrytka e-PUAP, nr telefonu)
  // UNP
  // numer sprawy
  // data na piśmie
  // imię i nawisko osoby podpisującej doka
  private Map<String, String> extractMetadataFromFooter(PDDocument pdfDocument) {
    final var footerRectangle = new Rectangle(0, 780, 530, 110);

    String actualTextContent = extractTextFromRegion(pdfDocument, footerRectangle);

    return Arrays.stream(actualTextContent.split("\n"))
        .flatMap(line -> Arrays.stream(line.split("●")))
        .filter(value -> expectedFooterMetadata.stream().anyMatch(value::contains))
        .collect(Collectors.toMap(fragment -> expectedFooterMetadata.stream().filter(fragment::contains).findAny().orElseThrow(),
            Function.identity())).entrySet()
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().replace(entry.getKey(), "")));
  }

  private Map<String, String> extractMetadataFromHeader(PDDocument pdfDocument) {
    Map<String, String> adreseeMetadata = getAdreseeMetadata(pdfDocument);
    Map<String, String> senderMetadata = getSenderMetadata(pdfDocument);

    final var resultMap = new HashMap<String, String>();
    resultMap.putAll(adreseeMetadata);
    resultMap.putAll(senderMetadata);

    return resultMap;
  }

  private Map<String, String> getAdreseeMetadata(PDDocument pdfDocument) {
    final var adreseeRectangle = new Rectangle(300, 180, 300, 280);
    final var adreseeText = extractTextFromRegion(pdfDocument, adreseeRectangle);

    final var adreseeParts = adreseeText.split("\n");

    final var adreseeMetadata = new HashMap<String, String>();

    if (adreseeParts.length >= 3) {
      final var nameAndSurname = adreseeParts[0];
      final var nameAndSurnameParts = nameAndSurname.split(" ");
      if (nameAndSurnameParts.length >= 2) {
        adreseeMetadata.put(ADDRESSEE_NAME, nameAndSurnameParts[0]);
        adreseeMetadata.put(ADDRESSEE_SURNAME, nameAndSurnameParts[1]);
      }

      final var street = adreseeParts[1];
      final var postalCodeAndCity = adreseeParts[2];
      final var postalCodeAndCityParts = postalCodeAndCity.split(" ");
      if (postalCodeAndCityParts.length >= 2) {
        adreseeMetadata.put(ADDRESSEE_POST_CODE, postalCodeAndCityParts[0]);
        adreseeMetadata.put(ADDRESSEE_CITY, postalCodeAndCityParts[1]);
      }

      adreseeMetadata.put(ADDRESSEE_STREET, street);
    }

    return adreseeMetadata;
  }

  private Map<String, String> getSenderMetadata(PDDocument pdfDocument) {
    final var senderRectangle = new Rectangle(0, 230, 300, 80);
    final var senderText = extractTextFromRegion(pdfDocument, senderRectangle);

    final var senderWithoutContact = senderText.replace("Kontakt: ", "");
    final var senderTextParts = senderWithoutContact.split("\n");

    final var senderMetadata = new HashMap<String, String>();

    if (senderTextParts.length >= 4) {
      final var senderTextPartsExtract = senderTextParts[0].split(" ");

      if (senderTextPartsExtract.length >= 2) {
        senderMetadata.put(SENDER_NAME, senderTextPartsExtract[0]);
        senderMetadata.put(SENDER_SURNAME, senderTextPartsExtract[1]);
      }
    }


    return senderMetadata;
  }

  private Map<String, String> extractOtherMetadata(PDDocument pdfDocument) {
    final var otherMetadata = new HashMap<String, String>();

    final var unpRectangle = new Rectangle(0, 130, 300, 70);
    final var unpText = extractTextFromRegion(pdfDocument, unpRectangle);
    final var unpValue = unpText.replace("UNP: ", "").replace("\n", "").trim();
    otherMetadata.put(UNP, unpValue);

    final var caseNumberRectangle = new Rectangle(0, 210, 300, 20);
    final var caseNumberText = extractTextFromRegion(pdfDocument, caseNumberRectangle);
    final var caseNumberValue = caseNumberText.replace("Znak sprawy:", "").trim();
    otherMetadata.put(CASE_NUMBER, caseNumberValue);

    return otherMetadata;
  }

  @SneakyThrows
  private String extractTextFromRegion(PDDocument pdfDocument, Rectangle footerRectangle) {
    PDFTextStripperByArea stripper = new PDFTextStripperByArea();
    stripper.setSortByPosition(true);

    // add the defined rectangle to the area defined above
    stripper.addRegion("class1", footerRectangle);

    // extract the page in which the area has to be extracted from
    PDPage firstPage = pdfDocument.getPage(0);

    // extract content from the page
    stripper.extractRegions(firstPage);
    return stripper.getTextForRegion("class1");
  }
}
