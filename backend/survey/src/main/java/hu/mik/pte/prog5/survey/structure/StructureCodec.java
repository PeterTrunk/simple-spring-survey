package hu.mik.pte.prog5.survey.structure;

import hu.mik.pte.prog5.survey.common.ValidationException;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Component
public class StructureCodec {
    private final JsonMapper mapper;

    public StructureCodec(JsonMapper mapper) {
        this.mapper = mapper;
    }

    public SurveyStructure parse(String json) {
        try{
            return mapper.readValue(json, SurveyStructure.class);
        } catch (JacksonException e) {
            throw new ValidationException("structure", "A kérdőív szerkezete nem értelmezhető");
        }
    }

    public String write(Object value) {
        return mapper.writeValueAsString(value);
    }
}
