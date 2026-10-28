package hu.mik.pte.prog5.survey.structure;

import java.util.List;

public record SurveyStructure(List<FieldDefinition> fields) {

    public SurveyStructure {
        if(fields == null){
            fields = List.of();
        }
    }
}
