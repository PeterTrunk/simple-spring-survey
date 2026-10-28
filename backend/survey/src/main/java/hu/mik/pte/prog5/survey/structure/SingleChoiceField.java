package hu.mik.pte.prog5.survey.structure;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SingleChoiceField(String id, String label, boolean required, List<String> options, Display display) implements FieldDefinition{

    public enum Display {
        @JsonProperty("radio") RADIO,
        @JsonProperty("dropdown") DROPDOWN
    }

    public SingleChoiceField {
        if(options == null) {
            options = List.of();
        }
        if(display == null) {
            display = Display.RADIO;
        }
    }
}
