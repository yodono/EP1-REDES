package protocol;

/**
 * Interface que abstrai ambiente em que o comando será executado
 */
public interface CommandContext {
    void send(String message);
    String getUsername();
}
