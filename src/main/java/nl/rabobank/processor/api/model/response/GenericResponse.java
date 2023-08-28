package nl.rabobank.processor.api.model.response;

import java.util.Objects;

public record GenericResponse(String message) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenericResponse that = (GenericResponse) o;

        return Objects.equals(message, that.message);
    }

}
