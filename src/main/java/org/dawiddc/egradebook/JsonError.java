package org.dawiddc.egradebook;

class JsonError {
    private final String type;
    private final String message;

    public JsonError(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return this.type;
    }

    public String getMessage() {
        return this.message;
    }
}
