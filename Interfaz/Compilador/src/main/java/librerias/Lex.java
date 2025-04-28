package librerias;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lex
{
	private Map<String, String> patrones;
	private HashMap<String, String> tbl_simbolos;

	public Lex()
	{
		// Detalle con el reconocimiento de los patrones se sobreponen unos patrones
		// sobre otros, indicar prioridad
		patrones = Map.ofEntries(Map.entry("IDENTIFICADOR", "[a-zA-Z_]\\w*"), Map.entry("ESPACIO", "\\s+"),
				Map.entry("PUNTOCOMA", ";"), Map.entry("MAS", "\\+"), Map.entry("MENOS", "-"), Map.entry("MULT", "\\*"),
				Map.entry("DIV", "/"), Map.entry("PORCENTAJE", "%"), Map.entry("ASIGNACION", "="),
				Map.entry("IGUAL", "=="), Map.entry("DIF", "!="), Map.entry("MENORQ", "<"), Map.entry("MAYORQ", ">"),
				Map.entry("MENORIG", "<="), Map.entry("MAYORIG", ">="), Map.entry("AND", "&&"),
				Map.entry("OR", "\\|\\|"), Map.entry("NOT", "!"), Map.entry("COMA", ","), Map.entry("PARI", "\\("),
				Map.entry("PARF", "\\)"), Map.entry("LLAVEI", "\\{"), Map.entry("LLAVEF", "\\}"),
				Map.entry("CORCHI", "\\["), Map.entry("CORCHF", "\\]"), Map.entry("IF", "if|IF"),
				Map.entry("ELSE", "else|ELSE"), Map.entry("WHILE", "WHILE|while"), Map.entry("DO", "DO|do"),
				Map.entry("RETURN", "return|RETURN"), Map.entry("PRINT", "print|PRINT"), Map.entry("TINT", "int|INT"),
				Map.entry("TFLOAT", "float|FLOAT"), Map.entry("TCHAR", "char|CHAR"),
				Map.entry("TCADENA", "cadena|CADENA"), Map.entry("TBOOL", "bool|BOOL"), Map.entry("TVOID", "void|VOID"),
				Map.entry("COMENTARIOL", "\\/\\/.*"), Map.entry("INICOMENTARIOB", "\\/\\*"),
				Map.entry("FINCOMENTARIOB", "\\*\\/"), Map.entry("INT", "\\d+"), Map.entry("DECIMAL", "\\d+\\.\\d+"),
				Map.entry("CADENA", "\".*?\""), Map.entry("CARACTER", "'.*?'"), Map.entry("TRUE", "true|TRUE"),
				Map.entry("FALSE", "false|FALSE"), Map.entry("BREAK", "BREAK|break"),
				Map.entry("CONTINUE", "CONTINUE|continue"), Map.entry("FUNC", "FUNC|func"),
				Map.entry("INTERROGACION", "\\?"), Map.entry("DECREMENTO", "--"), Map.entry("INCREMENTO", "\\+\\+"),
				Map.entry("DOSPUNTO", "\\:"));

	}

	public Vector<String> Pre_Procesado(Vector<String> array)
	{
		boolean blockcomment = false;
		Vector<String> temp = new Vector<>();

		for (String line : array)
		{
			StringBuilder lineLimpia = new StringBuilder();
			int con = 0;

			// Eliminar comentarios de línea
			line = line.replaceAll("//.*", "");
			// Reemplazar múltiples espacios por uno
			// line = line.replaceAll("\\s+", " ").trim();

			while (con < line.length())
			{
				// Asegurarse de no salirse de rango
				if (con + 1 < line.length())
				{
					if (!blockcomment)
					{
						if (line.charAt(con) == '/' && line.charAt(con + 1) == '*')
						{
							blockcomment = true;
							con += 2; // Saltar '/*'
							continue;
						}
					}
					else
						if (line.charAt(con) == '*' && line.charAt(con + 1) == '/')
						{
							blockcomment = false;
							con += 2; // Saltar '*/'
							continue;
						}
				}

				if (!blockcomment)
					lineLimpia.append(line.charAt(con));

				con++;
			}

			// Agregar línea limpia si no está vacía
			String cleaned = lineLimpia.toString();
			if (!cleaned.isEmpty())
				temp.add(cleaned);
		}

		return temp;
	}

	public HashMap<String, String> Lexico(Vector<String> c)
	{
		int con_elem = 0, con = 0;
		StringBuilder patronTotal = new StringBuilder();
		tbl_simbolos = new HashMap<>();

		// Lista ordenada con prioridad de tokens
		List<String> prioridad = List.of(
				// Comentarios primero
				"COMENTARIOL", "INICOMENTARIOB", "FINCOMENTARIOB",
				// Palabras clave y booleanos
				"IF", "ELSE", "WHILE", "DO", "RETURN", "BREAK", "CONTINUE", "PRINT", "FUNC", "TRUE", "FALSE",
				// Tipos de datos
				"TINT", "TFLOAT", "TCHAR", "TCADENA", "TBOOL", "TVOID",
				// Literales
				"DECIMAL", "INT", "CADENA", "CARACTER",
				// Operadores dobles primero
				"INCREMENTO", "DECREMENTO", "IGUAL", "DIF", "MENORIG", "MAYORIG", "AND", "OR",
				// Operadores simples
				"ASIGNACION", "MAS", "MENOS", "MULT", "DIV", "PORCENTAJE", "NOT", "INTERROGACION", "MAYORQ", "MENORQ",
				// Delimitadores
				"DOSPUNTO", "PUNTOCOMA", "COMA", "PARI", "PARF", "LLAVEI", "LLAVEF", "CORCHI", "CORCHF",
				// Identificador
				"IDENTIFICADOR",
				// Espacios (último para ignorarlos)
				"ESPACIO");

		// Construcción del patrón total en orden de prioridad
		for (String clave : prioridad)
			if (patrones.containsKey(clave))
				patronTotal.append(String.format("|(?<%s>%s)", clave, patrones.get(clave)));

		// Compilar el patrón eliminando el primer '|'
		Pattern pattern = Pattern.compile(patronTotal.substring(1));

		for (String line : c)
		{
			Matcher matcher = pattern.matcher(line);
			while (matcher.find())
				for (String tipo : patrones.keySet())
					if (matcher.group(tipo) != null && !tipo.equals("ESPACIO"))
					{
						tbl_simbolos.put(con_elem + "",
								String.format("%s,%s,%s,%s", tipo, matcher.group(tipo), con, matcher.start()));
						con_elem++;
					}
			con++;
		}
		return tbl_simbolos;
	}
}
