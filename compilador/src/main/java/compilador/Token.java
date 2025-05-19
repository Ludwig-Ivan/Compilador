package compilador;

/**
 * Esta clase representa un elemento tipo token, el cual resguarda
 * tanto tipo, lexema, linea y columna del token, permitiendo su manipulacion y
 * validacion de elementos
 */
public class Token {
    public String tipo;
    public String lexema;
    public int linea;
    public int columna;

    /**
     * Constructor para la creacion de token
     * 
     * @param tipo    -> tipo de lexema
     * @param lexema  -> el lexema correspondiente
     * @param linea   -> numero de linea en donde se encontro el lexema
     * @param columna -> numero de columa en donde se encontro el lexema
     */
    public Token(String tipo, String lexema, int linea, int columna) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
    }

    /**
     * Metodo sobre escrito para convertir el token en string
     * 
     * @return -> regresa una cadena con informacion del lexema
     */
    @Override
    public String toString() {
        return String.format("%d~%d~%s~%s", linea, columna, tipo, lexema);
    }
}
