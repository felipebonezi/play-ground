package core.database;

import io.ebean.Expression;
import io.ebean.Finder;
import play.libs.F;

import java.util.List;

public interface IDatabase<I, T> {

    Finder<I, T> getFinder();

    T findById(I id);
    T find(Expression expression);
    T find(Expression expression, String orderBy);
    T find(String fields, Expression expression);
    T find(String fields, Expression expression, String orderBy);
    T findOne(Expression expression);
    T findOne(Expression expression, String orderBy);
    T findOne(String fields, Expression expression);
    T findOne(String fields, Expression expression, String orderBy);
    List<T> findAll();
    List<T> findAll(Expression expression);
    List<T> findAll(Expression expression, String orderBy);
    List<T> findAll(Expression expression, int limit);
    List<T> findAll(Expression expression, String orderBy, int limit);
    List<T> findAll(Expression expression, int offset, int limit);
    List<T> findAll(Expression expression, String orderBy, int offset, int limit);
    List<T> findAll(String fields, Expression expression);
    List<T> findAll(String fields, Expression expression, String orderBy);
    List<T> findAll(String fields, Expression expression, int limit);
    List<T> findAll(String fields, Expression expression, String orderBy, int limit);
    List<T> findAll(String fields, Expression expression, int offset, int limit);
    List<T> findAll(String fields, Expression expression, String orderBy, int offset, int limit);
    F.Tuple<List<T>, Integer> findAndCountAll(String fields, Expression expression);
    F.Tuple<List<T>, Integer> findAndCountAll(String fields, Expression expression, int offset, int limit);
    F.Tuple<List<T>, Integer> findAndCountAll(String fields, Expression expression, String orderBy, int offset, int limit);
    F.Tuple<List<T>, F.Tuple<Integer, Integer>> findAndCountersAll(String fields, Expression expression, int offset, int limit);
    F.Tuple<List<T>, F.Tuple<Integer, Integer>> findAndCountersAll(String fields, Expression expression, String orderBy, int offset, int limit);
    void deleteAll(Expression expression);

    int count(Expression expression);
    boolean exists(Expression expression);

}
