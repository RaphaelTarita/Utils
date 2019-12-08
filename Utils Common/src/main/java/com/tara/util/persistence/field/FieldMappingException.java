package com.tara.util.persistence.field;

public class FieldMappingException extends RuntimeException {
    private final String fieldname;
    private final String entityname;

    public FieldMappingException(String fieldname, String entityname, String msg) {
        super(msg);
        this.fieldname = fieldname;
        this.entityname = entityname;
    }

    public FieldMappingException(String entityname, String msg) {
        this("<unspecified>", entityname, msg);
    }

    @Override
    public String toString() {
        return "Problematic Field mapping found on entity '" + entityname + "', field '" + fieldname + "': " + super.getMessage();
    }
}
