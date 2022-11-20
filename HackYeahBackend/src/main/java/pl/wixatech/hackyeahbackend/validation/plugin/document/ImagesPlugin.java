package pl.wixatech.hackyeahbackend.validation.plugin.document;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;
import pl.wixatech.hackyeahbackend.configuration.ConfigService;
import pl.wixatech.hackyeahbackend.validation.ValidationFieldMapper;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationPluginWithInput;
import pl.wixatech.hackyeahbackend.validation.plugin.ValidationResult;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ImagesPlugin implements ValidationPluginWithInput {
    public static final String FORMAT = "format";
    private final ConfigService configService;
    public static final String FILE_PARAMS = "file_params";
    private final ValidationFieldMapper fieldMapperService;

    @SneakyThrows
    @Override
    public ValidationResult validate(PDDocument doc) {
        List<String> errors = new ArrayList<>();
        List<RenderedImage> imagesFromPDF = getImagesFromPDF(doc);

        return ValidationResult.builder()
                .valid(true)
                .groupName("images")
                .messageErrors(errors)
                .build();
    }


    public List<RenderedImage> getImagesFromPDF(PDDocument document) throws IOException {
        List<RenderedImage> images = new ArrayList<>();
        for (PDPage page : document.getPages()) {
            images.addAll(getImagesFromResources(page.getResources()));
        }

        return images;
    }

    private List<RenderedImage> getImagesFromResources(PDResources resources) throws IOException {
        List<RenderedImage> images = new ArrayList<>();

        for (COSName xObjectName : resources.getXObjectNames()) {
            PDXObject xObject = resources.getXObject(xObjectName);

            if (xObject instanceof PDFormXObject) {
                images.addAll(getImagesFromResources(((PDFormXObject) xObject).getResources()));
            } else if (xObject instanceof PDImageXObject) {
                images.add(((PDImageXObject) xObject).getImage());
            }
        }

        return images;
    }
}
