package entities;

public abstract class SeccionLaNacion extends Seccion{
	private final String nombre = "La Nación";

	public SeccionLaNacion(){
		super.setNombreDiario(nombre);
	}
}
