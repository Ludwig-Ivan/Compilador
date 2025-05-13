package compilador;

import java.util.ArrayList;
import java.util.List;

public class AnalizadorLexico {

    /**
     * Esta clase representa un elemento tipo token, el cual resguarda tanto
     * tipo, lexema, linea y columna del token, permitiendo su manipulacion y
     * validacion de elementos
     */
    public static class Token {
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

        // TODO: Corregir metodo toString
        // ! El metodo devuelve un msj, generara conflicto, mejor devolver una cadena
        // ! formateada que se pueda manipular
        /**
         * Metodo sobre escrito para convertir el token en string
         * 
         * @return -> regresa una cadena con informacion del lexema
         */
        @Override
        public String toString() {
            return String.format("Línea %d, Columna %d: %s -> '%s'", linea, columna, tipo, lexema);
        }
    }

    /**
     * Arreglo de las palabras reservadas reconocibles
     */
    private static final String[] PALABRAS_RESERVADAS = {
            "if", "else", "while", "do", "return", "print", "int", "float", "char", "cadena",
            "bool", "void", "true", "false", "break", "continue", "func"
    };

    /**
     * Arreglo de los operadores reconocibles
     */
    private static final String[] OPERADORES = {
            "+", "-", "*", "/", "%", "=", "==", "!=", "<", ">", "<=", ">=", "&&", "||", "!",
            "++", "--", "?", ":"
    };

    /**
     * Arreglo de los simbolos reconocibles
     */
    private static final String[] SIMBOLOS = {
            ";", ",", "(", ")", "{", "}", "[", "]"
    };

    /**
     * Determina si al caracter recibido es una letra
     * 
     * @param c -> caracter
     * @return boolean
     */
    private static boolean esLetra(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    /**
     * Determina si el caracter recibido es un digito
     * 
     * @param c -> caracter
     * @return boolean
     */
    private static boolean esDigito(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Determina si al caracter recibido es algun caracter vacio \
     * 
     * @param c -> caracter
     * @return boolean
     */
    private static boolean esEspacio(char c) {
        return c == ' ' || c == '\n' || c == '\t' || c == '\r';
    }

    /**
     * Determina si la palabra es una palabra reservada
     * 
     * @param palabra -> String palabra
     * @return boolean
     */
    private static boolean esPalabraReservada(String palabra) {
        for (String pr : PALABRAS_RESERVADAS)
            if (pr.equalsIgnoreCase(palabra))
                return true;
        return false;
    }

    /**
     * Determina si la palabra recibida es un operador
     * 
     * @param s -> String operador
     * @return boolean
     */
    private static boolean esOperador(String s) {
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
    private static boolean esSimbolo(String s) {
        for (String sim : SIMBOLOS)
            if (sim.equals(s))
                return true;
        return false;
    }

    /**
     * Metodo encargado del analisis de palabras para aplicar el correspondiente
     * lexico
     * 
     * @param entrada -> entrada a aplicar analisis
     */
    public List<Token> analizar(String entrada) {
        List<Token> tokens = new ArrayList<>(); // ?Lista de tokens
        StringBuilder buffer = new StringBuilder();
        int i = 0, linea = 1, columna = 1;

        while (i < entrada.length()) {
            char actual = entrada.charAt(i);

            // ? Al encontrar salto de linea, lo ignora y prosigue
            if (actual == '\n') {
                linea++;
                columna = 1;
                i++;
                continue;
            }

            // ? Si encuentra un espacio lo ignora
            if (esEspacio(actual)) {
                columna++;
                i++;
                continue;
            }

            // ? Identificadores o palabras reservadas
            if (esLetra(actual)) {
                buffer.setLength(0);
                int startColumna = columna;
                while (i < entrada.length() && (esLetra(entrada.charAt(i)) || esDigito(entrada.charAt(i)))) {
                    buffer.append(entrada.charAt(i));
                    i++;
                    columna++;
                }

                // ? Verifica que la palabra formada en el buffer es
                // ? reservada | identificador / error
                // ? Y la agrega a la lista de tokens
                String palabra = buffer.toString();
                tokens.add(palabra.matches("[a-zA-Z_][a-zA-Z0-9_]*")
                        ? new Token(esPalabraReservada(palabra) ? "PALABRA_RESERVADA" : "IDENTIFICADOR", palabra,
                                linea, startColumna)
                        : new Token("ERROR", "Identificador inválido: '" + palabra + "'", linea, startColumna));
                continue;
            }

            // ? Forma el digito (Token) y valida que se forme de manera correcta
            if (esDigito(actual)) {
                buffer.setLength(0);
                int startColumna = columna;
                boolean tienePunto = false;
                while (i < entrada.length() && (esDigito(entrada.charAt(i)) || entrada.charAt(i) == '.')) {
                    if (entrada.charAt(i) == '.') {
                        if (tienePunto) {
                            buffer.append(entrada.charAt(i++));
                            columna++;
                            tokens.add(new Token("ERROR", "Número mal formado: '" + buffer.toString() + "'", linea,
                                    startColumna));
                            continue;
                        }
                        tienePunto = true;
                    }
                    buffer.append(entrada.charAt(i++));
                    columna++;
                }

                // ? Verifica si es un numero o un error
                String numero = buffer.toString();
                tokens.add(numero.matches("\\d+") || numero.matches("\\d+\\.\\d+")
                        ? new Token("NUMERO", numero, linea, startColumna)
                        : numero.endsWith(".") || numero.startsWith(".")
                                ? new Token("ERROR", "Número incompleto: '" + numero + "'", linea, startColumna)
                                : new Token("ERROR", "Número mal formado: '" + numero + "'", linea, startColumna));
                continue;
            }

            // ? Identifica las cadenas y verifica q esten bien formadas
            if (actual == '"') {
                buffer.setLength(0);
                int startColumna = columna++;
                i++;

                while (i < entrada.length() && entrada.charAt(i) != '"' && entrada.charAt(i) != '\n') {
                    buffer.append(entrada.charAt(i++));
                    columna++;
                }

                if (i >= entrada.length() || entrada.charAt(i) == '\n') {
                    tokens.add(
                            new Token("ERROR", "Cadena sin cerrar: '" + buffer.toString() + "'", linea, startColumna));
                    if (i < entrada.length() && entrada.charAt(i) == '\n') {
                        linea++;
                        columna = 1;
                        i++;
                    }
                    continue;
                }

                i++;
                columna++;
                tokens.add(new Token("CADENA", buffer.toString(), linea, startColumna));
                continue;
            }

            // ? Identifica los caracteres y valida que se encuentren bien formados
            if (actual == '\'') {
                buffer.setLength(0);
                int startColumna = columna++;
                i++;

                while (i < entrada.length() && entrada.charAt(i) != '\'' && entrada.charAt(i) != '\n') {
                    buffer.append(entrada.charAt(i++));
                    columna++;
                }

                if (i >= entrada.length() || entrada.charAt(i) == '\n') {
                    tokens.add(new Token("ERROR", "Caracter sin cerrar: '" + buffer.toString() + "'", linea,
                            startColumna));
                    if (i < entrada.length() && entrada.charAt(i) == '\n') {
                        linea++;
                        columna = 1;
                        i++;
                    }
                    continue;
                }

                i++;
                columna++;

                String caracter = buffer.toString();
                tokens.add(caracter.length() == 1 || caracter.isEmpty()
                        ? new Token("CARACTER", caracter, linea, startColumna)
                        : new Token("ERROR", "Caracter inválido: '" + caracter + "'", linea, startColumna));
                continue;
            }

            // ? Forma los comentarios y valida si estan bien formados
            if (actual == '/' && i + 1 < entrada.length()) {
                if (entrada.charAt(i + 1) == '/') {
                    buffer.setLength(0);
                    int startColumna = columna;
                    i += 2;
                    columna += 2;
                    while (i < entrada.length() && entrada.charAt(i) != '\n') {
                        buffer.append(entrada.charAt(i++));
                        columna++;
                    }
                    tokens.add(new Token("COMENTARIO", buffer.toString(), linea, startColumna));
                    continue;
                } else if (entrada.charAt(i + 1) == '*') {
                    buffer.setLength(0);
                    int startColumna = columna;
                    i += 2;
                    columna += 2;
                    while (i + 1 < entrada.length() && !(entrada.charAt(i) == '*' && entrada.charAt(i + 1) == '/')) {
                        if (entrada.charAt(i) == '\n') {
                            linea++;
                            columna = 1;
                        } else
                            columna++;
                        buffer.append(entrada.charAt(i++));
                    }
                    if (i + 1 >= entrada.length()) {
                        tokens.add(new Token("ERROR", "Comentario multilínea sin cerrar: '/*" + buffer.toString() + "'",
                                linea, startColumna));
                        continue;
                    }
                    i += 2;
                    columna += 2;
                    tokens.add(new Token("COMENTARIO", buffer.toString(), linea, startColumna));
                    continue;
                }
            }

            // ? Forma los operadores dobles (++, --, &&, ||, etc.) y verifica que se
            // ? encuentren bien formados
            if (i + 1 < entrada.length()) {
                String doble = "" + actual + entrada.charAt(i + 1);
                if (esOperador(doble)) {
                    tokens.add(new Token("OPERADOR", doble, linea, columna));
                    i += 2;
                    columna += 2;
                    continue;
                }
            }

            // ? Forma los operadores y símbolos individuales y verifica que se encuentren
            // ? bien formados
            String single = String.valueOf(actual);
            if (esOperador(single)) {
                tokens.add(new Token("OPERADOR", single, linea, columna));
                i++;
                columna++;
                continue;
            }
            if (esSimbolo(single)) {
                tokens.add(new Token("SIMBOLO", single, linea, columna));
                i++;
                columna++;
                continue;
            }

            // ? identifica el token formado como caracter no reconocido (Error)
            tokens.add(new Token("ERROR", "Carácter inválido: '" + actual + "'", linea, columna));
            i++;
            columna++;
        }

        return tokens;
    }
}