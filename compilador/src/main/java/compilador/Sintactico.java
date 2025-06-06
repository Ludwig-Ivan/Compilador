package compilador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import compilador.TablaLit.Literal;
import compilador.TablaToken.Token;

public class Sintactico {

    public Stack<String> pila = new Stack<>(); // ? Se encarga de resguardar la estructura correcta del programa
    private HashMap<String, String> tbl_ll = new HashMap<>(); // ? estructura que reguarda la tabla LL
    private Vector<String> term; // ? Lista de terminales
    public List<String> historial_pila;
    private String tipo = "", ref = "", linea = "", columna = "", cima = "";
    private Map<String, String> descripciones = new HashMap<>();

    {
        descripciones.put("P", "< programa >");
        descripciones.put("D's", "< declaracion >");
        descripciones.put("LD", "< ID >");
        descripciones.put("LD'", "< , >");
        descripciones.put("As", "< =, ; >");
        descripciones.put("F's", "< func, procedure >");
        descripciones.put("Pm", "< int bool float char cadena >");
        descripciones.put("Pm'", " < , >");
        descripciones.put("S's", "< sentencia >");
        descripciones.put("S", "< asignacion, condicional, ciclo, salto, lfunc, lproced print >");
        descripciones.put("A'", "< = -= += *= /= >");
        descripciones.put("C'", "< else >");
        descripciones.put("C''", "< {, if >");
        descripciones.put("FI", "< ID >");
        descripciones.put("LFI", "< , >");
        descripciones.put("FC", "< expresion >");
        descripciones.put("FU", "< ID >");
        descripciones.put("LFU", "< , >");
        descripciones.put("RV", "< expresion >");
        descripciones.put("Ag", "< expresion >");
        descripciones.put("Ag'", "< , >");
        descripciones.put("T", "< int, float, char, cadena, bool >");
        descripciones.put("E", "< expresión >");
        descripciones.put("E'", "< ? >");
        descripciones.put("E1", "< expresion >");
        descripciones.put("E1'", "< &&, || >");
        descripciones.put("E2", "< expresion >");
        descripciones.put("E2'", "< ==, !=, <, >, <=, >= >");
        descripciones.put("E3", "< expresion >");
        descripciones.put("E3'", "< +, - >");
        descripciones.put("E4", "< expresion >");
        descripciones.put("E4'", "< *, /, % >");
        descripciones.put("E5", "< -, !, Factor >");
        descripciones.put("FA", "< numero, decimal, caracter, llamada, true, false, ID, read >");
        descripciones.put("OU", "< -, ! >");
        descripciones.put("$", "< fin de cadena >");
        descripciones.put("default", "un elemento del lenguaje");
    }

    /**
     * Constructor para dar pie al analisis sintactico
     * El simbolo inicial por defecto es "P"
     * 
     * @param sim_ini -> Simbolo inicial para el analisis
     */
    public Sintactico(String sim_ini) {
        pila.push("$");
        pila.push(sim_ini != null ? sim_ini : "P");
        historial_pila = new ArrayList<String>();
    }

    /**
     * Se encarga de analizar el token recibido, determinar la produccion y agregar
     * a la pila de estructura del programa para validar si se encuentra en el orden
     * determinado por la tabla LL
     * 
     * @param token -> Token a aplicar analisis
     * @return true en caso de poder analizar correctamente el token o false en caso
     *         de no poder analizar el token.
     */
    public boolean AnalizarToken(Token token) {
        historial_pila.add(pila + "");
        if (!extraerDatosToken(token))
            return false;

        cima = pila.peek();

        if (cima.equals("$"))
            return false;

        if (term.contains(cima))
            return procesarTerminal(token);

        return expandirProduccion(token);
    }

    /**
     * Metodo encargado de desglosar el token y extraer sus propiedades
     * 
     * @param token -> Token a desglosar
     * @return true en caso de poderlo desglozar o false en caso de no poderlo
     *         hacer.
     */
    private boolean extraerDatosToken(Token token) {
        if (token == null) {
            App.tbl_error.agregarError("SINTACTICO", "null", "", "", "Entrada Inesperada");
            return false;
        }

        String[] elem = token.toString().split("~");
        linea = elem[0];
        columna = elem[1];
        tipo = elem[2];
        ref = elem[3];

        return true;
    }

    /**
     * Se encarga de procesar la cima de la pila con el token actual.
     * Si lo que esta en la cima es igual a al token actual, lo saca de la pila.
     * En caso contrario manda error, y continua con el analisis de los siguientes
     * tokens.
     * 
     * @return true | false
     */
    private boolean procesarTerminal(Token token) {

        if (tipo.equals("LITERAL") && ref.matches("[0-9]*")) { // Concuerdan para literales
            Literal lit = App.tbl_lit.BuscarID(Integer.parseInt(ref));
            if (cima.equals(lit.getComp())) {
                pila.pop(); // Saco de la cima
                return true; // Continuo al siguiente toquen
            } else {
                App.tbl_error.agregarError("SINTACTICO", pila.pop(), linea, columna,
                        String.format("Se esperaba %s, se encontro", lit.getRep_text()));
                return AnalizarToken(token);
            }
        }

        if (cima.equals(
                tipo.equals("ID") || tipo.equals("IDF") || tipo.equals("IDM") || tipo.equals("IDP") ? "ID" : tipo)
                || cima.equals(ref)) {
            pila.pop();
            return true;
        } else {
            App.tbl_error.agregarError("SINTACTICO", ref, linea, columna,
                    String.format("Se esperaba %s, se encontro", pila.pop()));
            return AnalizarToken(token);
        }
    }

    /**
     * Metodo encargado de procesar las producciones especiales
     * ("&" : Epsilon | "`" : Saltar | """" : Sacar)
     * Si es epsilon, saca el ultimo elemento de la pila, y vuelve a realizar el
     * analisis con el token actual.
     * Si es ` o """" saca el ultimo elemenmto de la pila, y manda un error
     * dependiendo del caso.
     * 
     * @param token -> Token para aplicar analsis en caso de epsilon
     * @return true | false para indicar que se proceso de manera correcta la
     *         produccion
     */
    private boolean procesarProduccionEspecial(Token token, String sig_prod, String prod_act) {
        String descripcion = descripciones.getOrDefault(prod_act, prod_act);

        switch (sig_prod) {
            case "&":
                return AnalizarToken(token); // Reprocesar el mismo token al siguiente elemento en la pila
            case "`":
                String ref = "?";
                if (token.getTipo().equals("ID") || token.getTipo().equals("IDM") || token.getTipo().equals("IDF")
                        || token.getTipo().equals("IDP")) {
                    ref = App.tbl_id.BuscarID(Integer.parseInt(token.getRef())).getNom();
                } else if (token.getTipo().equals("LITERAL")) {
                    ref = App.tbl_lit.BuscarID(Integer.parseInt(token.getRef())).getValor();
                } else {
                    ref = token.getRef();
                }
                App.tbl_error.agregarError("SINTACTICO", ref.replace("\n", "").replace(" ", ""),
                        token.getLinea() + "",
                        token.getColumna() + "",
                        String.format("Se esperaba %s, se encontro", descripcion));
                pila.push(prod_act);
                return true; // Avanzar token
            case "\"\"\"\"":
                App.tbl_error.agregarError("SINTACTICO", token.getRef(), token.getLinea() + "",
                        token.getColumna() + "",
                        String.format("Se esperaba %s, se encontro", descripcion));
                return AnalizarToken(token); // Reintentar con el mismo token
            default:
                return true;
        }
    }

    /**
     * Metodo encargado de expandir la produccion y proseguir con el analisis
     * El metodo saca el ultimo no terminal de la pila, obtiene la siguiente
     * produccion, la coloca de manera inversa en la cima de la pila, y vuelve a
     * analizar con el token actual
     * 
     * @param token
     * @return
     */
    private boolean expandirProduccion(Token token) {
        String prod_act = pila.pop();
        String prod_sig = tbl_ll.get(obtenerClaveProduccion(prod_act));

        if (prod_sig == null) {
            App.tbl_error.agregarError("SINTACTICO", prod_act, linea, columna,
                    String.format("No se encontró producción para <%s, %s>", prod_act, ref));
            return false;
        }

        if (prod_sig.equals("&") || prod_sig.equals("`") || prod_sig.equals("\"\"\"\""))
            return procesarProduccionEspecial(token, prod_sig, prod_act);

        colocarProduccionEnPila(prod_sig);
        return AnalizarToken(token);
    }

    /**
     * Determina como estara formada la clave para la produccion a obtener.
     * Hace uso del ultimo no terminal recibido, usando el token actual
     * verifica si la clave de produccion es con Tipo o Lexema
     * 
     * @param prod_act -> ultimo no terminal de la pila
     * @return Produccion siguiente en la tabla LL
     */
    private String obtenerClaveProduccion(String prod_act) {
        if (tipo.equals("ID") || tipo.equals("IDM") || tipo.equals("IDF") || tipo.equals("IDP")) {
            return String.format("%s,%s", prod_act, "ID");
        }

        if (tipo.equals("LITERAL")) {
            Literal lit = App.tbl_lit.BuscarID(Integer.parseInt(ref));
            return String.format("%s,%s", prod_act, lit.getComp());
        }

        return String.format("%s,%s", prod_act, ref);
    }

    /**
     * Metodo encargado de desglosar y apilar la produccion recibida de manera
     * inversa en la pila
     * 
     * @param produccion -> produccion a desglosar y apilar
     */
    private void colocarProduccionEnPila(String produccion) {
        if (produccion.contains("~")) {
            String[] elementos = produccion.split("~");
            for (int i = elementos.length - 1; i >= 0; i--)
                pila.push(elementos[i]);
        } else
            pila.push(produccion);

    }

    /**
     * Metodo encargado de importar el archivo excel de la ruta indicada
     * El metodo obtiene la primera fila del documento y la separa para obtener
     * los terminales de la tabla. Evita el primer elemento de esa fila (no es
     * relevante).
     * Despues va obteniendo las siguientes lineas, las separa y el primer elemento
     * lo reguarda como no Terminal, y lo utiliza para formar la siguiente clave con
     * la respectiva produccion.
     * (no Terminal (fila), Terminal(columna)) -> produccion(celda de interseccion)
     * 
     * @implSpec El archivo excel debe ser un archivo separado por tabulaciones, y
     *           hay que tener cuidado con algunos simbolos porque excel agrega "" a
     *           estas celdas y pasan como caracter extra a la hora de la
     *           importacion
     * 
     * @param ruta
     */
    public void importarExcel(InputStream input) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            String line = br.readLine();
            term = new Vector<>();

            // ? Crea un vector con todos los nombres de las cabeceras
            for (String ter : line.split("\t")) {
                if (ter.contains("LL"))
                    continue;
                term.add(ter);
            }

            // ? Llena la tbl ll
            while ((line = br.readLine()) != null) {
                int conv = 0;
                boolean ban = true;
                String noTer = "*";

                for (String elem : line.split("\t")) {
                    if (ban) {
                        noTer = elem;
                        ban = false;
                        continue;
                    }
                    tbl_ll.put(String.format("%s,%s", noTer, term.get(conv++)), elem);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al importar la tbl: " + e);
        }
    }

}
