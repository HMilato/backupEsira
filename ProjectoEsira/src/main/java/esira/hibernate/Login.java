package esira.hibernate;

public class Login {
	
	private static String tenantId="fecn1";

	public static String getTenantId() {
		return tenantId;
	}

	public static void setTenantId(String myTenantId) {
		tenantId = myTenantId;
	}

}
