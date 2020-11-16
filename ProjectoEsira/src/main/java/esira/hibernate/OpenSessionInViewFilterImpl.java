/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.hibernate;

import esira.service.Utilizador;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.LogRecord;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptorAdapter;
import org.springframework.web.context.request.async.WebAsyncManager;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.zk.ui.Sessions;

/**
 *
 * @author Ussimane
 */
public class OpenSessionInViewFilterImpl extends OpenSessionInViewFilter implements java.util.logging.Filter {

    public static final String DEFAULT_SESSION_FACTORY_BEAN_NAME = "sessionFactory";

    private String sessionFactoryBeanName = DEFAULT_SESSION_FACTORY_BEAN_NAME;
    Utilizador usr;
    String t = TenantIdResolver.getTenant();//"fecn1";

    /**
     * Set the bean name of the SessionFactory to fetch from Spring's root
     * application context. Default is "sessionFactory".
     *
     * @see #DEFAULT_SESSION_FACTORY_BEAN_NAME
     */
    public void setSessionFactoryBeanName(String sessionFactoryBeanName) {
        this.sessionFactoryBeanName = sessionFactoryBeanName;
    }

    /**
     * Return the bean name of the SessionFactory to fetch from Spring's root
     * application context.
     */
    protected String getSessionFactoryBeanName() {
        return this.sessionFactoryBeanName;
    }

    /**
     * Returns "false" so that the filter may re-bind the opened Hibernate
     * {@code Session} to each asynchronously dispatched thread and postpone
     * closing it until the very last asynchronous dispatch.
     */
    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    /**
     * Returns "false" so that the filter may provide a Hibernate
     * {@code Session} to each error dispatches.
     */
    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        SessionFactory sessionFactory = lookupSessionFactory(request);
        boolean participate = false;

        WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
        String key = getAlreadyFilteredAttributeName();

        if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
            // Do not modify the Session: just set the participate flag.
            participate = true;
        } else {
            boolean isFirstRequest = !isAsyncDispatch(request);
            if (isFirstRequest || !applySessionBindingInterceptor(asyncManager, key)) {
                logger.debug("Opening Hibernate Session in OpenSessionInViewFilter");
                Session session = openSession(sessionFactory);
                SessionHolder sessionHolder = new SessionHolder(session);
                TransactionSynchronizationManager.bindResource(sessionFactory, sessionHolder);

                asyncManager.registerCallableInterceptor(key,
                        new SessionBindingCallableInterceptor(sessionFactory, sessionHolder));
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            if (!participate) {
                SessionHolder sessionHolder
                        = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
                if (!isAsyncStarted(request)) {
                    logger.debug("Closing Hibernate Session in OpenSessionInViewFilter");
                    SessionFactoryUtils.closeSession(sessionHolder.getSession());
                }
            }
        }
    }

    /**
     * Look up the SessionFactory that this filter should use, taking the
     * current HTTP request as argument.
     * <p>
     * The default implementation delegates to the
     * {@link #lookupSessionFactory()} variant without arguments.
     *
     * @param request the current request
     * @return the SessionFactory to use
     */
    protected SessionFactory lookupSessionFactory(HttpServletRequest request) {
        return lookupSessionFactory();
    }

    /**
     * Look up the SessionFactory that this filter should use.
     * <p>
     * The default implementation looks for a bean with the specified name in
     * Spring's root application context.
     *
     * @return the SessionFactory to use
     * @see #getSessionFactoryBeanName
     */
    protected SessionFactory lookupSessionFactory() {
        if (logger.isDebugEnabled()) {
            logger.debug("Using SessionFactory '" + getSessionFactoryBeanName() + "' for OpenSessionInViewFilter");
        }
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        return wac.getBean(getSessionFactoryBeanName(), SessionFactory.class);
    }

    /**
     * Open a Session for the SessionFactory that this filter uses.
     * <p>
     * The default implementation delegates to the
     * {@code SessionFactory.openSession} method and sets the {@code Session}'s
     * flush mode to "MANUAL".
     *
     * @param sessionFactory the SessionFactory that this filter uses
     * @return the Session to use
     * @throws DataAccessResourceFailureException if the Session could not be
     * created
     * @see org.hibernate.FlushMode#MANUAL
     */
    protected Session openSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
        try {
            if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null
                    && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Utilizador) {
                usr = (Utilizador) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (usr != null) {
                    //  System.out.println("utiliza o esquema de utilizador"+usr.getEsquema());
                    t = usr.getEsquema();
                }
            } else {
               if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null
                    && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                   System.out.println("exist Seccao");
               }else{
                System.out.println("nao ha Seccao");
               }
//                Object o = null;
//                org.zkoss.zk.ui.Session s = Sessions.getCurrent();
//                if (s != null) {
//                    o = s.getAttribute("tenant");
//                    if (o != null) {
//                        System.out.println("exist Seccao");
//                    }
//                } else {
//                    System.out.println("nao ha Seccao");
//
//                }
            }
            Session session = sessionFactory.withOptions().tenantIdentifier(t).openSession();
            session.setFlushMode(FlushMode.MANUAL);
            return session;
        } catch (HibernateException ex) {
            throw new DataAccessResourceFailureException("Could not open Hibernate Session", ex);
        }
    }

    private boolean applySessionBindingInterceptor(WebAsyncManager asyncManager, String key) {
        if (asyncManager.getCallableInterceptor(key) == null) {
            return false;
        }
        ((SessionBindingCallableInterceptor) asyncManager.getCallableInterceptor(key)).initializeThread();
        return true;
    }

    @Override
    public boolean isLoggable(LogRecord record) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Bind and unbind the Hibernate {@code Session} to the current thread.
     */
    private static class SessionBindingCallableInterceptor extends CallableProcessingInterceptorAdapter {

        private final SessionFactory sessionFactory;

        private final SessionHolder sessionHolder;

        public SessionBindingCallableInterceptor(SessionFactory sessionFactory, SessionHolder sessionHolder) {
            this.sessionFactory = sessionFactory;
            this.sessionHolder = sessionHolder;
        }

        @Override
        public <T> void preProcess(NativeWebRequest request, Callable<T> task) {
            initializeThread();
        }

        @Override
        public <T> void postProcess(NativeWebRequest request, Callable<T> task, Object concurrentResult) {
            TransactionSynchronizationManager.unbindResource(this.sessionFactory);
        }

        private void initializeThread() {
            TransactionSynchronizationManager.bindResource(this.sessionFactory, this.sessionHolder);
        }
    }
}
