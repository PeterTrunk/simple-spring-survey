package hu.mik.pte.prog5.survey.structure;

public record NumberField(String id, String label, boolean required, Double min, Double max, boolean integerOnly) implements FieldDefinition{

}
