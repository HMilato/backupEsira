package esira.service;

import esira.dao.ConnectionFactory;
import esira.domain.Users;
import esira.domain.Utilizadorgeral;
import esira.hibernate.Login;
import esira.hibernate.MultiTenantConnectionProviderImpl;
import esira.hibernate.TenantIdResolver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.zkoss.zk.ui.Sessions;

public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private CRUDService CRUDService;
//    @Autowired
//    SessionFactory sessionFactory;
    
//    static 
    static Utilizadorgeral dbUser = new Utilizadorgeral();
//    static Users dbUser = new Users();

    /*
     * You just have to make sure that the user-by-username-query returns three
     * fields. 1) the userName 2) the password 3) boolean for is the user
     * active. If you don't have an active field, make your query always return
     * true for that third field.
     */
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException {
        // Declare a null Spring User
        UserDetails user = null;

        try {

            // Search database for a user that matches the specified username
            // You can provide a custom DAO to access your persistence layer
            // Or use JDBC to access your database
            // DbUser is our custom domain user. This is not the same as
            // Spring's User
//            dbUser = CRUDService.GetUniqueEntityByNamedQuery(
//                    "Users.findByUtilizador", username);
            dbUser = CRUDService.findEntByJPQueryT("SELECT u FROM Utilizadorgeral u WHERE u.utilizador = '" + username + "'", null);
          //  dbUser = CRUDService.findEntByJPQueryT("SELECT u FROM Users u WHERE u.utilizador = '" + username + "'", null);
//            Login.setTenantId(dbUser.getTenant());
//            TenantIdResolver.setTenant(dbUser.getTenant());
//            Sessions.getCurrent().setAttribute("tenant",dbUser.getTenant());
            //  dbUser.getIdGrupo().
            //   for (esira.domain.Item i : dbUser.getIdGrupo().getItemList()) {
            // UserAutentic.setUser(dbUser.getUtilizador());
//            UserAutentic.setUestudante(dbUser.getUestudante());
            //UserAutentic.setIdf(dbUser.getIdUtilizador());
            //UserAutentic.setIdfac(dbUser.getFaculdade());
            //  }

            // Populate the Spring User object with details from the dbUser
            // Here we just pass the username, password, and access level
            // getAuthorities() will translate the access level to the correctdbUser.getIdGrupo().getItemList().get(0).getCategoria().getIdCategoria()
            // role type
//            user = new User(dbUser.getUtilizador(), dbUser.getPasword()
//                    .toLowerCase(), true, true, true, true,
//                    getAuthorities(dbUser.getUtilizador(), dbUser.getTenant()));
            user = new Utilizador(dbUser.getUtilizador(), dbUser.getPasword()
                    ,getAuthorities(dbUser.getUtilizador(), dbUser.getTenant()), dbUser.getTenant());

        } catch (Exception e) {
            throw new UsernameNotFoundException("Error in retrieving user");
        }
        return user;
    }

    /**
     * Retrieves the correct ROLE type depending on the access level, where
     * access level is an Integer. Basically, this interprets the access value
     * whether it's for a regular user or admin.
     *
     * @param u
     * @param schema
     * @param access an integer value representing the access of the user
     * @return collection of granted authorities
     * @throws java.sql.SQLException
     */
    public Collection<GrantedAuthority> getAuthorities(String u, String schema) throws SQLException {
        // Create a list of grants for this user
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        // All users are granted with ROLE_USER access
        // Therefore this user gets a ROLE_USER by default
        //String sql = "select * from fecn2.acesso('"+UserAutentic.getUser()+"')";
        String sql = " select r.id_categoria as acesso from " + schema + ".roles r, " + schema + ".users u where r.id_grupo = u.id_grupo AND "
                + "  u.utilizador = ? GROUP BY r.id_categoria union select r.id_item from  " + schema + ".roles r, " + schema + ".users u where r.id_grupo = u.id_grupo AND "
                + "  u.utilizador = ?";
        //CallableStatement callableStmt = ConnectionFactory.getInstance().getConnection().prepareCall(sql);
     //   PreparedStatement pstm = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
     PreparedStatement pstm = new MultiTenantConnectionProviderImpl().getConnection(schema).prepareStatement(sql);  
     
     pstm.setString(1, u);
        pstm.setString(2, u);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            authList.add(new GrantedAuthorityImpl(rs.getString("acesso")));
        }
        rs.close();
        pstm.close();
        return authList;
    }
}
