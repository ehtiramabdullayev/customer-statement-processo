package nl.rabobank.processor.util;

public enum FileType {
    XML("application/xml"),
    CSV("text/csv"),
    INVALID("invalid");
    private String code;
    FileType(String code) {this.code= code;}
    public String code() {return code;}

}
