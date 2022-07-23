/*
 * Copyright 2022 Felipe Bonezi <https://about.me/felipebonezi>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package core.repository;

import com.google.common.base.Strings;
import core.repository.dto.Pagination;
import io.ebean.Ebean;
import io.ebean.Expression;
import io.ebean.Finder;
import io.ebean.Query;
import java.util.List;
import java.util.Optional;
import play.libs.F;

/**
 * Generic {@link QueryRepository} implementation.
 *
 * @param <I> Primary key.
 * @param <T> Entity.
 */
public abstract class QueryRepositoryImpl<I, T> implements QueryRepository<I, T> {
  
  private final Finder<I, T> finder;
  
  protected QueryRepositoryImpl(Finder<I, T> finder) {
    this.finder = finder;
  }
  
  @Override
  public Finder<I, T> getFinder() {
    return this.finder;
  }
  
  @Override
  public Optional<T> findById(I id) {
    return Optional.ofNullable(this.finder.byId(id));
  }
  
  @Override
  public Optional<T> find(Expression expression) {
    return find(null, expression);
  }
  
  @Override
  public Optional<T> find(Expression expression, String orderBy) {
    return find(null, expression, orderBy);
  }
  
  @Override
  public Optional<T> find(String fields, Expression expression) {
    return find(fields, expression, null);
  }
  
  @Override
  public Optional<T> find(String fields, Expression expression, String orderBy) {
    Query<T> query = this.finder.query();
    if (expression != null) {
      query = query.where(expression);
    }
    
    if (!Strings.isNullOrEmpty(fields)) {
      query.select(fields);
    }
    
    if (!Strings.isNullOrEmpty(orderBy)) {
      query.orderBy(orderBy);
    }
    
    return query.findOneOrEmpty();
  }
  
  @Override
  public List<T> findAll() {
    return this.finder.all();
  }
  
  @Override
  public List<T> findAll(Expression expression) {
    return findAll(expression, null);
  }
  
  @Override
  public List<T> findAll(Expression expression, Pagination pagination) {
    return findAll(null, expression, null);
  }
  
  @Override
  public List<T> findAll(String fields, Expression expression) {
    return findAll(fields, expression, null);
  }
  
  @Override
  public List<T> findAll(String fields, Expression expression, Pagination pagination) {
    Query<T> query = this.finder.query();
    if (expression != null) {
      query.where(expression);
    }
    
    if (!Strings.isNullOrEmpty(fields)) {
      query.select(fields);
    }
    
    queryPaginate(pagination, query);
    
    return query.findList();
  }
  
  @Override
  public F.Tuple<List<T>, Integer> findAndCountAll(String fields, Expression expression) {
    return findAndCountAll(fields, expression, null);
  }
  
  @Override
  public F.Tuple<List<T>, Integer> findAndCountAll(String fields, Expression expression,
                                                   Pagination pagination) {
    Query<T> query = this.finder.query();
    if (expression != null) {
      query.where(expression);
    }
    
    if (!Strings.isNullOrEmpty(fields)) {
      query.select(fields);
    }
    
    int count = query.findCount();
    queryPaginate(pagination, query);
    
    List<T> all = query.findList();
    
    return new F.Tuple<>(all, count);
  }
  
  @Override
  public F.Tuple<List<T>, F.Tuple<Integer, Integer>> findAndCountersAll(String fields,
                                                                        Expression expression,
                                                                        Pagination pagination) {
    Query<T> query = this.finder.query();
    if (expression != null) {
      query.where(expression);
    }
    
    if (!Strings.isNullOrEmpty(fields)) {
      query.select(fields);
    }
    
    int countAll = query.findCount();
    queryPaginate(pagination, query);
    
    List<T> all           = query.findList();
    int     countFiltered = query.findCount();
    
    return new F.Tuple<>(all, new F.Tuple<>(countAll, countFiltered));
  }
  
  private void queryPaginate(Pagination pagination, Query<T> query) {
    if (pagination != null) {
      int    offset  = pagination.getOffset();
      int    limit   = pagination.getLimit();
      String orderBy = pagination.getOrderBy();
      
      if (!Strings.isNullOrEmpty(orderBy)) {
        query.orderBy(orderBy);
      }
      
      if (limit > 0) {
        query.setFirstRow(offset * limit).setMaxRows(limit);
      }
    }
  }
  
  @Override
  public void deleteAll(Expression expression) {
    Ebean.deleteAll(findAll(expression));
  }
  
  @Override
  public int count(Expression expression) {
    return this.finder.query().where(expression).findCount();
  }
  
  @Override
  public boolean exists(Expression expression) {
    return this.finder.query().where(expression).findCount() > 0;
  }
  
}
