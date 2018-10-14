package core.database;

import com.google.common.base.Strings;
import io.ebean.Ebean;
import io.ebean.Expression;
import io.ebean.Finder;
import io.ebean.Query;
import play.libs.F;

import java.util.List;

public abstract class ADatabase<I, T> implements IDatabase<I, T> {

    private final Finder<I, T> mFinder;

    public ADatabase(Finder<I, T> finder) {
        this.mFinder = finder;
    }

    @Override
    public Finder<I, T> getFinder() {
        return this.mFinder;
    }

    @Override
    public T findById(I id) {
        return this.mFinder.byId(id);
    }

    @Override
    public T find(Expression expression) {
        return find(null, expression);
    }

    @Override
    public T find(Expression expression, String orderBy) {
        return find(null, expression, orderBy);
    }

    @Override
    public T find(String fields, Expression expression) {
        return find(fields, expression, null);
    }

    @Override
    public T find(String fields, Expression expression, String orderBy) {
        Query<T> query = this.mFinder.query();
        if (expression != null)
            query = query.where(expression);

        if (!Strings.isNullOrEmpty(fields))
            query.select(fields);

        if (!Strings.isNullOrEmpty(orderBy))
            query.orderBy(orderBy);

        return query.findOne();
    }

    @Override
    public T findOne(Expression expression) {
        return findOne(expression, null);
    }

    @Override
    public T findOne(Expression expression, String orderBy) {
        return findOne(null, expression, orderBy);
    }

    @Override
    public T findOne(String fields, Expression expression) {
        return findOne(fields, expression, null);
    }

    @Override
    public T findOne(String fields, Expression expression, String orderBy) {
        Query<T> query = this.mFinder.query();
        if (expression != null)
            query.where(expression);

        if (!Strings.isNullOrEmpty(orderBy))
            query.orderBy(orderBy);

        List<T> list = query.findList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<T> findAll() {
        return this.mFinder.all();
    }

    @Override
    public List<T> findAll(Expression expression) {
        return findAll(expression, null, 0, 0);
    }

    @Override
    public List<T> findAll(Expression expression, String orderBy) {
        return findAll(expression, orderBy, 0, 0);
    }

    @Override
    public List<T> findAll(Expression expression, int limit) {
        return findAll(expression, null, 0, limit);
    }

    @Override
    public List<T> findAll(Expression expression, String orderBy, int limit) {
        return findAll(expression, orderBy, 0, limit);
    }

    @Override
    public List<T> findAll(Expression expression, int offset, int limit) {
        return findAll(expression, null, offset, limit);
    }

    @Override
    public List<T> findAll(Expression expression, String orderBy, int offset, int limit) {
        return findAll(null, expression, orderBy, offset, limit);
    }

    @Override
    public List<T> findAll(String fields, Expression expression) {
        return findAll(fields, expression, null, 0, 0);
    }

    @Override
    public List<T> findAll(String fields, Expression expression, String orderBy) {
        return findAll(fields, expression, orderBy, 0, 0);
    }

    @Override
    public List<T> findAll(String fields, Expression expression, int limit) {
        return findAll(fields, expression, null, 0, limit);
    }

    @Override
    public List<T> findAll(String fields, Expression expression, String orderBy, int limit) {
        return findAll(fields, expression, orderBy, 0, limit);
    }

    @Override
    public List<T> findAll(String fields, Expression expression, int offset, int limit) {
        return findAll(fields, expression, null, offset, limit);
    }

    @Override
    public List<T> findAll(String fields, Expression expression, String orderBy, int offset, int limit) {
        Query<T> query = this.mFinder.query();
        if (expression != null)
            query.where(expression);

        if (!Strings.isNullOrEmpty(fields))
            query.select(fields);

        if (!Strings.isNullOrEmpty(orderBy))
            query.orderBy(orderBy);

        if (limit > 0) {
            if (offset > 0)
                query.setFirstRow(offset * limit);
            query.setMaxRows(limit);
        }

        return query.findList();
    }

    @Override
    public F.Tuple<List<T>, Integer> findAndCountAll(String fields, Expression expression) {
        return findAndCountAll(fields, expression, null, 0, 0);
    }

    @Override
    public F.Tuple<List<T>, Integer> findAndCountAll(String fields, Expression expression, int offset, int limit) {
        return findAndCountAll(fields, expression, null, offset, limit);
    }

    @Override
    public F.Tuple<List<T>, Integer> findAndCountAll(String fields, Expression expression, String orderBy, int offset, int limit) {
        Query<T> query = this.mFinder.query();
        if (expression != null)
            query.where(expression);

        int count = query.findCount();

        if (!Strings.isNullOrEmpty(fields))
            query.select(fields);

        if (limit > 0) {
            if (offset > 0)
                query.setFirstRow(offset * limit);
            query.setMaxRows(limit);
        }

        if (!Strings.isNullOrEmpty(orderBy))
            query.orderBy(orderBy);

        List<T> all = query.findList();

        return new F.Tuple<>(all, count);
    }

    @Override
    public F.Tuple<List<T>, F.Tuple<Integer, Integer>> findAndCountersAll(String fields, Expression expression, int offset, int limit) {
        return findAndCountersAll(fields, expression, null, offset, limit);
    }

    @Override
    public F.Tuple<List<T>, F.Tuple<Integer, Integer>> findAndCountersAll(String fields, Expression expression, String orderBy, int offset, int limit) {
        Query<T> query = this.mFinder.query();
        if (expression != null)
            query.where(expression);

        int countAll = query.findCount();

        if (!Strings.isNullOrEmpty(fields))
            query.select(fields);

        if (limit > 0) {
            if (offset > 0)
                query.setFirstRow(offset);
            query.setMaxRows(limit);
        }

        if (!Strings.isNullOrEmpty(orderBy))
            query.orderBy(orderBy);

        List<T> all = query.findList();
        int countFiltered = query.findCount();

        return new F.Tuple<>(all, new F.Tuple<>(countAll, countFiltered));
    }

    @Override
    public void deleteAll(Expression expression) {
        Ebean.deleteAll(findAll(expression));
    }

    @Override
    public int count(Expression expression) {
        return this.mFinder.query().where(expression).findCount();
    }

    @Override
    public boolean exists(Expression expression) {
        return this.mFinder.query().where(expression).findCount() > 0;
    }

}
