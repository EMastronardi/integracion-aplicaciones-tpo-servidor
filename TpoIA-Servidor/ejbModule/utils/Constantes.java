package utils;

public class Constantes {
	final static int id = 12;
	
	final static String ip = "";//nuestra ip.. no se si servirá
	final static String jmsEnviarADepDest = "jms/queue/solicitud";
	final static String jmsEnviarADepUser = "test1";
	final static String jmsEnviarADepPass = "test12341";
	final static String restEnviarNotiLog = "/LogisticaMonitoreoWEB/rest/LogisticaMonitoreo/notificarEntregaDespacho";
	
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

}
