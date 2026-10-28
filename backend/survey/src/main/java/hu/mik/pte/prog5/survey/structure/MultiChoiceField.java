package hu.mik.pte.prog5.survey.structure;

import java.util.List;

public record MultiChoiceField(String id, String label, boolean required, List<String> options)  implements FieldDefinition {

    public MultiChoiceField {
        if (options == null) {
            options = List.of();
        }
    }
}
