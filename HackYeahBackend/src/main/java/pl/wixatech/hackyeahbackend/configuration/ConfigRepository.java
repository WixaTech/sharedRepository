package pl.wixatech.hackyeahbackend.configuration;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigRepository extends JpaRepository<ConfigValidation, Long> {
    Optional<ConfigValidation> findByValidationGroup(String validationGroup);
}
