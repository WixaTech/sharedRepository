package pl.wixatech.hackyeahbackend.validation.plugin;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.wixatech.hackyeahbackend.configuration.ConfigService;
import pl.wixatech.hackyeahbackend.configuration.ValidationField;
import pl.wixatech.hackyeahbackend.document.Document;
import pl.wixatech.hackyeahbackend.validation.ValidationFieldMapper;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;


@Component
@Slf4j
@RequiredArgsConstructor
public class FileNameValidationPlugin implements ValidationPlugin {

    public static final String GROUP_NAME = "file_name";

    public static final String FORBIDDEN_CHARS = "forbiden_chars";
    public static final String TRIM = "trim";
    public static final String CHAR_CODE = "char_code";
    public static final String FULL_FILENAME_LENGTH = "full_filename_length";
    public static final String SPACE_BEFORE = "space_before";
    public static final String SPACE_AFTER = "space_after";

    Map<String, BiFunction<String, ValidationField, Optional<String>>> VALIDATION_FUNCTION_BY_PARAMETER_NAME =
        Map.of(FORBIDDEN_CHARS, validateForbiddenChars(),
            TRIM, validateTrailingSpaces(),
            CHAR_CODE, validateCharCode(),
            FULL_FILENAME_LENGTH, validateFullFileNameLength());

    private final ConfigService configService;
    private final ValidationFieldMapper fieldMapperService;

    @SneakyThrows
    @Override
    public ValidationResult validate(Document document) {
        final var fileName = getFileName(document);

        log.debug("Validating file name of document with id {} and fileName {}", document.getId(), fileName);

        final var configValidation = configService.getConfigValidationByGroup(GROUP_NAME);
        final var validationFieldByName = configValidation.getValidationFieldByName();

        final var errorMessages = VALIDATION_FUNCTION_BY_PARAMETER_NAME.entrySet().stream()
            .map(e -> {
                ValidationField field = validationFieldByName.get(e.getKey());
                return e.getValue().apply(fileName, field);
            }).filter(Optional::isPresent)
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

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    private String getFileName(Document document) {
        return Paths.get(document.getFilePath()).getFileName().toString();
    }

    private BiFunction<String, ValidationField, Optional<String>> validateForbiddenChars() {
        return (fileName, validationField) -> {
            final List<String> forbiddenChars = (List<String>) fieldMapperService.mapValue(validationField);

            return forbiddenChars.stream().anyMatch(fileName::contains) ? Optional.of(
                String.format("File name %s contains some of the forbidden characters: %s", fileName, forbiddenChars))
                : Optional.empty();
        };
    }

    private BiFunction<String, ValidationField, Optional<String>> validateTrailingSpaces() {
        return (fileName, validationField) -> {
            final List<String> trailingSettings = (List<String>) fieldMapperService.mapValue(validationField);
            String errorMessage = "";

            if (trailingSettings.contains(SPACE_BEFORE)) {
                String ltrim = StringUtils.stripStart(fileName, null);

                if (!ltrim.equals(fileName)) {
                    errorMessage += "Beginning of the file name contains trailing spaces. ";
                }
            }

            if (trailingSettings.contains(SPACE_AFTER)) {
                String rtrim = StringUtils.stripEnd(fileName, null);

                if (!rtrim.equals(fileName)) {
                    errorMessage += "End of the file name contains trailing spaces. ";
                }

            }

            return errorMessage.isEmpty() ? Optional.empty() : Optional.of(errorMessage);
        };
    }


    private BiFunction<String, ValidationField, Optional<String>> validateCharCode() {
        return (fileName, validationField) -> {
            return Optional.empty(); // TODO: try to validate it later
        };
    }


    private BiFunction<String, ValidationField, Optional<String>> validateFullFileNameLength() {
        return (fileName, validationField) -> {
            final int maximumLength = (Integer) fieldMapperService.mapValue(validationField);

            return fileName.length() > maximumLength ? Optional.of(
                String.format("File name %s is longer than allowed %d characters", fileName, maximumLength))
                : Optional.empty();
        };
    }
}
