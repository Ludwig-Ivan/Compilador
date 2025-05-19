package compilador;

public class Validator {
    /**
     * Arreglo de las palabras reservadas reconocibles
     */
    private static final String[] PALABRAS_RESERVADAS = {
            "if", "else", "while", "do", "return", "print", "int", "float", "char", "cadena",
            "bool", "void", "true", "false", "break", "continue", "func", "main", "read"
    };

    /**
     * Arreglo de los operadores reconocibles
     */
    private static final String[] OPERADORES = {
            "+", "-", "*", "/", "%", "=", "+=", "-=", "*=", "/=", "==", "!=", "<", ">", "<=", ">=", "&&", "||", "!",
            "++", "--"
    };

    /**
     * Arreglo de los simbolos reconocibles
     */
    private static final String[] SIMBOLOS = {
            ";", ",", "(", ")", "{", "}", "[", "]", "?", ":", "#"
    };

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

    /**
     * Determina si la palabra es una palabra reservada
     * 
     * @param palabra -> String palabra
     * @return boolean
     */
    public static boolean esPalabraReservada(String palabra) {
        for (String pr : PALABRAS_RESERVADAS)
            if (pr.equals(palabra))
                return true;
        return false;
    }

    /**
     * Determina si la palabra recibida es un operador
     * 
     * @param s -> String operador
     * @return boolean
     */
    public static boolean esOperador(String s) {
        for (String op : OPERADORES)
            if (op.equals(s))
                return true;
        return false;
    }

    /**
     * Determina si la palabra recibida es un simbolo
     * 
     * @param s -> String simbolo
     * @return boolean
     */
    public static boolean esSimbolo(String s) {
        for (String sim : SIMBOLOS)
            if (sim.equals(s))
                return true;
        return false;
    }
}
