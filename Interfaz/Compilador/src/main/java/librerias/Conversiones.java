package librerias;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import javax.swing.table.DefaultTableModel;

public class Conversiones
{
        public DefaultTableModel convertirTM(HashMap<String, String> tokens) {
        // Definimos las columnas de la tabla
        String[] columnas = {"Lexema", "Componente", "Línea", "Posición"};

        // Creamos el modelo vacío
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        // Ordenamos el mapa por las llaves (IDs) usando TreeMap
        TreeMap<String, String> tokensOrdenados = new TreeMap<>(tokens);

        for (Map.Entry<String, String> entrada : tokensOrdenados.entrySet()) {
            String datos = entrada.getValue();
            String[] partes = datos.split(",", -1); // "-1" para que no pierda campos vacíos

            if (partes.length == 4) {
                modelo.addRow(new Object[]{partes[0], partes[1], partes[2], partes[3]});
            } else {
                System.out.println("Error en formato de token (ID " + entrada.getKey() + "): " + datos);
            }
        }

        return modelo;
    }
    
	// Conversion de una Matriz -> HashMap <T> -> Cualquier tipo de dato
	public <T> HashMap<String, HashMap<String, T>> ConvMHM(T[][] mat, T[] enc)
	{
		HashMap<String, HashMap<String, T>> map = new HashMap<>();
		for (int i = 0; i < mat.length; i++)
		{
			HashMap<String, T> filaMap = new HashMap<>();
			for (int j = 0; j < mat[0].length; j++)
			{
				filaMap.put(enc[j] + "", mat[i][j]);
			}
			map.put(i + "", filaMap);
		}
		return map;
	}

	// Conversion de un Array -> HashSet
	public <T> HashSet<T> ConvAHS(T[] array)
	{
		HashSet<T> lista = new HashSet<>();
		for (int i = 0; i < array.length; i++)
		{
			lista.add(array[i]);
		}
		return lista;
	}

	// Conversion de una Cadena -> HashSet
	public HashSet<String> ConvCHS(String cad)
	{
		HashSet<String> lista = new HashSet<>();
		for (int i = 0; i < cad.length(); i++)
		{
			lista.add(cad.charAt(i) + "");
		}
		return lista;
	}

	// Muestra un HashMap
	public <T, K, V> void MostrarHM(HashMap<String, HashMap<String, T>> map)
	{
		for (Entry<String, HashMap<String, T>> fila : map.entrySet())
		{
			String filaIndex = fila.getKey();
			HashMap<String, T> columnas = fila.getValue();

			for (Entry<String, T> columna : columnas.entrySet())
			{
				String columnaIndex = columna.getKey();
				T valor = columna.getValue();
				System.out.println("Fila: " + filaIndex + ", Columna: " + columnaIndex + ", Valor: " + valor);
			}
		}
	}

	public <T> void MostrarHS(HashSet<T> lista)
	{
		String cad = "";
		for (T elem : lista)
		{
			cad += elem;
		}
		System.out.println("HS: " + cad);
	}
}
