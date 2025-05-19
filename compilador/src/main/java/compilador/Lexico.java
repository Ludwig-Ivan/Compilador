package compilador;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Lexico {

    public List<Token> errores;
    private List<Token> tokens;
    private ListIterator<Token> tokens_i;
    private StringBuilder buffer;
    private int i, linea, columna;

    /**
     * Metodo que inicializa/reinicia todos los elementos para el analisis
     */
    private void inicializarAnalisis() {
        tokens = new ArrayList<>();
        buffer = new StringBuilder();
        errores = new ArrayList<>();
        i = 0;
        linea = 1;
        columna = 1;
    }

    /**
     * Metodo encargado del analisis de palabras para aplicar el correspondiente
     * lexico
     * 
     * @param entrada -> cadena a aplicar analisis
     */
    public void Analizar(String entrada) {
        inicializarAnalisis();

        while (i < entrada.length()) {
            char actual = entrada.charAt(i);

            if (manejarEspaciosYSaltos(actual))
                continue;
            if (manejarIdentificadoresYPalabrasReservadas(entrada))
                continue;
            if (manejarNumeros(entrada))
                continue;
            if (manejarCadenas(entrada))
                continue;
            if (manejarCaracteres(entrada))
                continue;
            if (manejarComentarios(entrada))
                continue;
            if (manejarOperadoresDobles(entrada))
                continue;
            if (manejarOperadoresYSimbolos(actual))
                continue;

            manejarCaracterInvalido(actual);
        }

        tokens.add(new Token("$", "$", linea, columna));
        tokens_i = tokens.listIterator();
    }

    /**
     * Metodo que se encarga de identificar los espacios y saltos de la cadena.
     * Al detectar un espacio, indica que se debe avanzar de caracter
     * Al detectar un salto de linea, indica que debe reiniciar la columna, y
     * avanzar de linea
     * 
     * @param actual -> Caracter a identificar
     * @return true | false
     */
    private boolean manejarEspaciosYSaltos(char actual) {
        if (actual == '\n') {
            linea++;
            columna = 1;
            i++;
            return true;
        }
        if (Validator.esEspacio(actual)) {
            columna++;
            i++;
            return true;
        }
        return false;
    }

    /**
     * Metodo encargado de identificar identificadores/palabras reservadas.
     * Hace uso de un buffer, para armar la palabra apartir de la posicion actual en
     * la entrada.
     * Una vez armada identificamos si es identificador/palabra reservada
     * 
     * @param entrada -> cadena a analizar
     * @return true / false
     */
    private boolean manejarIdentificadoresYPalabrasReservadas(String entrada) {
        if (!Validator.esLetra(entrada.charAt(i)))
            return false;

        buffer.setLength(0);
        int startColumna = columna;
        while (i < entrada.length()
                && (Validator.esLetra(entrada.charAt(i)) || Validator.esDigito(entrada.charAt(i)))) {
            buffer.append(entrada.charAt(i++));
            columna++;
        }

        String palabra = buffer.toString();
        if (palabra.matches("[a-zA-Z_][a-zA-Z0-9_]*"))
            tokens.add(new Token(Validator.esPalabraReservada(palabra) ? "RESERVADAS" : "ID", palabra, linea,
                    startColumna));
        else
            errores.add(new Token("ERROR_ID_INVALID", palabra, linea, startColumna));

        return true;
    }

    /**
     * Metodo encargado de identificar numeros (enteros)/decimales (flotantes).
     * Hace uso de un buffer, para armar el numero apartir de la posicion actual en
     * la entrada.
     * Una vez armada identificamos si es numero (entero)/decimal (flotante).
     * 
     * @param entrada -> cadena a analizar
     * @return true / false
     */
    private boolean manejarNumeros(String entrada) {
        if (!Validator.esDigito(entrada.charAt(i)))
            return false;

        buffer.setLength(0);
        int startColumna = columna;
        boolean tienePunto = false;
        while (i < entrada.length() && (Validator.esDigito(entrada.charAt(i)) || entrada.charAt(i) == '.')) {
            if (entrada.charAt(i) == '.') {
                if (tienePunto) {
                    buffer.append(entrada.charAt(i++));
                    columna++;
                    errores.add(new Token("ERROR_NUM_FORMAT", buffer.toString(), linea, startColumna));
                    return true;
                }
                tienePunto = true;
            }
            buffer.append(entrada.charAt(i++));
            columna++;
        }

        String numero = buffer.toString();
        if (numero.matches("\\d+"))
            tokens.add(new Token("NUMERO", numero, linea, startColumna));
        else if (numero.matches("\\d+\\.\\d+"))
            tokens.add(new Token("DECIMAL", numero, linea, startColumna));
        else if (numero.endsWith(".") || numero.startsWith("."))
            errores.add(new Token("ERROR_INCOMPLETE_NUM", numero, linea, startColumna));
        else
            errores.add(new Token("ERROR_NUM_FORMAT", numero, linea, startColumna));

        return true;
    }

    /**
     * Metodo encargado de identificar cadenas.
     * Hace uso de un buffer, para armar la cadena apartir de la posicion actual en
     * la entrada.
     * 
     * @param entrada -> cadena a analizar
     * @return true / false
     */
    private boolean manejarCadenas(String entrada) {
        if (entrada.charAt(i) != '"')
            return false;

        buffer.setLength(0);
        int startColumna = columna++;
        i++;

        while (i < entrada.length() && entrada.charAt(i) != '"' && entrada.charAt(i) != '\n') {
            buffer.append(entrada.charAt(i++));
            columna++;
        }

        if (i >= entrada.length() || entrada.charAt(i) == '\n') {
            errores.add(new Token("ERROR_UNCLOSED_CAD", '"' + buffer.toString(), linea, startColumna));
            if (i < entrada.length() && entrada.charAt(i) == '\n') {
                linea++;
                columna = 1;
                i++;
            }
            return true;
        }

        i++;
        columna++;
        tokens.add(new Token("CADENA", buffer.toString(), linea, startColumna));
        return true;
    }

    /**
     * Metodo encargado de identificar caracteres.
     * Hace uso de un buffer, para armar el caracter apartir de la posicion actual
     * en la entrada.
     * 
     * @param entrada -> cadena a analizar
     * @return true / false
     */
    private boolean manejarCaracteres(String entrada) {
        if (entrada.charAt(i) != '\'')
            return false;

        buffer.setLength(0);
        int startColumna = columna++;
        i++;

        while (i < entrada.length() && entrada.charAt(i) != '\'' && entrada.charAt(i) != '\n') {
            buffer.append(entrada.charAt(i++));
            columna++;
        }

        if (i >= entrada.length() || entrada.charAt(i) == '\n') {
            errores.add(new Token("ERROR_UNCLOSED_CHAR", "'" + buffer.toString(), linea, startColumna));
            if (i < entrada.length() && entrada.charAt(i) == '\n') {
                linea++;
                columna = 1;
                i++;
            }
            return true;
        }

        i++;
        columna++;

        String caracter = buffer.toString();
        if (caracter.length() == 1 || caracter.isEmpty())
            tokens.add(new Token("CARACTER", caracter, linea, startColumna));
        else
            errores.add(new Token("ERROR_INVALID_CHAR", "'" + caracter + "'", linea, startColumna));

        return true;
    }

    /**
     * Metodo encargado de identificar comentarios (linea | bloque).
     * Hace uso de un buffer, para armar el comentario apartir de la posicion actual
     * en la entrada.
     * 
     * @param entrada -> cadena a analizar
     * @return true / false
     */
    private boolean manejarComentarios(String entrada) {
        if (entrada.charAt(i) == '/' && i + 1 < entrada.length()) {
            if (entrada.charAt(i + 1) == '/') {
                i += 2;
                columna += 2;
                while (i < entrada.length() && entrada.charAt(i) != '\n') {
                    i++;
                    columna++;
                }
                return true;
            } else if (entrada.charAt(i + 1) == '*') {
                buffer.setLength(0);
                int startColumna = columna;
                int startIndex = i;
                i += 2;
                columna += 2;

                boolean cerrado = false;
                while (i + 1 < entrada.length()) {
                    if (entrada.charAt(i) == '*' && entrada.charAt(i + 1) == '/') {
                        cerrado = true;
                        i += 2;
                        columna += 2;
                        break;
                    }
                    if (entrada.charAt(i) == '\n') {
                        linea++;
                        columna = 1;
                    } else {
                        columna++;
                    }
                    buffer.append(entrada.charAt(i++));
                }

                if (!cerrado) {
                    String comentarioNoCerrado = entrada.substring(startIndex);
                    errores.add(new Token("ERROR_UNCLOSED_COMENTARY", comentarioNoCerrado, linea, startColumna));
                    i = entrada.length();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo encargado de identificar operadores compuestos.
     * Hace uso de un buffer, para armar el operador apartir de la posicion actual
     * en la entrada.
     * 
     * @param entrada -> cadena a analizar
     * @return true / false
     */
    private boolean manejarOperadoresDobles(String entrada) {
        if (i + 1 >= entrada.length())
            return false;
        String doble = "" + entrada.charAt(i) + entrada.charAt(i + 1);
        if (Validator.esOperador(doble)) {
            tokens.add(new Token("OPERADOR", doble, linea, columna));
            i += 2;
            columna += 2;
            return true;
        }
        return false;
    }

    /**
     * Metodo encargado de identificar operadores y simbolos simples.
     * 
     * @param actual -> caracter a analizar
     * @return true / false
     */
    private boolean manejarOperadoresYSimbolos(char actual) {
        String single = String.valueOf(actual);
        if (Validator.esOperador(single)) {
            tokens.add(new Token("OPERADOR", single, linea, columna++));
            i++;
            return true;
        }
        if (Validator.esSimbolo(single)) {
            tokens.add(new Token("SIMBOLO", single, linea, columna++));
            i++;
            return true;
        }
        return false;
    }

    /**
     * Metodo encargado de identificar los casos invalidos de caracteres.
     * 
     * @param actual -> caracter a analizar
     * @return true / false
     */
    private void manejarCaracterInvalido(char actual) {
        errores.add(new Token("ERROR_INVALID_CARACTER", String.valueOf(actual), linea, columna++));
        i++;
    }

    /**
     * Metodo para obtener el siguiente token dentro de la cadena a analizar
     * 
     * @return Token analizado | null en caso de no encontrar mas tokens
     */
    public Token SiguienteToken() {
        if (tokens_i.hasNext())
            return tokens_i.next();
        return null;
    }

}