package app;



import java.util.Date;

import pagina12.DescargarNotasEconomiaPagina12;

public class OptimizadorHilos {

	public static void main(String[] args) {
		System.out.println("cantidad hilos;seg");
		for (Integer i = 1; i < 100; i++) {
			String[] hilos = new String[1];
			hilos[0] = i.toString();
			long init = new Date().getTime();
			DescargarNotasEconomiaPagina12.main(hilos);//TODO:  y si quiero usar los dos?
			long fin = new Date().getTime();
			System.out.println(i+ ";"+(fin-init)/1000);
		}
	}
}