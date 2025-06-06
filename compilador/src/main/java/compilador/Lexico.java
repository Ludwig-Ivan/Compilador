package compilador;

import compilador.TablaToken.Token;

public class Lexico {

    private StringBuilder buffer;
    private int i, linea, columna;
    private boolean genTkn, ban = true, banprog = false, banfunc = false, banproc = false;

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

        genTkn = false;
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
            genTkn = false; // ? Verificar que se genero un token

            if (manejarEspaciosYSaltos(actual)) {
                llamarSintactico();
                continue;
            }
            if (manejarIdentificadoresYPalabrasReservadas(entrada)) {
                llamarSintactico();
                continue;
            }
            if (manejarNumeros(entrada)) {
                llamarSintactico();
                continue;
            }
            if (manejarCadenas(entrada)) {
                llamarSintactico();
                continue;
            }
            if (manejarCaracteres(entrada)) {
                llamarSintactico();
                continue;
            }
            if (manejarComentarios(entrada)) {
                llamarSintactico();
                continue;
            }
            if (manejarOperadoresDobles(entrada)) {
                llamarSintactico();
                continue;
            }
            if (manejarOperadoresYSimbolos(actual)) {
                llamarSintactico();
                continue;
            }

            manejarCaracterInvalido(actual);

        }

        App.tbl_token.AgregarToken("$", linea, columna, "$");
        genTkn = true;
        llamarSintactico();
        App.tbl_token.ResetPos();
    }

    // ? Metodo encargado del llamado del sintactico para el envio de tokens
    private void llamarSintactico() {
        Token token;
        if (!ban) { // ? Determina la pauta de seguir analizando junto al sintactico o no
            return;
        }

        if (genTkn) {
            token = App.tbl_token.SiguienteToken();
            if (token == null)
                ban = false;
            ban = App.sin.AnalizarToken(token);
        }
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
            App.cad_cod += "\n";
            return true;
        }
        if (Validator.esEspacio(actual)) {
            columna++;
            i++;
            if (actual != '\t')
                App.cad_cod += " ";
            else
                App.cad_cod += "\t";
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
                App.cad_cod += palabra;
                genTkn = true;
                if (palabra.equals("program")) {
                    banprog = true;
                }
                if (palabra.equals("func")) {
                    banfunc = true;
                }
                if (palabra.equals("procedure")) {
                    banproc = true;
                }
            } else {
                if (banprog) {
                    App.tbl_token.AgregarToken("IDM", linea, startColumna, App.tbl_id.AgregarID(palabra) + "");
                    App.cad_cod += "IDM";
                    banprog = false;
                } else if (banfunc) {
                    App.tbl_token.AgregarToken("IDF", linea, startColumna, App.tbl_id.AgregarID(palabra) + "");
                    App.cad_cod += "IDF";
                    banfunc = false;
                } else if (banproc) {
                    App.tbl_token.AgregarToken("IDP", linea, startColumna, App.tbl_id.AgregarID(palabra) + "");
                    App.cad_cod += "IDP";
                    banproc = false;
                } else {
                    App.tbl_token.AgregarToken("ID", linea, startColumna, App.tbl_id.AgregarID(palabra) + "");
                    App.cad_cod += "ID";
                }
                genTkn = true;
            }
        else
            App.tbl_error.agregarError("LEXICO", palabra, linea + "", startColumna + "", "Identificador invalido.");

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
                    App.tbl_error.agregarError("LEXICO", buffer.toString(), linea + "", startColumna + "",
                            "Formato de numero incorrecto.");
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
                    App.tbl_lit.AgregarLit("LNUM", numero, numero, "int") + "");
            App.cad_cod += "LNUM";
            genTkn = true;
            banprog = false;
            banfunc = false;
            banproc = false;
        } else if (numero.matches("\\d+\\.\\d+")) {
            App.tbl_token.AgregarToken("LITERAL", linea, startColumna,
                    App.tbl_lit.AgregarLit("LDEC", numero, numero, "float") + "");
            App.cad_cod += "LDEC";
            genTkn = true;
            banprog = false;
            banfunc = false;
            banproc = false;
        } else if (numero.endsWith(".") || numero.startsWith("."))
            App.tbl_error.agregarError("LEXICO", "", linea + "", startColumna + "", "Numero incompleto");
        else
            App.tbl_error.agregarError("LEXICO", "", linea + "", startColumna + "", "Formato de numero incorrecto");

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
            App.tbl_error.agregarError("LEXICO", "Fin de Archivo", startLinea + "", startColumna + "",
                    "Cadeno no cerrada");
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
                App.tbl_lit.AgregarLit("LCAD", buffer.toString(), "\"" + buffer.toString() + "\"", "cadena") + "");
        App.cad_cod += "LCAD";
        genTkn = true;
        banprog = false;
        banfunc = false;
        banproc = false;
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
            App.tbl_error.agregarError("LEXICO", "Fin de Linea", linea + "", startColumna + "", "Caracter no cerrado");
            return true;
        }

        char caracter = entrada.charAt(i);
        buffer.append(caracter);
        i++;
        columna++;

        // Esperamos la comilla de cierre
        if (i >= entrada.length() || entrada.charAt(i) != '\'') {
            App.tbl_error.agregarError("LEXICO", entrada.charAt(i) + "", linea + "", columna + "",
                    "Caracter no cerrado");
            return true;
        }

        // Comilla de cierre encontrada
        i++;
        columna++;

        // Validar que sea un único carácter (ya validado implícitamente)
        if (buffer.length() == 1) {
            App.tbl_token.AgregarToken("LITERAL", linea, startColumna,
                    App.tbl_lit.AgregarLit("LCAR", buffer.toString(), "\'" + buffer.toString() + "\'", "char") + "");
            App.cad_cod += "LCAR";
            genTkn = true;
            banprog = false;
            banfunc = false;
            banproc = false;
        } else
            App.tbl_error.agregarError("LEXICO", "Mas de un caracter", linea + "", startColumna + "", "Char invalido");

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
                    App.tbl_error.agregarError("LEXICO", comentarioNoCerrado, linea + "", columna + "",
                            "Comentario no cerrado");
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
            App.cad_cod += doble;
            genTkn = true;
            i += 2;
            columna += 2;
            banprog = false;
            banfunc = false;
            banproc = false;
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
            App.cad_cod += single;
            genTkn = true;
            i++;
            banprog = false;
            banfunc = false;
            banproc = false;
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
        App.tbl_error.agregarError("LEXICO", actual + "", linea + "", (columna++) + "", "Caracter Invalido");
        i++;
    }

}