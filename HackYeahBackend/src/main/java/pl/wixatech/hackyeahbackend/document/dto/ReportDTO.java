package pl.wixatech.hackyeahbackend.document.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.wixatech.hackyeahbackend.validation.report.ErrorGroup;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ReportDTO {

  private Long id;

  private boolean isSuccess;

  private Instant created;

  private Set<ErrorGroup> errorGroups;

  private Map<String, String> metadata;
}
