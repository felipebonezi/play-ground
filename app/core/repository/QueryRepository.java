package core.repository;

import core.repository.dto.Pagination;
import io.ebean.Expression;
import io.ebean.Finder;
import java.util.List;
import java.util.Optional;
import play.libs.F;

/**
 * Repository with basic query methods to access a database entity using <a
 * href="https://github.com/playframework/play-ebean">Play-Ebean</a>.
 *
 * @param <I> Primary key.
 * @param <T> Entity.
 */
public interface QueryRepository<I, T> {
  
  Finder<I, T> getFinder();
  
  Optional<T> findById(I id);
  
  Optional<T> find(Expression expression);
  
  Optional<T> find(Expression expression, String orderBy);
  
  Optional<T> find(String fields, Expression expression);
  
  Optional<T> find(String fields, Expression expression, String orderBy);
  
  List<T> findAll();
  
  List<T> findAll(Expression expression);
  
  List<T> findAll(Expression expression, Pagination pagination);
  
  List<T> findAll(String fields, Expression expression);
  
  List<T> findAll(String fields, Expression expression, Pagination pagination);
  
  F.Tuple<List<T>, Integer> findAndCountAll(String fields, Expression expression);
  
  F.Tuple<List<T>, Integer> findAndCountAll(String fields, Expression expression,
                                            Pagination pagination);
  
  F.Tuple<List<T>, F.Tuple<Integer, Integer>> findAndCountersAll(String fields,
                                                                 Expression expression,
                                                                 Pagination pagination);
  
  void deleteAll(Expression expression);
  
  int count(Expression expression);
  
  boolean exists(Expression expression);
  
}
