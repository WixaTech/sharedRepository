package pl.wixatech.hackyeahbackend.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationField {
    @Id
    @GeneratedValue
    private Long id;

    private String keyName;

    private String content;

    @Enumerated(EnumType.STRING)
    private ValidationFieldType validationFieldType;
}
