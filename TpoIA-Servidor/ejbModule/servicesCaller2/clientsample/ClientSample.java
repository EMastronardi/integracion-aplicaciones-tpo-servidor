package servicesCaller2.clientsample;

import servicesCaller2.*;

public class ClientSample {

	public static void main(String[] args) {
	        System.out.println("***********************");
	        System.out.println("Create Web Service Client...");
	        ServidorEstadoEntregaBeanService service1 = new ServidorEstadoEntregaBeanService();
	        System.out.println("Create Web Service...");
	        ServidorEstadoEntregaBean port1 = service1.getServidorEstadoEntregaBeanPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port1.notificarEntregaDespacho(Integer.parseInt(args[0])));
	        System.out.println("Create Web Service...");
	        ServidorEstadoEntregaBean port2 = service1.getServidorEstadoEntregaBeanPort();
	        System.out.println("Call Web Service Operation...");
	        System.out.println("Server said: " + port2.notificarEntregaDespacho(Integer.parseInt(args[1])));
	        System.out.println("***********************");
	        System.out.println("Call Over!");
	}
}
