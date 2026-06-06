package exception;

public class DatoDuplicadaException extends RuntimeException {
    public DatoDuplicadaException(String mensaje) {
        super(mensaje);
    }
}
