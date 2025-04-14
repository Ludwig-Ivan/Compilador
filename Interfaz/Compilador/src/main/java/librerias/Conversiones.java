package librerias;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class Conversiones
{
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
