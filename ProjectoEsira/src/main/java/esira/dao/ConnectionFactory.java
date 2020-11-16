/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.dao;

/**
 *
 * @author user
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author User
 */
public class ConnectionFactory {

    public static ConnectionFactory factory=null;
    private Connection con;

    public static ConnectionFactory getInstance(){
        if(factory == null){
//            return new ConnectionFactory();
            return factory = new ConnectionFactory();
        }
        return factory;
    }

    public  ConnectionFactory(){
        System.out.println("Conectando ao banco");
        try {
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/fecn1", "postgres", "seul2020");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Connection getConnection() {
        return con;
    }

    public static void main(String[] args) throws SQLException {
        Connection c = ConnectionFactory.getInstance().getConnection();
        
        String sql = " select r.id_categoria as acesso from fecn1.roles r, fecn1.users u where r.id_grupo = u.id_grupo AND " +
"  u.utilizador = ? GROUP BY r.id_categoria union select r.id_item from  fecn1.roles r, fecn1.users u where r.id_grupo = u.id_grupo AND " +
"  u.utilizador = ?";
        //CallableStatement callableStmt = ConnectionFactory.getInstance().getConnection().prepareCall(sql);
        PreparedStatement pstm = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, "admin");
        pstm.setString(2, "admin");
        ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                String m = rs.getString("acesso");
                System.out.println(m);
            }
            rs.close();
            pstm.close(); 
    }
//
}
