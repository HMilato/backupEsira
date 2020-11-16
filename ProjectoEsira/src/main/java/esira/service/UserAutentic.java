/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esira.service;

import esira.domain.Estudante;
import esira.domain.Item;
import esira.domain.Users;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author slima
 */
public class UserAutentic {
    private static CRUDService CRUDService;// = (CRUDService) org.zkoss.zkplus.spring.SpringUtil.getBean("CRUDService");
    static Users u;
    static boolean uestudante = false;
    static int tab = 0;
    private static String user;
//    private static long idf;
    private static List<String> rs = new ArrayList<String>();
    private static boolean sel = false;
    private static Long ide;
    private static int idfac;


    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        UserAutentic.user = user;
    }

    public static boolean isUestudante() {
        return uestudante;
    }

//    public static long getIdf() {
//        return idf;
//    }
//
//    public static void setIdf(long idf) {
//        UserAutentic.idf = idf;
//    }

    public static CRUDService getCRUDService() {
        return CRUDService;
    }

    public static void setCRUDService(CRUDService CRUDService) {
        UserAutentic.CRUDService = CRUDService;
    }

    public static Users getU() {
        return u;
    }

    public static void setU(Users u) {
        UserAutentic.u = u;
    }

    public static int getIdfac() {
        return idfac;
    }

    public static void setIdfac(int idfac) {
        UserAutentic.idfac = idfac;
    }
    
    

    public static void setUestudante(boolean uestudante) {
        UserAutentic.uestudante = uestudante;
    }

    public static int getTab() {
        return tab;
    }

    public static void setTab(int tab) {
        UserAutentic.tab = tab;
    }
    
    public static String getPathR(String p) throws IOException{
      BufferedReader bufReader = new BufferedReader(
                new InputStreamReader(Sessions.getCurrent().getWebApp().getServletContext().getResourceAsStream("/path.properties")));
        String prop = null;
        while ((prop = bufReader.readLine()) != null) {
          //  int begin = prop.indexOf("=");
            if (prop.startsWith(p)) {
                return prop.substring(2);
            }
        }
        return null;
    }
    
    public static String getEmailP(String p) throws IOException{
      BufferedReader bufReader = new BufferedReader(
                new InputStreamReader(Sessions.getCurrent().getWebApp().getServletContext().getResourceAsStream("/EmailPath.properties")));
        String prop = null;
        while ((prop = bufReader.readLine()) != null) {
          int begin = prop.indexOf("=");
            if (prop.startsWith(p)) {
                return prop.substring(begin+1);
            }
        }
        return null;
    }
    
    public static String getEmailP2(String p, ServletContext s) throws IOException{ //usado dentro de uma thread
      BufferedReader bufReader = new BufferedReader(
                new InputStreamReader(s.getResourceAsStream("/EmailPath.properties")));
        String prop = null;
        while ((prop = bufReader.readLine()) != null) {
          int begin = prop.indexOf("=");
            if (prop.startsWith(p)) {
                return prop.substring(begin+1);
            }
        }
        return null;
    }

    public static List<String> getRs() {
//       String sql = "{CALL List_roles(?)}";
//       
//
//              CallableStatement callableStmt = ConnectionFactory.getInstance().getConnection().prepareCall(sql);
//
//              callableStmt.setString(1, UserAutentic.getUser());
//
//              boolean moreResult = callableStmt.execute();
//            
//             
//            
//            while (moreResult) {
//                ResultSet rs = callableStmt.getResultSet();
//                while (rs.next()) {
//
//                    authList.add(new GrantedAuthorityImpl(rs.getString("acesso")));
//                }
//                rs.close();
//                moreResult = callableStmt.getMoreResults();
//            }
//            
//            
//            
//            callableStmt.close();
        return rs;
    }

    public static void setRs(String s) {
        rs.add(s);
    }

    public static boolean isSel() {
        return sel;
    }

    public static void setSel(boolean sel) {
        UserAutentic.sel = sel;
    }

    public static Long getIde() {
        return ide;
    }

    public static void setIde(Long ide) {
        UserAutentic.ide = ide;
    }

    
    

    
}
