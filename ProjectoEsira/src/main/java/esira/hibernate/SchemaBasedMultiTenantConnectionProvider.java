package esira.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;



import org.hibernate.c3p0.internal.C3P0ConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;


import org.hibernate.HibernateException;

//import org.hibernate.service.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hibernate.service.spi.Stoppable;

public class SchemaBasedMultiTenantConnectionProvider implements MultiTenantConnectionProvider, Stoppable,
		Configurable, ServiceRegistryAwareService {

	private final C3P0ConnectionProvider connectionProvider = new C3P0ConnectionProvider();

	@Override
	public Connection getAnyConnection() throws SQLException {
		return connectionProvider.getConnection();
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		connectionProvider.closeConnection(connection);
	}

	@Override
	public Connection getConnection(String tenantIdentifier) throws SQLException {
		final Connection connection = getAnyConnection();
		try {
			connection.createStatement().execute("USE " + tenantIdentifier);
		} catch (SQLException e) {
			throw new HibernateException("Could not alter JDBC connection to specified schema [" + tenantIdentifier
					+ "]", e);
		}
		return connection;
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
		try {
			connection.createStatement().execute("USE fecn1");
		} catch (SQLException e) {
			throw new HibernateException("Could not alter JDBC connection to specified schema [" + tenantIdentifier
					+ "]", e);
		}
		connectionProvider.closeConnection(connection);
	}

	@Override
	public boolean isUnwrappableAs(Class unwrapType) {
		return this.connectionProvider.isUnwrappableAs(unwrapType);
	}

	@Override
	public <T> T unwrap(Class<T> unwrapType) {
		return this.connectionProvider.unwrap(unwrapType);
	}

	@Override
	public void stop() {
		this.connectionProvider.stop();
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return this.connectionProvider.supportsAggressiveRelease();
	}

	@Override
	public void configure(Map configurationValues) {
		this.connectionProvider.configure(configurationValues);

	}

	@Override
	public void injectServices(ServiceRegistryImplementor serviceRegistry) {
		this.connectionProvider.injectServices(serviceRegistry);

	}

}
