package pl.wixatech.hackyeahbackend.validation.report;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "errorgroup")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ErrorGroup {

  public ErrorGroup(String groupName, List<String> messages, boolean recoverable) {
    this.groupName = groupName;
    this.messages = messages;
    this.recoverable = recoverable;
  }

  @Id
  @GeneratedValue
  private Long id;

  private String groupName;

  @ElementCollection
  private List<String> messages;

  private boolean recoverable = false;
}
