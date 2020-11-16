package esira.hibernate;

import esira.hibernate.Login;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.ServletWebRequest;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

public class TenantIdResolver implements CurrentTenantIdentifierResolver {

    private static final ThreadLocal<String> tenant = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return "fecn1";
        }
    };

    public static void setTenant(String tenant) {
        TenantIdResolver.tenant.set(tenant); 
    }

    public static String getTenant() {
//       return TenantIdResolver.tenant.get();
        Object o = null;
        Session s = Sessions.getCurrent();
        if (s != null) {
            o = s.getAttribute("tenant");
            if(o!=null)return (String)o;
        }
        return "fecn1";
    }

    @Autowired
    private HttpSession httpSession;

    @Override
    public String resolveCurrentTenantIdentifier() {
//	    HttpServletRequest req = ((ServletWebRequest) RequestCycle.get().getRequest()).getContainerRequest();
//        Object o = null;
//        if (httpSession != null) {
//            o = httpSession.getAttribute("tenant");
//        }
//        if (o != null) {
//            return (String) o;
//        }
        ///req.getSession().setAttribute("tenant", tenant);	
//            return t;//Login.getTenantId();
//        return Login.getTenantId();
//        return tenant.get();
        Object o = null;
        Session s = Sessions.getCurrent();
        if (s != null) {
            o = s.getAttribute("tenant");
            if(o!=null)return (String)o;
        }
        return "fecn1";
//         return TenantIdResolver.tenant.get();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

}
