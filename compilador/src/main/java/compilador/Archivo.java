package compilador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Vector;

/**
 * Clase para manejo de archivos, cada instancia de esta clase hace referencia a
 * un unico archivo.
 * 
 * @author Ludwig Ivan Ortiz Sierra
 */
public class Archivo {
	private Path ruta;

	/**
	 * Constructor que recibe la ruta del archivo y la almacena para su posterior
	 * uso.
	 * 
	 * @param r : ruta del archivo
	 */
	public Archivo(String r) {
		ruta = Paths.get(r);
	}

	public Path getRuta() {
		return ruta;
	}

	/**
	 * Constructor que recibe la ruta, nombre de archivo y extension para la
	 * creacion de un nuevo archivo.
	 * 
	 * @param r   : ruta del archivo
	 * @param nom : nombre del archivo
	 * @param ext : extension del archivo
	 */
	public Archivo(String r, String nom, String ext) {
		Path ruta = Paths.get(String.format("%s\\%s%s", r, nom, ext));

		if (!Files.exists(ruta))
			try {
				Files.createDirectories(ruta.getParent());
				ruta = Files.createFile(ruta);
			} catch (IOException e) {
				System.err.println("Error al crear el archivo");
			}
		this.ruta = ruta;
	}

	/**
	 * Metodo para convertir el archivo a una cadena (String), en caso de no
	 * encontrar el archivo o algun error, devuelve Null.
	 * 
	 * @return String (Object) | Null
	 */
	public String toString() {
		String arc = "";
		List<String> lineas;
		if (Files.exists(ruta))
			try {
				lineas = Files.readAllLines(ruta);
				for (String linea : lineas)
					arc += linea + "\n";
			} catch (IOException e) {
				System.err.println("Error al leer el archivo" + e);
				return null;
			}
		else
			return null;

		return arc;
	}

	/**
	 * Se encarga de convertir un archivo con texto a un vector con cada linea del
	 * documento
	 * 
	 * @return -> Devuelve un vector con cada una de las lineas del documento leido
	 *         o devuelve null en caso de no procesar ninguna linea
	 */
	public Vector<String> toArrayLine() {
		try (BufferedReader obb = new BufferedReader(new FileReader(ruta.toString()))) {
			String line;
			Vector<String> file = new Vector<>();

			while ((line = obb.readLine()) != null)
				file.add(line);

			return file;

		} catch (Exception e) {
			System.out.println("Error al leer el documento" + e);
		}

		return null;
	}

	/**
	 * Metodo que sobreescribe el texto recibido en el archivo actual. En caso de no
	 * existir el archivo se creara uno nuevo. Si los directorios no existen tambien
	 * se crearan.
	 * 
	 * @param txt : texto a escribir en el documento
	 */
	public void write(String txt) {
		try {
			Files.createDirectories(ruta.getParent());
			Files.writeString(ruta, txt, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			System.err.println("Error al escribir en el archivo");
		}
	}

	/**
	 * Metodo que escribe el texto recibido al final del archivo actual. En caso de
	 * no existir el archivo se creara uno nuevo. Si los directorios no existen
	 * tambien se crearan.
	 * 
	 * @param txt : texto a escribir al final del documento
	 */
	public void writeAppend(String txt) {
		try {
			Files.createDirectories(ruta.getParent());
			Files.writeString(ruta, txt, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			System.err.println("Error al escribir en el archivo");
		}
	}

	/**
	 * Metodo que elimina el archivo actual si existe, regresando true en caso de
	 * eliminarlo, en caso contrario false
	 * 
	 * @return boolean
	 */
	public boolean delete() {
		try {
			return Files.deleteIfExists(ruta);
		} catch (IOException e) {
			System.err.println("Error al eliminar el archivo");
		}

		return false;
	}

	/**
	 * Metodo que mueve el archivo actual a un numero destino. Si en la ruta destino
	 * el nombre cambia, automaticamente el archivo se renombra. Si el directorio no
	 * existe, lo crea de maner automatica.
	 * 
	 * @param ruta_destino : ruta a la que se movera el archivo
	 */
	public void move(String ruta_destino) {
		Path rutades = Paths.get(ruta_destino);
		try {
			Files.createDirectories(rutades.getParent());
			Files.move(ruta, rutades, StandardCopyOption.REPLACE_EXISTING);
			ruta = rutades;
		} catch (IOException e) {
			System.err.println("Error al mover el archivo: " + e.getMessage());
		}
	}

	/**
	 * Metodo que copea el archivo actual a un numero destino. En caso de error
	 * devuelve null. Si no existen los directorios los crea de manera automatica.
	 * 
	 * @param ruta_destino : ruta a la que se copeara el archivo
	 * @return Archivo (Object) | Null
	 */
	public Archivo copy(String ruta_destino) {
		Archivo newArc = null;
		Path rutades = Paths.get(ruta_destino);
		try {
			Files.createDirectories(rutades.getParent());
			Files.copy(ruta, rutades, StandardCopyOption.REPLACE_EXISTING);
			newArc = new Archivo(rutades.toString());
		} catch (IOException e) {
			System.err.println("Error al mover el archivo: " + e.getMessage());
		}
		return newArc;
	}
}
