package pl.wixatech.hackyeahbackend.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.wixatech.hackyeahbackend.validation.plugin.FileNameValidationPlugin;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static pl.wixatech.hackyeahbackend.validation.plugin.document.FileParametersPlugin.FILE_PARAMS;
import static pl.wixatech.hackyeahbackend.validation.plugin.document.FileParametersPlugin.FORMAT;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfigInitBean {
    private final ConfigService configService;
    private final ObjectMapper objectMapper;

    @Transactional
    @PostConstruct
    public void init() {
//        ConfigValidation filename = new ConfigValidation();
//        filename.setConfigMap(Map.of("maxsize", "100"));
//        filename.setValidationGroup("filename");
//        configService.add(filename);
//
//        ConfigValidation configValidation = new ConfigValidation();
//        configValidation.setConfigMap(Map.of(
//                "name", "Aerial",
//                "size", "10"
//                )
//        );
//        configValidation.setValidationGroup("font");
//
//        configService.add(configValidation);

        fileNameParams();
        fileParams();
        optimizationParams();
        fontParams();
        lineParams();
        formParams();
        tintParams();
        imagesParams();


//        technicalParams();


    }

    @SneakyThrows
    private void imagesParams() {
        Set<ValidationField> validations = new HashSet<>();
        validations.add(ValidationField.builder()
                .keyName("min_dpi")
                .content(objectMapper.writeValueAsString("150"))
                .validationFieldType(ValidationFieldType.INT)
                .build());
        validations.add(ValidationField.builder()
                .keyName("optimal_dpi_scale_1_to_1")
                .content(objectMapper.writeValueAsString("300"))
                .validationFieldType(ValidationFieldType.INT)
                .build());
        validations.add(ValidationField.builder()
                .keyName("modes")
                .content(objectMapper.writeValueAsString(List.of("CMYK", "8-bit")))
                .validationFieldType(ValidationFieldType.LIST)
                .build());
        addConfig("images", validations);
    }

    @SneakyThrows
    private void tintParams() {
        Set<ValidationField> validations = new HashSet<>();
        validations.add(ValidationField.builder()
                .keyName("tint")
                .content(objectMapper.writeValueAsString("CMYK"))
                .validationFieldType(ValidationFieldType.STRING)
                .build());
        addConfig("tint", validations);
    }

    @SneakyThrows
    private void formParams() {
        Set<ValidationField> validations = new HashSet<>();
        validations.add(ValidationField.builder()
                .keyName("allowed")
                .content(objectMapper.writeValueAsString("false"))
                .validationFieldType(ValidationFieldType.BOOLEAN)
                .build());
        addConfig("form", validations);
    }

    @SneakyThrows
    private void lineParams() {
        Set<ValidationField> validations = new HashSet<>();
        validations.add(ValidationField.builder()
                .keyName("single_color_min_thickness")
                .content(objectMapper.writeValueAsString("0.1"))
                .validationFieldType(ValidationFieldType.DOUBLE)
                .build());
        validations.add(ValidationField.builder()
                .keyName("contra_or_multiple_color_min_thickness")
                .content(objectMapper.writeValueAsString("0.5"))
                .validationFieldType(ValidationFieldType.DOUBLE)
                .build());
        addConfig("lines", validations);
    }

    @SneakyThrows
    private void fontParams() {
        Set<ValidationField> validations = new HashSet<>();
        validations.add(ValidationField.builder()
                .keyName("rule")
                .content(objectMapper.writeValueAsString("embeded_in_doc"))
                .validationFieldType(ValidationFieldType.STRING)
                .build());
        validations.add(ValidationField.builder()
                .keyName("single_color_one_element_font")
                .content(objectMapper.writeValueAsString("5"))
                .validationFieldType(ValidationFieldType.INT)
                .build());
        validations.add(ValidationField.builder()
                .keyName("single_color_two_element_font")
                .content(objectMapper.writeValueAsString("6"))
                .validationFieldType(ValidationFieldType.INT)
                .build());
        validations.add(ValidationField.builder()
                .keyName("multiple_color_one_element_font")
                .content(objectMapper.writeValueAsString("8"))
                .validationFieldType(ValidationFieldType.INT)
                .build());
        validations.add(ValidationField.builder()
                .keyName("multiple_color_two_element_font")
                .content(objectMapper.writeValueAsString("10"))
                .validationFieldType(ValidationFieldType.INT)
                .build());
        addConfig("font", validations);
    }

    @SneakyThrows
    private void optimizationParams() {
        Set<ValidationField> validations = new HashSet<>();
        validations.add(ValidationField.builder()
                .keyName("generation_type")
                .content(objectMapper.writeValueAsString("composite"))
                .validationFieldType(ValidationFieldType.STRING)
                .build());
        validations.add(ValidationField.builder()
                .keyName("to_delete")
                .content(objectMapper.writeValueAsString(List.of("tab", "hyperlink", "bad_links", "inactive_layers")))
                .validationFieldType(ValidationFieldType.LIST)
                .build());

        addConfig("optimization", validations);
    }

    @SneakyThrows
    private void fileParams() {
        Set<ValidationField> validations = new HashSet<>();
        validations.add(ValidationField.builder()
                .keyName("type")
                .content(objectMapper.writeValueAsString("pdf"))
                .validationFieldType(ValidationFieldType.STRING)
                .build());
        validations.add(ValidationField.builder()
                .keyName(FORMAT)
                .content(objectMapper.writeValueAsString("A4"))
                .validationFieldType(ValidationFieldType.STRING)
                .build());
        validations.add(ValidationField.builder()
                .keyName("orientation")
                .content(objectMapper.writeValueAsString("VERTICAL"))
                .validationFieldType(ValidationFieldType.STRING)
                .build());
        validations.add(ValidationField.builder()
                .keyName("pdf_version")
                .content(objectMapper.writeValueAsString(List.of("A-2", "A-4")))
                .validationFieldType(ValidationFieldType.LIST)
                .build());

        validations.add(ValidationField.builder()
                .keyName("margin_top")
                .content(objectMapper.writeValueAsString("10"))
                .validationFieldType(ValidationFieldType.INT)
                .build());
        validations.add(ValidationField.builder()
                .keyName("margin_bottom")
                .content(objectMapper.writeValueAsString("8"))
                .validationFieldType(ValidationFieldType.INT)
                .build());
        validations.add(ValidationField.builder()
                .keyName("margin_left")
                .content(objectMapper.writeValueAsString("15"))
                .validationFieldType(ValidationFieldType.INT)
                .build());
        validations.add(ValidationField.builder()
                .keyName("margin_right")
                .content(objectMapper.writeValueAsString("15"))
                .validationFieldType(ValidationFieldType.INT)
                .build());

        validations.add(ValidationField.builder()
                .keyName("forbiden_restrictions")
                .content(objectMapper.writeValueAsString(List.of("password", "print", "edition", "copy", "edit", "other_based_on_certification")))
                .validationFieldType(ValidationFieldType.LIST)
                .build());

        addConfig(FILE_PARAMS, validations);
    }

    @SneakyThrows
    private void fileNameParams() {
        Set<ValidationField> validations = new HashSet<>();
        validations.add(ValidationField.builder()
            .keyName(FileNameValidationPlugin.FORBIDDEN_CHARS)
                .content(objectMapper.writeValueAsString(List.of("~", "\"", "#", "%", "&", "*", ":", "<", ">", "?", "!", "/", "\\", "{", "|", "}")))
                .validationFieldType(ValidationFieldType.LIST)
                .build());
        validations.add(ValidationField.builder()
            .keyName(FileNameValidationPlugin.TRIM)
            .content(objectMapper.writeValueAsString(
                List.of(FileNameValidationPlugin.SPACE_BEFORE, FileNameValidationPlugin.SPACE_AFTER)))
                .validationFieldType(ValidationFieldType.LIST)
                .build());
        validations.add(ValidationField.builder()
            .keyName(FileNameValidationPlugin.CHAR_CODE)
                .content(objectMapper.writeValueAsString("UTF-8"))
                .validationFieldType(ValidationFieldType.STRING)
                .build());
        validations.add(ValidationField.builder()
            .keyName(FileNameValidationPlugin.FULL_FILENAME_LENGTH)
            .content(objectMapper.writeValueAsString("255"))
            .validationFieldType(ValidationFieldType.INT)
            .build());
        addConfig(FileNameValidationPlugin.GROUP_NAME, validations);
    }

    @SneakyThrows
    private void technicalParams() {

        Set<ValidationField> validations = new HashSet<>();
        validations.add(ValidationField.builder()
                .keyName("print_color")
                .content(objectMapper.writeValueAsString(List.of("monochromatic", "color")))
                .validationFieldType(ValidationFieldType.LIST)
                .build());
        validations.add(ValidationField.builder()
                .keyName("print_pages")
                .content(objectMapper.writeValueAsString(List.of("single", "double")))
                .validationFieldType(ValidationFieldType.LIST)
                .build());
        validations.add(ValidationField.builder()
                .keyName("grammage")
                .content(objectMapper.writeValueAsString("80"))
                .validationFieldType(ValidationFieldType.INT)
                .build());
        validations.add(ValidationField.builder()
                .keyName("paper_format")
                .content(objectMapper.writeValueAsString("A4"))
                .validationFieldType(ValidationFieldType.STRING)
                .build());
        validations.add(ValidationField.builder()
                .keyName("paper_color")
                .content(objectMapper.writeValueAsString("white"))
                .validationFieldType(ValidationFieldType.STRING)
                .build());
        addConfig("technical_params", validations);
    }

    private void addConfig(String group, Set<ValidationField> configList) {
        ConfigValidation configValidation = ConfigValidation.builder()
                .configMap(configList)
                .validationGroup(group)
                .build();
        configService.add(configValidation);
    }
}
