package pl.wixatech.hackyeahbackend.validation.report;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "report")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Report {

  public Report(boolean isSuccess, Set<ErrorGroup> errorGroups) {
    this.isSuccess = isSuccess;
    this.errorGroups = errorGroups;
  }

  @Id
  @GeneratedValue
  private Long id;

  private boolean isSuccess;

  @CreationTimestamp
  private Instant created;

  @OneToMany(cascade = CascadeType.ALL)
  @Fetch(FetchMode.JOIN)
  private Set<ErrorGroup> errorGroups;
}
