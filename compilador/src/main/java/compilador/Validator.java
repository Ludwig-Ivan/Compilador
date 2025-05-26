package compilador;

public class Validator {

    /**
     * Determina si al caracter recibido es una letra
     * 
     * @param c -> caracter
     * @return boolean
     */
    public static boolean esLetra(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    /**
     * Determina si el caracter recibido es un digito
     * 
     * @param c -> caracter
     * @return boolean
     */
    public static boolean esDigito(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Determina si al caracter recibido es algun caracter vacio \
     * 
     * @param c -> caracter
     * @return boolean
     */
    public static boolean esEspacio(char c) {
        return c == ' ' || c == '\n' || c == '\t' || c == '\r';
    }
}
