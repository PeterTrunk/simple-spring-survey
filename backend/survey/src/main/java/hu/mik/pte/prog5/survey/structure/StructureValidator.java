package hu.mik.pte.prog5.survey.structure;

import hu.mik.pte.prog5.survey.common.FieldError;
import hu.mik.pte.prog5.survey.common.ValidationException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public final class StructureValidator {

    //Kicsit hasraütéses, nagyságrendre elég.
    public static final int MAX_FIELDS = 100;
    public static final int MAX_OPTIONS = 50;
    public static final int MAX_TEXT_LENGTH = 5000;
    private static final Pattern ID_PATTERN = Pattern.compile("[A-Za-z0-9_-]{1,40}");

    private StructureValidator() {}

    public static void validate(SurveyStructure structure) {
        List<FieldError> errors = new ArrayList<>();
        List<FieldDefinition> fields = structure == null ? List.of() : structure.fields();

        //Teljes üresség, max mező darabszám
        if (fields.isEmpty()) {
            errors.add(new FieldError("fields", "Legalább egy mező szükséges"));
        }
        if (fields.size() > MAX_FIELDS) {
            errors.add(new FieldError("fields", "Legfeljebb " + MAX_FIELDS + " mező lehet"));
        }

        Set<String> seenIds = new HashSet<>();
        for (int i = 0; i < fields.size(); i++) {

            FieldDefinition f = fields.get(i);
            String where = "fields[" + i + "]";
            if (f == null) {
                errors.add(new FieldError(where, "Üres mező"));
                continue;
            }
            if (f.id() == null || !ID_PATTERN.matcher(f.id()).matches()) {
                errors.add(new FieldError(where + ".id",
                        "Az azonosító 1-40 karakter, betű, szám, _ vagy - lehet"));
            } else if (!seenIds.add(f.id())) {
                errors.add(new FieldError(where + ".id", "Ismétlődő azonosító: " + f.id()));
            }
            if (f.label() == null || f.label().isBlank() || f.label().length() > 200) {
                errors.add(new FieldError(where + ".label", "A cím kötelező, legfeljebb 200 karakter"));
            }

            switch (f) {
                case TextField t -> {
                    if (t.maxLength() < 1 || t.maxLength() > MAX_TEXT_LENGTH) {
                        errors.add(new FieldError(where + ".maxLength",
                                "A maximum karakterszám 1 és " + MAX_TEXT_LENGTH + " között lehet"));
                    }
                }
                case NumberField n -> {
                    if (n.min() != null && n.max() != null && n.min() > n.max()) {
                        errors.add(new FieldError(where + ".min", "A minimum nem lehet nagyobb a maximumnál"));
                    }
                }
                case SingleChoiceField c -> checkOptions(c.options(), where, errors);
                case MultiChoiceField c -> checkOptions(c.options(), where, errors);
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private static void checkOptions(List<String> options, String where, List<FieldError> errors) {

        if (options.size() < 2 || options.size() > MAX_OPTIONS) {
            errors.add(new FieldError(where + ".options",
                    "2 és " + MAX_OPTIONS + " között kell lennie a lehetőségek számának"));
            return;
        }

        Set<String> seen = new HashSet<>();
        for (String o : options) {
            if (o == null || o.isBlank() || o.length() > 200) {
                errors.add(new FieldError(where + ".options", "A lehetőségek nem lehetnek üresek (max. 200 karakter)"));
                return;
            }
            if (!seen.add(o)) {
                errors.add(new FieldError(where + ".options", "Ismétlődő lehetőség: " + o));
                return;
            }
        }
    }
}
