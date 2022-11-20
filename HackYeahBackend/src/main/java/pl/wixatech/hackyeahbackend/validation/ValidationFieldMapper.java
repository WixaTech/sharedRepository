package pl.wixatech.hackyeahbackend.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import pl.wixatech.hackyeahbackend.configuration.ValidationField;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ValidationFieldMapper {

  private final ObjectMapper objectMapper;

  @SneakyThrows
  public Object mapValue(ValidationField validationField) {
    return switch (validationField.getValidationFieldType()) {
      case LIST -> objectMapper.readValue(validationField.getContent(), List.class);
      case STRING -> objectMapper.readValue(validationField.getContent(), String.class);
      case INT -> objectMapper.readValue(validationField.getContent(), Integer.class);
      case DOUBLE -> objectMapper.readValue(validationField.getContent(), Double.class);
      case BOOLEAN -> objectMapper.readValue(validationField.getContent(), Boolean.class);
      default -> throw new IllegalStateException(
          String.format("ValidationFieldType: %s not supported", validationField.getValidationFieldType()));
    };
  }
}
