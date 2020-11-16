package esira.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.hibernate.LockMode;
import org.hibernate.Transaction;

public interface CRUDService {

    <T> List<T> getAll(Class<T> klass);

    <T> List<T> getAllQuery(String s);

    <T> void Save(T klass);

    <T> void Saves(T klass);

    <T> void delete(T klass);

    <T> void refresh(T klass);

    <T> void deletes(T klass);

    <T> void lock(T klass);

    <T> void update(T klass);

    <T> void updates(T klass);

    <T> int updateQuery(String query, Object... params);

    <T> int count(Class<T> klass);

    <T> Long countJPQuery(String hql, Map<String, Object> namedParams);

    <T> Float sumJPQuery(String hql, Map<String, Object> namedParams);

    <T> boolean exist(T klass);

    <T> LockMode bloqueado(T klass);

    <T> Transaction getTransacao();

    <T> T GetUniqueEntityByNamedQuery(String query, Object... params);

    <T> List<T> GetAllEntityByNamedQuery(String query, Object... params);

    <T> List<T> findByQuery(String hql, Map<String, Object> entidade, Map<String, Object> namedParams);

    <T> List<T> findByQueryFilter(String hql, Map<String, Object> entidade, Map<String, Object> namedParams, int f, int m);

    <T> List<T> findByJPQuery(String hql, Map<String, Object> namedParams);

    <T> T findEntByJPQuery(String hql, Map<String, Object> namedParams);

    <T> T findEntByJPQueryT(String hql, Map<String, Object> namedParams);

    <T> T findEntByJPQueryLock(String hql, Map<String, Object> namedParams);

    <T> List<T> findByJPQueryFilter(String hql, Map<String, Object> namedParams, int f, int m);

    <T> T load(Class<T> klass, Serializable id);

    <T> T get(Class<T> klass, Serializable id);

    <T> T loadLocked(Class<T> klass, Serializable id);

    <T> T getLocked(Class<T> klass, Serializable id);

    ////////////////////////////////////////tenant param///////////////////////////////////////////

    <T> T findEntByJPQuerySes(String hql, Map<String, Object> namedParams, String tenant);

    <T> T getSes(Class<T> klass, Serializable id, String tenant);

    <T> void updateSes(T klass, String tenant);

    <T> void SaveSes(T klass, String tenant);

    <T> void refreshSes(T klass, String tenant);

    <T> int updateQuerySes(String query, String tenant);
}
