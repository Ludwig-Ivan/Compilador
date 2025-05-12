package compilador;

import java.util.ArrayList;
import java.util.List;

public class AnalizadorLexico {

    public static class Token {
        public String tipo;
        public String lexema;
        public int linea;
        public int columna;

        public Token(String tipo, String lexema, int linea, int columna) {
            this.tipo = tipo;
            this.lexema = lexema;
            this.linea = linea;
            this.columna = columna;
        }

        @Override
        public String toString() {
            return String.format("Línea %d, Columna %d: %s -> '%s'", linea, columna, tipo, lexema);
        }
    }

    private static final String[] PALABRAS_RESERVADAS = {
        "if", "else", "while", "do", "return", "print", "int", "float", "char", "cadena",
        "bool", "void", "true", "false", "break", "continue", "func"
    };

    private static final String[] OPERADORES = {
        "+", "-", "*", "/", "%", "=", "==", "!=", "<", ">", "<=", ">=", "&&", "||", "!",
        "++", "--", "?", ":"
    };

    private static final String[] SIMBOLOS = {
        ";", ",", "(", ")", "{", "}", "[", "]"
    };

    private static boolean esLetra(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private static boolean esDigito(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean esEspacio(char c) {
        return c == ' ' || c == '\n' || c == '\t' || c == '\r';
    }

    private static boolean esPalabraReservada(String palabra) {
        for (String pr : PALABRAS_RESERVADAS) {
            if (pr.equalsIgnoreCase(palabra)) {
                return true;
            }
        }
        return false;
    }

    private static boolean esOperador(String s) {
        for (String op : OPERADORES) {
            if (op.equals(s)) {
                return true;
            }
        }
        return false;
    }

    private static boolean esSimbolo(String s) {
        for (String sim : SIMBOLOS) {
            if (sim.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public List<Token> analizar(String entrada) {
        List<Token> tokens = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        int i = 0;
        int linea = 1;
        int columna = 1;

        while (i < entrada.length()) {
            char actual = entrada.charAt(i);

            // Manejar saltos de línea
            if (actual == '\n') {
                linea++;
                columna = 1;
                i++;
                continue;
            }

            // Ignorar espacios
            if (esEspacio(actual)) {
                columna++;
                i++;
                continue;
            }

            // Identificadores o palabras reservadas
            if (esLetra(actual)) {
                buffer.setLength(0);
                int startColumna = columna;
                while (i < entrada.length() && (esLetra(entrada.charAt(i)) || esDigito(entrada.charAt(i)))) {
                    buffer.append(entrada.charAt(i));
                    i++;
                    columna++;
                }
                String palabra = buffer.toString();
                if (palabra.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                    if (esPalabraReservada(palabra)) {
                        tokens.add(new Token("PALABRA_RESERVADA", palabra, linea, startColumna));
                    } else {
                        tokens.add(new Token("IDENTIFICADOR", palabra, linea, startColumna));
                    }
                } else {
                    tokens.add(new Token("ERROR", "Identificador inválido: '" + palabra + "'", linea, startColumna));
                }
                continue;
            }

            // Números (enteros y decimales)
            if (esDigito(actual)) {
                buffer.setLength(0);
                int startColumna = columna;
                boolean tienePunto = false;
                while (i < entrada.length() && (esDigito(entrada.charAt(i)) || entrada.charAt(i) == '.')) {
                    if (entrada.charAt(i) == '.') {
                        if (tienePunto) {
                            buffer.append(entrada.charAt(i));
                            i++;
                            columna++;
                            tokens.add(new Token("ERROR", "Número mal formado: '" + buffer.toString() + "'", linea, startColumna));
                            continue;
                        }
                        tienePunto = true;
                    }
                    buffer.append(entrada.charAt(i));
                    i++;
                    columna++;
                }
                String numero = buffer.toString();
                if (numero.matches("\\d+")) {
                    tokens.add(new Token("NUMERO", numero, linea, startColumna));
                } else if (numero.matches("\\d+\\.\\d+")) {
                    tokens.add(new Token("NUMERO", numero, linea, startColumna));
                } else if (numero.endsWith(".") || numero.startsWith(".")) {
                    tokens.add(new Token("ERROR", "Número incompleto: '" + numero + "'", linea, startColumna));
                } else {
                    tokens.add(new Token("ERROR", "Número mal formado: '" + numero + "'", linea, startColumna));
                }
                continue;
            }

            // Cadenas
            if (actual == '"') {
                buffer.setLength(0);
                int startColumna = columna;
                i++;
                columna++;
                while (i < entrada.length() && entrada.charAt(i) != '"' && entrada.charAt(i) != '\n') {
                    buffer.append(entrada.charAt(i));
                    i++;
                    columna++;
                }
                if (i >= entrada.length() || entrada.charAt(i) == '\n') {
                    tokens.add(new Token("ERROR", "Cadena sin cerrar: '" + buffer.toString() + "'", linea, startColumna));
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

            // Caracteres
            if (actual == '\'') {
                buffer.setLength(0);
                int startColumna = columna;
                i++;
                columna++;
                while (i < entrada.length() && entrada.charAt(i) != '\'' && entrada.charAt(i) != '\n') {
                    buffer.append(entrada.charAt(i));
                    i++;
                    columna++;
                }
                if (i >= entrada.length() || entrada.charAt(i) == '\n') {
                    tokens.add(new Token("ERROR", "Caracter sin cerrar: '" + buffer.toString() + "'", linea, startColumna));
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
                if (caracter.length() == 1 || caracter.isEmpty()) {
                    tokens.add(new Token("CARACTER", caracter, linea, startColumna));
                } else {
                    tokens.add(new Token("ERROR", "Caracter inválido: '" + caracter + "'", linea, startColumna));
                }
                continue;
            }

            // Comentarios
            if (actual == '/' && i + 1 < entrada.length()) {
                if (entrada.charAt(i + 1) == '/') {
                    buffer.setLength(0);
                    int startColumna = columna;
                    i += 2;
                    columna += 2;
                    while (i < entrada.length() && entrada.charAt(i) != '\n') {
                        buffer.append(entrada.charAt(i));
                        i++;
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
                        } else {
                            columna++;
                        }
                        buffer.append(entrada.charAt(i));
                        i++;
                    }
                    if (i + 1 >= entrada.length()) {
                        tokens.add(new Token("ERROR", "Comentario multilínea sin cerrar: '/*" + buffer.toString() + "'", linea, startColumna));
                        continue;
                    }
                    i += 2;
                    columna += 2;
                    tokens.add(new Token("COMENTARIO", buffer.toString(), linea, startColumna));
                    continue;
                }
            }

            // Operadores dobles (++, --, &&, ||, etc.)
            if (i + 1 < entrada.length()) {
                String doble = "" + actual + entrada.charAt(i + 1);
                if (esOperador(doble)) {
                    tokens.add(new Token("OPERADOR", doble, linea, columna));
                    i += 2;
                    columna += 2;
                    continue;
                }
            }

            // Operadores y símbolos individuales
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

            // Caracter no reconocido
            tokens.add(new Token("ERROR", "Carácter inválido: '" + actual + "'", linea, columna));
            i++;
            columna++;
        }

        return tokens;
    }
}