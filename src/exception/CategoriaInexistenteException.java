package exception;

public class CategoriaInexistenteException extends RuntimeException {
    public CategoriaInexistenteException(String mensaje) {
        super(mensaje);
    }
    
}
