package librerias;

public class Menu
{
	public Datos obd = new Datos();
	private String titulo, menu[];
	private int pos = 0;

	Menu()
	{
		int tam;
		do
		{
			tam = obd.Entero("Tamano del menu");
		}
		while (tam < 1);
		this.menu = new String[tam + 1];

		this.TituloOpciones(tam);
	}

	Menu(int tam)
	{
		this.menu = new String[tam + 1];
		this.TituloOpciones(tam);
	}

	Menu(String vec[], String titulo)
	{
		this.menu = new String[vec.length + 1];
		this.titulo = titulo;
		for (pos = 0; pos < vec.length; pos++)
			menu[pos] = vec[pos].toUpperCase();
		this.menu[pos] = "SALIR";
	}

	public Menu(String titulo, String vec[])
	{
		this(vec, titulo);
	}

	public void TituloOpciones(int tam)
	{
		this.titulo = obd.Cadena("Dame el titulo del menu").toUpperCase();

		System.out.println("Escribe las opciones de menu");
		for (tam = 0; tam < menu.length - 1; tam++)
			menu[tam] = obd.Cadena("Dame la opcion " + (tam + 1)).toUpperCase();
		menu[tam] = "SALIR";
	}

	private void Mostrar()
	{
		System.out.println(this.titulo);
		for (pos = 0; pos < this.menu.length; pos++)
			System.out.println(" " + (pos + 1) + "-." + this.menu[pos]);
	}

	public int Opcion()
	{
		int op;
		do
		{
			this.Mostrar();
			op = obd.Entero("Dame tu opcion...");
		}
		while (op < 1 || op > menu.length);
		return op;
	}

	public int Salir()
	{
		return menu.length;
	}
}
