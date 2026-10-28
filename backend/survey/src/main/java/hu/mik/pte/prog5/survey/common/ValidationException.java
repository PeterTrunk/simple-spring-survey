package hu.mik.pte.prog5.survey.common;

import java.util.List;

public class ValidationException extends RuntimeException{

    private final List<FieldError> errors;

    public ValidationException(List<FieldError> errors){
        super("Érvénytelen adat (" + errors.size() + " hiba)");
        this.errors = List.copyOf(errors);
    }

    public ValidationException(String field, String message){
        this(List.of(new FieldError(field, message)));
    }

    public List<FieldError> getErrors(){
        return errors;
    }
}
