package hu.mik.pte.prog5.survey.structure;

import hu.mik.pte.prog5.survey.common.FieldError;
import hu.mik.pte.prog5.survey.common.ValidationException;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class ValidationTest {

    private static final String JSON = """
        {"fields": [
          {"type": "text", "id": "name", "label": "Neved", "required": true, "maxLength": 10},
          {"type": "number", "id": "age", "label": "Korod", "min": 0, "max": 120, "integerOnly": true},
          {"type": "single_choice", "id": "lang", "label": "Nyelv", "options": ["Java", "C#"], "display": "dropdown"},
          {"type": "multi_choice", "id": "tools", "label": "Eszköz", "options": ["Spring", "JUnit"]}
        ]}
        """;

    //Manuálisan kell kikapcsolni itt IS, másik fele a yaml-ben...
    private final JsonMapper mapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            .build();

    private SurveyStructure structure() {
        return mapper.readValue(JSON, SurveyStructure.class);
    }

    @Test
    void parsesPolymorphicFieldsAndRoundTrips() {
        SurveyStructure s = structure();
        assertThat(s.fields()).hasSize(4);
        assertThat(s.fields().get(0)).isInstanceOf(TextField.class);
        assertThat(s.fields().get(2)).isInstanceOf(SingleChoiceField.class);
        assertThat(mapper.readValue(mapper.writeValueAsString(s), SurveyStructure.class)).isEqualTo(s);
    }

    @Test
    void validStructureAndAnswersPass() {
        SurveyStructure s = structure();
        assertThatCode(() -> StructureValidator.validate(s)).doesNotThrowAnyException();
        assertThatCode(() -> AnswerValidator.validate(s,
                Map.of("name", "Peti", "age", 17, "lang", "Java", "tools", List.of("Spring"))))
                .doesNotThrowAnyException();
    }

    @Test
    void missingRequiredFieldIsRejected() {
        assertThat(errorFields(Map.of("age", 17))).containsExactly("name");
    }

    @Test
    void badValuesAreRejected() {
        Map<String, Object> answers = Map.of(
                "name", "ez egy túl hosszú név",
                "age", 150,
                "lang", "Rust",
                "tools", List.of("Spring", "Spring"));
        assertThat(errorFields(answers)).containsExactlyInAnyOrder("name", "age", "lang", "tools");
    }

    @Test
    void unknownFieldAndNonIntegerAreRejected() {
        assertThat(errorFields(Map.of("name", "x", "age", 1.5, "extra", "?")))
                .containsExactlyInAnyOrder("age", "extra");
    }

    @Test
    void invalidStructureIsRejected() {
        SurveyStructure bad = new SurveyStructure(List.of(
                new TextField("a", "", true, 10),
                new TextField("a", "Cím", false, 10),
                new SingleChoiceField("c", "Cím", false, List.of("egy"), null)));
        assertThatThrownBy(() -> StructureValidator.validate(bad)).isInstanceOf(ValidationException.class);
    }

    private List<String> errorFields(Map<String, Object> answers) {
        try {
            AnswerValidator.validate(structure(), answers);
            return List.of();
        } catch (ValidationException e) {
            return e.getErrors().stream().map(FieldError::field).toList();
        }
    }
}
