package hu.mik.pte.prog5.survey.structure;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.awt.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextField.class, name = "text"),
        @JsonSubTypes.Type(value = NumberField.class, name = "number"),
        @JsonSubTypes.Type(value = SingleChoiceField.class, name = "single_choice"),
        @JsonSubTypes.Type(value = MultiChoiceField.class, name = "multi_choice")
})
public sealed interface FieldDefinition permits TextField, NumberField, SingleChoiceField, MultiChoiceField{

    String id();

    String label();

    boolean required();
}
