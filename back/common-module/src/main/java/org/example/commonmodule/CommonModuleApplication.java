package org.example.commonmodule;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.commonmodule.dto.UserDTO;

import java.util.Set;

public class CommonModuleApplication {

    public static void main(String[] args) {
        // 1. Создаем валидатор
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // 2. Создаем DTO с пустыми полями
        UserDTO userDTO = UserDTO.builder()
                .email("")
                .login("")
                .userName("")
                .build();

        // 3. Запускаем валидацию
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        // 4. Проверяем результат
        if (violations.isEmpty()) {
            System.out.println("✅ Валидация пройдена!");
        } else {
            System.out.println("❌ Найдены ошибки:");
            for (ConstraintViolation<UserDTO> violation : violations) {
                System.out.println("  - " + violation.getPropertyPath() + ": " + violation.getMessage());
            }
        }

        factory.close();
    }
}