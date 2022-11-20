package pl.wixatech.hackyeahbackend.validation.plugin;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ValidationResult {
    private boolean valid;
    private String groupName;
    private List<String> messageErrors;
}
