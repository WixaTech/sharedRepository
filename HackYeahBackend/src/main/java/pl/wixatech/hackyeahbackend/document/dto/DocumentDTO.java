package pl.wixatech.hackyeahbackend.document.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import pl.wixatech.hackyeahbackend.document.DocumentStatus;
import pl.wixatech.hackyeahbackend.validation.report.Report;

import java.time.Instant;
import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class DocumentDTO {

  private final Long id;

  private final DocumentStatus status;

  private final Instant addedDate;

  private final String fileName;

  private final Set<Report> reports;

}
