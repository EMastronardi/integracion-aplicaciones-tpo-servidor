package utils;

public class Constantes {
	final static int id = 12;
	
	final static String ip = "";//nuestra ip.. no se si servir�
	final static String jmsEnviarADepDest = "jms/queue/solicitud";
	final static String jmsEnviarADepUser = "test1";
	final static String jmsEnviarADepPass = "test12341";
	final static String restEnviarNotiLog = ":8080/LogisticaMonitoreoWEB/rest/LogisticaMonitoreo/notificarEntregaDespacho";
	final static String wsEnviarNotiPortalWeb = ":8080/portalEstadoEntrega/ServidorEstadoEntregaBeanService/ServidorEstadoEntregaBean";
	
	public static int getId() {
		return id;
	}
	public static String getIp() {
		return ip;
	}
	public static String getJmsEnviarADepDest() {
		return jmsEnviarADepDest;
	}
	public static String getJmsenviaradepdest() {
		return jmsEnviarADepDest;
	}
	public static String getJmsEnviarADepUser() {
		return jmsEnviarADepUser;
	}
	public static String getJmsEnviarADepPass() {
		return jmsEnviarADepPass;
	}
	public static String getRestEnviarNotiLog(){
		return restEnviarNotiLog;
	}
	public static String getWsEnviarNotiPortalWeb(){
		return wsEnviarNotiPortalWeb;
	}
}
