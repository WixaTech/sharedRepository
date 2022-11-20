package pl.wixatech.hackyeahbackend.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfigService {
    private final ConfigRepository configRepository;
    private final ObjectMapper objectMapper;

    public List<ConfigValidation> getAllConfig() {
        return configRepository.findAll();
    }

    public void add(ConfigValidation configValidation) {
        configRepository.save(configValidation);
    }

    public ConfigValidation getConfigValidationByGroup(String valdiaitonGroup) {
        return configRepository.findByValidationGroup(valdiaitonGroup).orElseThrow();
    }

    @Transactional
    public void updateConfig(Map<String, Map<String, Object>> configMap) {

        configMap.forEach((key, value) -> {
            ConfigValidation configValidation = getConfigValidationByGroup(key);
            configValidation.getConfigMap().forEach(field -> {
                if (value.get(field.getKeyName()) == null) {
                    throw new NoSuchElementFoundException("There is no such key in configuration: " + field.getKeyName() + " in group: " + key);
                }
            });

            Map<String, ValidationField> validationFieldTypeMap = configValidation.getConfigMap().stream().collect(Collectors.toMap(ValidationField::getKeyName, field -> field));

//
            configValidation.setConfigMap(
                    value.entrySet().stream().map(entry -> {
                        ValidationField validationField = validationFieldTypeMap.get(entry.getKey());
                        try {
                            validationField.setContent(objectMapper.writeValueAsString(entry.getValue()));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        return validationField;
                    }).collect(Collectors.toSet()));
//                    value.entrySet().stream()
//                    .collect(Collectors.toMap(Map.Entry::getKey, v -> {
//                        try {
//                            return objectMapper.writeValueAsString(v.getValue());
//                        } catch (JsonProcessingException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }))

        });
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class NoSuchElementFoundException extends RuntimeException {
        public NoSuchElementFoundException(String message) {
            super(message);
        }
    }
}
