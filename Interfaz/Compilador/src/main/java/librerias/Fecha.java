package librerias;

import java.util.Calendar;

public class Fecha
{
	private Calendar ref;
	private String fecha = "";

	public Fecha()
	{
		ref = Calendar.getInstance();
	}

	public void Mostrar()
	{
		this.Nombredia();
		this.NoDia();
		this.NombreMes();
		this.Año();
		System.out.println(fecha);
	}

	public Fecha(int dd, int mm, int ano)
	{
		ref = Calendar.getInstance();
		ref.set(ano, mm - 1, dd);
	}

	private void Nombredia()
	{
		switch (ref.get(Calendar.DAY_OF_WEEK))
		{
			case 1:
				fecha += "Domingo";
				break;
			case 2:
				fecha += "Lunes";
				break;
			case 3:
				fecha += "Martes";
				break;
			case 4:
				fecha += "Miercoles";
				break;
			case 5:
				fecha += "Jueves";
				break;
			case 6:
				fecha += "Viernes";
				break;
			case 7:
				fecha += "Sabado";
		}
		fecha += ", ";
	}

	private void NoDia()
	{
		fecha += ref.get(Calendar.DAY_OF_MONTH) + " de ";
	}

	private void NombreMes()
	{
		switch (ref.get(Calendar.DAY_OF_WEEK))
		{
			case 1:
				fecha += "Enero";
				break;
			case 2:
				fecha += "Febrero";
				break;
			case 3:
				fecha += "Marzo";
				break;
			case 4:
				fecha += "Abril";
				break;
			case 5:
				fecha += "Mayo";
				break;
			case 6:
				fecha += "Junio";
				break;
			case 7:
				fecha += "Julio";
				break;
			case 8:
				fecha += "Agosto";
				break;
			case 9:
				fecha += "Septiembre";
				break;
			case 10:
				fecha += "Octubre";
				break;
			case 11:
				fecha += "Noviembre";
				break;
			case 12:
				fecha += "Diciembre";
		}
		fecha += " del ";
	}

	private void Año()
	{
		fecha += ref.get(Calendar.YEAR);
	}

	private void NoDia2()
	{
		fecha += ref.get(Calendar.DAY_OF_MONTH) < 9 ? "0" + ref.get(Calendar.DAY_OF_MONTH)
				: ref.get(Calendar.DAY_OF_MONTH);
	}

	private void NoMes2()
	{
		fecha += ref.get(Calendar.MONTH) < 10 ? "0" + ref.get(Calendar.MONTH) + 1 : ref.get(Calendar.MONTH) + 1;
	}

	public String Hoy()
	{
		fecha = "";
		this.Nombredia();
		this.NoDia();
		this.NombreMes();
		this.Año();
		return fecha;
	}

	public String HoyS()
	{
		fecha = "";
		this.NoDia2();
		fecha += "/";
		this.NoMes2();
		fecha += "/";
		this.Año();
		return fecha;
	}
	
	public int Edad(int dia, int mes, int ano)
	{
		mes--;
		int edad=ref.get(Calendar.YEAR)-ano;
		if(mes>ref.get(Calendar.MONTH))
			edad--;
		else
			if(mes== ref.get(Calendar.MONTH) && dia>ref.get(Calendar.DAY_OF_MONTH))
				edad--;
		return edad;
				
	}
}
