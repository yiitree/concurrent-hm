package cn.itcast.n8.读写锁;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: 曾睿
 * @Date: 2021/4/19 20:57
 */
public class MyDao<T> extends GenericDao {

    private final GenericDao dao = new GenericDao();
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.ReadLock r = readWriteLock.readLock();
    ReentrantReadWriteLock.WriteLock w = readWriteLock.writeLock();
    HashMap<SqlPair, Object> map = new HashMap();

    /**
     * 查询
     * @return
     */
    @Override
    public <T> Object queryOne(Class<T> beanClass, String sql, Object... args) {
//        r.lock();
//        try{
//            SqlPair sqlPair = new SqlPair(sql, args);
//            // 先查询缓存
//            final Object data = map.get(sqlPair);
//            if (data == null) {
//                // 缓存中没有，查询数据库
//                r.unlock();
//
//                T value = (T) dao.queryOne(beanClass, sql, args);
//
//                w.lock();
//                try{
//                    map.put(sqlPair, value);
//                }finally {
//                    w.unlock();
//                }
//            }
//            return data;
//        }finally {
//            r.unlock();
//        }

        SqlPair sqlPair = new SqlPair(sql, args);
        r.lock();
        try{
            // 先查询缓存
            final Object data = map.get(sqlPair);
            if (data != null) {
                return data;
            }
        }finally {
            r.unlock();
        }

        // 缓存中没有，查询数据库
        try{
            final Object data = map.get(sqlPair);
            if(data == null){
                T value = (T) dao.queryOne(beanClass, sql, args);
                w.lock();
                map.put(sqlPair, value);
                return value;
            }
            return data;
        }finally {
            w.unlock();
        }
    }

    /**
     * 更新
     */
    @Override
    public int update(String sql, Object... args) {
        w.lock();
        try{
            final int update = dao.update(sql, args);
            map.clear();
            return update;
        }finally {
            w.unlock();
        }
    }

    /**
     * sql语句+传参
     * 确保查询是唯一的
     */
    class SqlPair {
        private final String sql;
        private final Object[] args;

        public SqlPair(String sql, Object[] args) {
            this.sql = sql;
            this.args = args;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SqlPair sqlPair = (SqlPair) o;
            return Objects.equals(sql, sqlPair.sql) &&
                    Arrays.equals(args, sqlPair.args);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(sql);
            result = 31 * result + Arrays.hashCode(args);
            return result;
        }
    }

}
