package hu.mik.pte.prog5.survey.structure;

import hu.mik.pte.prog5.survey.common.FieldError;
import hu.mik.pte.prog5.survey.common.ValidationException;

import java.util.*;

public final class AnswerValidator {

    private AnswerValidator() {}

    public static void validate(SurveyStructure structure, Map<String, Object> answers){

        Map<String, Object> given = answers == null ? Map.of() : answers;
        List<FieldError> errors = new ArrayList<>();

        Set<String> knownIds = new HashSet<>();
        for(FieldDefinition f : structure.fields()){
            knownIds.add(f.id());
        }
        for(String key : given.keySet()){
            if(!knownIds.contains(key)){
                errors.add(new FieldError(key, "Ismeretlen mező"));
            }
        }

        for(FieldDefinition f : structure.fields()) {
            Object value = given.get(f.id());
            if(isEmpty(value)) {
                if(f.required()) {
                    errors.add(new FieldError(f.id(), "Kötelező mező"));
                }
                continue;
            }
            String error = switch (f) {
                case TextField t -> checkText(t, value);
                case NumberField n -> checkNumber(n, value);
                case SingleChoiceField c -> checkSingle(c, value);
                case MultiChoiceField c -> checkMulti(c, value);
            };
            if(error != null) {
                errors.add(new FieldError(f.id(), error));
            }
        }

        if(!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private static boolean isEmpty(Object v) {
        return switch (v) {
            case null -> true;
            case String s -> s.isBlank();
            case Collection<?> c -> c.isEmpty();
            default -> false;
        };
    }

    private static String checkText(TextField t, Object v) {
        if(!(v instanceof String s)) return "Szöveget kell megadni";
        if(s.length() > t.maxLength()) return "Legfeljebb " + t.maxLength() + " karakter lehet";
        return null;
    }

    private static String checkNumber(NumberField n, Object v) {
        if(!(v instanceof Number num)) return "Számot kell megadni";
        double d = num.doubleValue();
        if(Double.isNaN(d) || Double.isInfinite(d)) return "Érvénytelen szám";
        if(n.integerOnly() && d != Math.rint(d)) return "Egész számot kell megadni";
        if(n.min() != null && d < n.min()) return "Legalább " + n.min() + " kell legyen";
        if(n.max() != null && d > n.max()) return "Legfeljebb " + n.max() + " lehet";
        return null;
    }

    private static String checkSingle(SingleChoiceField c, Object v) {
        if(!(v instanceof String s) || !c.options().contains(s)) return "Érvénytelen választás";
        return null;
    }

    private static String checkMulti(MultiChoiceField c, Object v) {
        if(!(v instanceof List<?> list)) return "Listát kell megadni";
        Set<Object> seen = new HashSet<>();
        for(Object o : list) {
            if(!(o instanceof String s) || !c.options().contains(s)) return "Érvénytelen választás";
            if(!seen.add(s)) return "Egy lehetőség csak egyszer választható";
        }
        return null;
    }
}
