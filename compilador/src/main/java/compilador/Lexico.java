package compilador;

public class Lexico {

    private StringBuilder buffer;
    private int i, linea, columna;

    /**
     * Metodo que inicializa/reinicia todos los elementos para el analisis
     */
    private void inicializarAnalisis() {
        App.tbl_token = new TablaToken();
        App.tbl_id = new TablaID();
        App.tbl_lit = new TablaLit();
        App.tbl_error = new TablaError();
        buffer = new StringBuilder();

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

        App.tbl_token.AgregarToken("$", 0, 0, "$");
        App.tbl_token.ResetPos();
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
            if (App.tbl_sim_res.containsKey(palabra)) {
                App.tbl_token.AgregarToken(App.tbl_sim_res.get(palabra), linea, startColumna, palabra);
            } else {
                App.tbl_token.AgregarToken("ID", linea, startColumna, App.tbl_id.AgregarID(palabra) + "");
            }
        else
            App.tbl_error.agregarError("Identificaro invalido", palabra, linea, startColumna);

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
                    App.tbl_error.agregarError("Formato de numero incorrecto", buffer.toString(), linea, startColumna);
                    return true;
                }
                tienePunto = true;
            }
            buffer.append(entrada.charAt(i++));
            columna++;
        }

        String numero = buffer.toString();
        if (numero.matches("\\d+")) {
            App.tbl_token.AgregarToken("LITERAL", linea, startColumna,
                    App.tbl_lit.AgregarLit("LNUM", numero, numero) + "");
        } else if (numero.matches("\\d+\\.\\d+")) {
            App.tbl_token.AgregarToken("LITERAL", linea, startColumna,
                    App.tbl_lit.AgregarLit("LDEC", numero, numero) + "");
        } else if (numero.endsWith(".") || numero.startsWith("."))
            App.tbl_error.agregarError("Numero incompleto", null, linea, startColumna);
        else
            App.tbl_error.agregarError("Formato de numero incorrecto", null, linea, startColumna);

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
        int startColumna = columna;
        int startLinea = linea;
        i++; // Avanza para saltar la comilla inicial
        columna++;

        while (i < entrada.length() && entrada.charAt(i) != '"' /* && entrada.charAt(i) != '\n' */) {
            char c = entrada.charAt(i);
            if (c == '\n') {
                linea++;
                columna = 1;
            } else {
                columna++;
            }
            buffer.append(c);
            i++;
        }

        if (i >= entrada.length() /* || entrada.charAt(i) == '\n' */) {
            App.tbl_error.agregarError("Cadeno no cerrada", "Fin de Archivo", linea, columna);
            // if (i < entrada.length() && entrada.charAt(i) == '\n') {
            // linea++;
            // columna = 1;
            // i++;
            // }
            return true;
        }

        i++;
        columna++;

        App.tbl_token.AgregarToken("LITERAL", startLinea, startColumna,
                App.tbl_lit.AgregarLit("LCAD", buffer.toString(), "\"" + buffer.toString() + "\"") + "");
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

        // Verifica si hay al menos un carácter después de la comilla inicial
        if (i >= entrada.length()) {
            App.tbl_error.agregarError("Caracter no cerrado", "Fin de Linea", linea, startColumna);
            return true;
        }

        char caracter = entrada.charAt(i);
        buffer.append(caracter);
        i++;
        columna++;

        // Esperamos la comilla de cierre
        if (i >= entrada.length() || entrada.charAt(i) != '\'') {
            App.tbl_error.agregarError("Caracter no cerrado", entrada.charAt(i) + "", linea, columna);
            return true;
        }

        // Comilla de cierre encontrada
        i++;
        columna++;

        // Validar que sea un único carácter (ya validado implícitamente)
        if (buffer.length() == 1)
            App.tbl_token.AgregarToken("LITERAL", linea, startColumna,
                    App.tbl_lit.AgregarLit("LCAR", buffer.toString(), "\'" + buffer.toString() + "\'") + "");
        else
            App.tbl_error.agregarError("Char invalido", "Mas de un caracter", linea, startColumna);

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
                    App.tbl_error.agregarError("Comentario no cerrado", comentarioNoCerrado, linea, columna);
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
        if (App.tbl_sim_res.containsKey(doble)) {
            App.tbl_token.AgregarToken(App.tbl_sim_res.get(doble), linea, columna, doble);
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
        if (App.tbl_sim_res.containsKey(single)) {
            App.tbl_token.AgregarToken(App.tbl_sim_res.get(single), linea, columna++, single);
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
        App.tbl_error.agregarError("ERROR_INVALID_CARACTER", actual + "", linea, columna++);
        i++;
    }

}