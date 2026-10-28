package hu.mik.pte.prog5.survey.structure;

public record TextField(String id, String label, boolean required, Integer maxLength) implements FieldDefinition {

    public static final int DEFAULT_MAX_LENGTH = 255;

    public  TextField {
        if(maxLength == null){
            maxLength = DEFAULT_MAX_LENGTH;
        }
    }
}
