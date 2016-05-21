package at.ac.tuwien.big.we16.ue3.exception;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class ValidationException extends Exception {
    private Set<ConstraintViolation<Object>> violations;

    public ValidationException(Set<ConstraintViolation<Object>> violations) {
        this.violations = violations;
    }

    public Set<ConstraintViolation<Object>> getViolations() {
        return violations;
    }
}
