package esira.dao;

import esira.hibernate.Login;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.hibernate.FlushMode;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;
import org.springframework.stereotype.Repository;

@Repository
public class CRUDDaoImpl implements CRUDDao {

    @Autowired
    SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public <T> List<T> getAll(Class<T> klass) {
        return getCurrentSession().createQuery("from " + klass.getName())
                .list();
    }

    @SuppressWarnings("unchecked")
    public <T> int count(Class<T> klass) {
        Long l = (Long) getCurrentSession().createQuery("select count(c) from " + klass.getName() + " c")
                .uniqueResult();
        return l.intValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Long countJPQuery(String hql, Map<String, Object> namedParams) {
        Query query = getCurrentSession().createQuery(hql);
        if (namedParams != null) {
            Entry mapEntry;
            for (Iterator it = namedParams.entrySet().iterator(); it
                    .hasNext(); query.setParameter(
                            (String) mapEntry.getKey(), mapEntry.getValue())) {
                mapEntry = (Entry) it.next();
            }
        }
        Long l = (Long) query.uniqueResult();
        return l;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Float sumJPQuery(String hql, Map<String, Object> namedParams) {
        Query query = getCurrentSession().createQuery(hql);
        if (namedParams != null) {
            Entry mapEntry;
            for (Iterator it = namedParams.entrySet().iterator(); it
                    .hasNext(); query.setParameter(
                            (String) mapEntry.getKey(), mapEntry.getValue())) {
                mapEntry = (Entry) it.next();
            }
        }
        Object o = query.uniqueResult();
        Float l = null;
        if (o != null) {
            l = ((Double) o).floatValue();
        }
        return l;
    }

    protected final Session getCurrentSession() {

//        Login.setTenantId("fecn2");
//        SessionBuilder sb=  sessionFactory.withOptions().tenantIdentifier("fecn1");
//        return  sb.openSession();//.getCurrentSession();
        return sessionFactory.getCurrentSession();
    }

    protected final Session getSession(String tenant) {
        SessionBuilder sb = sessionFactory.withOptions().tenantIdentifier(tenant);
        return sb.openSession();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getAllQuery(String s) {
        return getCurrentSession().createQuery(s)
                .list();
    }

    public <T> void update(T klass) {
        getCurrentSession().merge(klass);
    }

    public <T> void updates(T klass) {
        getCurrentSession().merge(klass);
        getCurrentSession().flush();
    }

    @Override
    public <T> boolean exist(T klass) {
        return getCurrentSession().contains(klass);
    }

    public <T> LockMode bloqueado(T klass) {
        return getCurrentSession().getCurrentLockMode(klass);
    }

    public <T> T load(Class<T> klass, Serializable id) {
        return (T) getCurrentSession().load(klass.getName(), id);
    }

    public <T> T get(Class<T> klass, Serializable id) {
        return (T) getCurrentSession().get(klass.getName(), id);
    }

    public <T> T loadLocked(Class<T> klass, Serializable id) {
        LockOptions lo = new LockOptions(LockMode.PESSIMISTIC_WRITE);

        Object o = getCurrentSession().load(klass.getName(), id, lo);
        getCurrentSession().flush();
        return (T) o;
    }

    public <T> T getLocked(Class<T> klass, Serializable id) {
        LockOptions lo = new LockOptions(LockMode.PESSIMISTIC_WRITE);
        Object o = getCurrentSession().get(klass.getName(), id, lo);
        getCurrentSession().flush();
        return (T) o;
    }

    @Override
    public <T> void refresh(T klass) {
        getCurrentSession().refresh(klass);
    }

    public <T> void Save(T klass) {
        getCurrentSession().save(klass);
    }

    public <T> void Saves(T klass) {
        //  getCurrentSession().setFlushMode(FlushMode.NEVER);
        getCurrentSession().save(klass);
        getCurrentSession().flush();
    }

    public <T> void delete(T klass) {
        getCurrentSession().delete(klass);
    }

    public <T> void deletes(T klass) {
        getCurrentSession().delete(klass);
        getCurrentSession().flush();
    }

    public <T> void lock(T klass) {
        LockOptions lo = new LockOptions(LockMode.PESSIMISTIC_WRITE);
        getCurrentSession().buildLockRequest(lo).lock(klass);
    }

    @SuppressWarnings("unchecked")
    public <T> T GetUniqueEntityByNamedQuery(String query, Object... params) {
        Query q = getCurrentSession().getNamedQuery(query);
        int i = 0;

        for (Object o : params) {
            q.setParameter(i, o);
            i++;//new
        }

        List<T> results = q.list();

        T foundentity = null;
        if (!results.isEmpty()) {
            // ignores multiple results
            foundentity = results.get(0);
        }
        return foundentity;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> GetAllEntityByNamedQuery(String query, Object... params) {
        Query q = getCurrentSession().getNamedQuery(query);
        int i = 0;

        for (Object o : params) {
            q.setParameter(i, o);
            i++;//new
        }

        List<T> results = q.list();

        return results;
    }

    @SuppressWarnings("unchecked")
    public <T> int updateQuery(String query, Object... params) {
        SQLQuery q = getCurrentSession().createSQLQuery(query);
        int i = 0;
        for (Object o : params) {
            q.setParameter(i, o);
            i++;//new
        }
        int r = q.executeUpdate();
        return r;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findByQuery(String hql, Map<String, Object> entidade, Map<String, Object> namedParams) {
        SQLQuery query = getCurrentSession().createSQLQuery(hql);
        if (entidade != null) {
            Entry mapEntry;
            for (Iterator it = entidade.entrySet().iterator(); it
                    .hasNext(); query.addEntity(
                            (String) mapEntry.getKey(), (Class) mapEntry.getValue())) {
                mapEntry = (Entry) it.next();
            }
        }
        if (namedParams != null) {
            Entry mapEntry;
            for (Iterator it = namedParams.entrySet().iterator(); it
                    .hasNext(); query.setParameter(
                            (String) mapEntry.getKey(), mapEntry.getValue())) {
                mapEntry = (Entry) it.next();
            }
        }
        List<T> returnList = query.list();

        return returnList;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findByQueryFilter(String hql, Map<String, Object> entidade, Map<String, Object> namedParams, int f, int m) {
        SQLQuery query = getCurrentSession().createSQLQuery(hql);
        if (entidade != null) {
            Entry mapEntry;
            for (Iterator it = entidade.entrySet().iterator(); it
                    .hasNext(); query.addEntity(
                            (String) mapEntry.getKey(), (Class) mapEntry.getValue())) {
                mapEntry = (Entry) it.next();
            }
        }
        if (namedParams != null) {
            Entry mapEntry;
            for (Iterator it = namedParams.entrySet().iterator(); it
                    .hasNext(); query.setParameter(
                            (String) mapEntry.getKey(), mapEntry.getValue())) {
                mapEntry = (Entry) it.next();
            }
        }
        query.setFirstResult(f);
        query.setMaxResults(m);
        List<T> returnList = query.list();

        return returnList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findByJPQueryFilter(String hql, Map<String, Object> namedParams, int f, int m) {
        Query query = getCurrentSession().createQuery(hql);
        if (namedParams != null) {
            Entry mapEntry;
            for (Iterator it = namedParams.entrySet().iterator(); it
                    .hasNext(); query.setParameter(
                            (String) mapEntry.getKey(), mapEntry.getValue())) {
                mapEntry = (Entry) it.next();
            }
        }
        if (query != null) {
            query.setFirstResult(f);
            query.setMaxResults(m);
        }
        List<T> returnList = query.list();

        return returnList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findByJPQuery(String hql, Map<String, Object> namedParams) {
        Query query = getCurrentSession().createQuery(hql);
        if (namedParams != null) {
            Entry mapEntry;
            for (Iterator it = namedParams.entrySet().iterator(); it
                    .hasNext(); query.setParameter(
                            (String) mapEntry.getKey(), mapEntry.getValue())) {
                mapEntry = (Entry) it.next();
            }
        }
        List<T> returnList = query.list();

        return returnList;
    }

    @SuppressWarnings("unchecked")
    public <T> T findEntByJPQueryLock(String hql, Map<String, Object> namedParams) {
        LockOptions lo = new LockOptions(LockMode.PESSIMISTIC_WRITE);
        Query query = getCurrentSession().createQuery(hql);
        query.setLockOptions(lo);
        if (namedParams != null) {
            Entry mapEntry;
            for (Iterator it = namedParams.entrySet().iterator(); it
                    .hasNext(); query.setParameter(
                            (String) mapEntry.getKey(), mapEntry.getValue())) {
                mapEntry = (Entry) it.next();
            }
        }
        List<T> results = query.list();

        T foundentity = null;
        if (!results.isEmpty()) {
            // ignores multiple results
            foundentity = results.get(0);
        }
        return foundentity;
    }

    @SuppressWarnings("unchecked")
    public <T> T findEntByJPQuery(String hql, Map<String, Object> namedParams) {
        Query query = getCurrentSession().createQuery(hql);
        if (namedParams != null) {
            Entry mapEntry;
            for (Iterator it = namedParams.entrySet().iterator(); it
                    .hasNext(); query.setParameter(
                            (String) mapEntry.getKey(), mapEntry.getValue())) {
                mapEntry = (Entry) it.next();
            }
        }
        List<T> results = query.list();

        T foundentity = null;
        if (!results.isEmpty()) {
            // ignores multiple results
            foundentity = results.get(0);
        }
        return foundentity;
    }

    @SuppressWarnings("unchecked")
    public <T> T findEntByJPQueryT(String hql, Map<String, Object> namedParams) {
        Query query = getCurrentSession().createQuery(hql);
        if (namedParams != null) {
            Entry mapEntry;
            for (Iterator it = namedParams.entrySet().iterator(); it
                    .hasNext(); query.setParameter(
                            (String) mapEntry.getKey(), mapEntry.getValue())) {
                mapEntry = (Entry) it.next();
            }
        }
        List<T> results = query.list();

        T foundentity = null;
        if (!results.isEmpty()) {
            // ignores multiple results
            foundentity = results.get(0);
        }
        return foundentity;
    }

    @Override
    public <T> Transaction getTransacao() {

        return getCurrentSession().getTransaction();
    }

    ////////////////////////////////////////tenant param///////////////////////////////////////////
    @SuppressWarnings("unchecked")
    public <T> int updateQuerySes(String query, String tenant) {
        Session s = getSession(tenant);
        s.beginTransaction();
        try {
            SQLQuery q = s.createSQLQuery(query);
            int i = 0;
//            for (Object o : params) {
//                q.setParameter(i, o);
//                i++;//new
//            }
            int r = q.executeUpdate();
            s.getTransaction().commit();
            s.close();
            return r;
        } finally {
            if (s.isOpen()) {
                s.close();
            }
        }
    }

    public <T> void refreshSes(T klass, String tenant) {
        Session s = getSession(tenant);
        try {
            s.refresh(klass);
        } finally {
            s.close();
        }

    }

    public <T> void updateSes(T klass, String tenant) {
        Session s = getSession(tenant);
        s.beginTransaction();
        try {
            s.merge(klass);
            s.getTransaction().commit();
        } finally {
            s.close();
        }
    }

    public <T> void SaveSes(T klass, String tenant) {
        //  getCurrentSession().setFlushMode(FlushMode.NEVER);
        Session s = getSession(tenant);
        s.beginTransaction();
        try {
            s.save(klass);
            s.getTransaction().commit();
        } finally {
            s.close();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T findEntByJPQuerySes(String hql, Map<String, Object> namedParams, String tenant) {
        Session s = getSession(tenant);
        try {
            Query query = s.createQuery(hql);
            if (namedParams != null) {
                Entry mapEntry;
                for (Iterator it = namedParams.entrySet().iterator(); it
                        .hasNext(); query.setParameter(
                                (String) mapEntry.getKey(), mapEntry.getValue())) {
                    mapEntry = (Entry) it.next();
                }
            }
            List<T> results = query.list();

            T foundentity = null;
            if (!results.isEmpty()) {
                // ignores multiple results
                foundentity = results.get(0);
            }
            return foundentity;
        } finally {
            s.close();
        }
    }

    public <T> T getSes(Class<T> klass, Serializable id, String tenant) {
        Session s = getSession(tenant);
        try {
            return (T) s.get(klass.getName(), id);
        } finally {
            s.close();
        }
    }
}
