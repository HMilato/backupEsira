/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.hibernate;

//import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.PooledDataSource;
import com.mchange.v2.c3p0.impl.C3P0PooledConnectionPool;
import esira.domain.Users;
import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.web.context.request.SessionScope;
import org.zkoss.zk.ui.Sessions;

/**
 *
 * @author Ussimane
 */
public class MultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private static final long serialVersionUID = 6241633589847209550L;

    //private BasicDataSource defaultDataSource;
    private ComboPooledDataSource defaultDataSource;

    public MultiTenantConnectionProviderImpl() {
        PooledDataSource pds = C3P0Registry.pooledDataSourceByName("fecn1");
        if (pds == null) {
            System.out.println("Criou novo pool");
            defaultDataSource = new ComboPooledDataSource("fecn1");
            defaultDataSource.setJdbcUrl("jdbc:postgresql://127.0.0.1:5433/fecn1");
            defaultDataSource.setUser("fecn1");
            defaultDataSource.setPassword("esira");
            defaultDataSource.setAcquireIncrement(5);
            defaultDataSource.setMinPoolSize(10);
            defaultDataSource.setMaxPoolSize(1000);
            defaultDataSource.setMaxStatements(1000);
            defaultDataSource.setMaxIdleTime(1500);
            try {
                defaultDataSource.setDriverClass("org.postgresql.Driver");
            } catch (PropertyVetoException ex) {
                Logger.getLogger(MultiTenantConnectionProviderImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
           System.out.println("Nao criou novo pool");
        }

    }

    @Override
    protected DataSource selectAnyDataSource() {
        System.out.println("Criou novo pool; anny data source");
        return defaultDataSource;
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
//        Users usr = (Users) Sessions.getCurrent().getAttribute("user");
//        if(usr!=null)System.out.println(usr.getTenant());
        ComboPooledDataSource cpds = null;
        ComboPooledDataSource pds = (ComboPooledDataSource) C3P0Registry.pooledDataSourceByName(tenantIdentifier);
        if (pds == null) {
            System.out.println("Criou novo pool: "+tenantIdentifier);
            cpds = new ComboPooledDataSource(tenantIdentifier);
            cpds.setJdbcUrl("jdbc:postgresql://127.0.0.1/fecn1");
            //jdbc:postgresql://localhost:5432/mydatabase?currentSchema=myschema
            cpds.setUser(tenantIdentifier);
            cpds.setPassword("esira");
            cpds.setAcquireIncrement(5);
            cpds.setMinPoolSize(10);
            cpds.setMaxPoolSize(1000);
            cpds.setMaxStatements(1000);
            cpds.setMaxIdleTime(1500);
          //  cpds.setPreferredTestQuery("set search_path to "+""+tenantIdentifier+"");
            try {
                cpds.setDriverClass("org.postgresql.Driver");
            } catch (PropertyVetoException ex) {
                Logger.getLogger(MultiTenantConnectionProviderImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            return cpds;

        } else {
            
                System.out.println("Usou antigo pool: "+tenantIdentifier+pds.getPreferredTestQuery()+"");
           
        }
        return pds;

    }
}
