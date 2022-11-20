package pl.wixatech.hackyeahbackend.validation.plugin.document;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.stereotype.Component;
import pl.wixatech.hackyeahbackend.configuration.ConfigService;
import pl.wixatech.hackyeahbackend.configuration.ValidationField;
import pl.wixatech.hackyeahbackend.validation.ValidationFieldMapper;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationPluginWithInput;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationResult;

import java.util.ArrayList;
import java.util.List;

import static org.apache.pdfbox.pdmodel.common.PDRectangle.A3;
import static org.apache.pdfbox.pdmodel.common.PDRectangle.A4;

@Component
@RequiredArgsConstructor
public class FileParametersPlugin implements ValidationPluginWithInput {
    public static final String FORMAT = "format";
    private final ConfigService configService;
    public static final String FILE_PARAMS = "file_params";
    private final ValidationFieldMapper fieldMapperService;

    @Override
    public ValidationResult validate(PDDocument doc) {
        List<String> errors = new ArrayList<>();

        PDPage page = doc.getPage(0);
        boolean format = format(page);
        if (!format) {
            errors.add("File format is not valid -> A4");
        }
        boolean orientedVertical = isOrientedVertical(page);
        if (!orientedVertical) {
            errors.add("File is not oriented vertical");
        }

        return ValidationResult.builder()
                .valid(format && orientedVertical)
                .groupName(FILE_PARAMS)
                .messageErrors(errors)
                .build();
    }

    private boolean format(PDPage page) {
        ValidationField validationField = configService.getConfigValidationByGroup(FILE_PARAMS).getValidationFieldByName().get(FORMAT);
        String format = (String) fieldMapperService.mapValue(validationField);
        PDRectangle formatRec = switch (format) {
            case "A3" -> A3;
            case "A4" -> A4;
            default -> A4;
        };

        PDRectangle mediaBox = page.getMediaBox();
        boolean a = Math.ceil(mediaBox.getLowerLeftX()) == Math.ceil(formatRec.getLowerLeftX());
        boolean b = Math.ceil(mediaBox.getLowerLeftY()) == Math.ceil(formatRec.getLowerLeftY());
        boolean c = Math.ceil(mediaBox.getUpperRightX()) == Math.ceil(formatRec.getUpperRightX());
        boolean d = Math.ceil(mediaBox.getUpperRightY()) == Math.ceil(formatRec.getUpperRightY());
        return a &&
                b &&
                c &&
                d;
    }

    private boolean isOrientedVertical(PDPage page) {
        PDRectangle mediaBox = page.getMediaBox();
        boolean isLandscape = mediaBox.getWidth() > mediaBox.getHeight();
        int rotation = page.getRotation();
        if (rotation == 90 || rotation == 270)
            isLandscape = !isLandscape;
        return !isLandscape;
    }


}
