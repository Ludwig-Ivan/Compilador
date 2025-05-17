package compilador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

public class Sintactico {

    private Stack<String> pila = new Stack<>(); // ? Se encarga de resguardar la estructura correcta del programa
    private HashMap<String, String> tbl_ll = new HashMap<>(); // ? estructura que reguarda la tabla LL
    private Vector<String> term; // ? Lista de terminales
    public List<String> errores; // ? Almacena los errores
    private String tipo = "", lexema = "", linea = "", columna = "", cima = "";

    /**
     * Constructor para dar pie al analisis sintactico
     * El simbolo inicial por defecto es "P"
     * 
     * @param sim_ini -> Simbolo inicial para el analisis
     */
    public Sintactico(String sim_ini) {
        pila.push("$");
        pila.push(sim_ini != null ? sim_ini : "P");
        errores = new ArrayList<>();
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
        System.out.println("Pila: " + pila);
        if (!extraerDatosToken(token))
            return false;
        cima = pila.peek();
        if (term.contains(cima))
            return procesarTerminal();
        if (cima.equals("@") || cima.equals("`") || cima.equals("\"\"\"\""))
            return procesarProduccionEspecial(token);
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
            errores.add("Error: fin de entrada inesperado");
            return false;
        }

        String[] elem = token.toString().split("~");
        linea = elem[0];
        columna = elem[1];
        tipo = elem[2];
        lexema = elem[3];

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
    private boolean procesarTerminal() {
        if (cima.equals(tipo) || cima.equals(lexema))
            pila.pop();
        else
            errores.add(String.format(
                    "Error en la línea %s, columna %s: Se esperaba '%s', se encontró '%s'",
                    linea, columna, pila.pop(), lexema));
        return true;
    }

    /**
     * Metodo encargado de procesar las producciones especiales
     * ("@" : Epsilon | "`" : Saltar | """" : Sacar)
     * Si es epsilon, saca el ultimo elemento de la pila, y vuelve a realizar el
     * analisis con el token actual.
     * Si es ` o """" saca el ultimo elemenmto de la pila, y manda un error
     * dependiendo del caso.
     * 
     * @param token -> Token para aplicar analsis en caso de epsilon
     * @return true | false para indicar que se proceso de manera correcta la
     *         produccion
     */
    private boolean procesarProduccionEspecial(Token token) {
        switch (cima) {
            case "@":
                pila.pop(); // Epsilon (vacío)
                return AnalizarToken(token);
            case "`":
                pila.pop(); // Panic mode
                errores.add(String.format(
                        "Error en la línea %s, columna %s: Token inesperado '%s', se omitió",
                        linea, columna, lexema));
                return true;
            case "\"\"\"\"":
                pila.pop(); // Descartar token
                errores.add(String.format(
                        "Error en la línea %s, columna %s: Token '%s' descartado",
                        linea, columna, lexema));
                return true;
            default:
                return true; // No debería ocurrir
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
            errores.add(String.format(
                    "Error en la línea %s, columna %s: No se encontró producción para <%s, %s>",
                    linea, columna, prod_act, lexema));
            return false;
        }

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
        if (tipo.equals("ID") || tipo.equals("NUMERO") || tipo.equals("DECIMAL") ||
                tipo.equals("CADENA") || tipo.equals("CARACTER")) {
            return String.format("%s,%s", prod_act, tipo);
        }
        return String.format("%s,%s", prod_act, lexema);
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
    public void importarExcel(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
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
