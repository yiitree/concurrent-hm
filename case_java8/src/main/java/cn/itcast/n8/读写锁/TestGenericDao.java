package cn.itcast.n8.读写锁;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TestGenericDao {
    public static void main(String[] args) {
        GenericDao dao = new GenericDaoCached();
        System.out.println("============> 查询");
        String sql = "select * from emp where empno = ?";
        int empno = 7369;
        Emp emp = dao.queryOne(Emp.class, sql, empno);
        System.out.println(emp);
        emp = dao.queryOne(Emp.class, sql, empno);
        System.out.println(emp);
        emp = dao.queryOne(Emp.class, sql, empno);
        System.out.println(emp);

        System.out.println("============> 更新");
        dao.update("update emp set sal = ? where empno = ?", 800, empno);
        emp = dao.queryOne(Emp.class, sql, empno);
        System.out.println(emp);
    }
}

/**
 * Dao增强
 * 带缓存的Dao---增加缓存
 */
class GenericDaoCached extends GenericDao {

    private final GenericDao dao = new GenericDao();
    /**
     * map集合，作为缓存---HashMap是不安全的
     * key：sql语句+传参
     * value：查询结果
     */
    private final Map<SqlPair, Object> map = new HashMap<>();
    private final ReentrantReadWriteLock rw = new ReentrantReadWriteLock();

    @Override
    public <T> List<T> queryList(Class<T> beanClass, String sql, Object... args) {
        return dao.queryList(beanClass, sql, args);
    }

    /**
     * 查询一个，先从缓存中找，如果没有找到再查询数据库
     */
    @Override
    public <T> T queryOne(Class<T> beanClass, String sql, Object... args) {
        // 先从缓存中找，找到直接返回
        SqlPair key = new SqlPair(sql, args);
        // 读锁
        rw.readLock().lock();
        try {
            T value = (T) map.get(key);
            if(value != null) {
                return value;
            }
        } finally {
            rw.readLock().unlock();
        }
        // 缓存中没有，查询数据库
        // 写锁
        rw.writeLock().lock();
        try {
            // 多个线程，双重验证，此时一个已经添加到缓存了，就不用再改了
            T value = (T) map.get(key);
            if(value == null) {
                // 缓存中没有，查询数据库
                value = dao.queryOne(beanClass, sql, args);
                map.put(key, value);
            }
            return value;
        } finally {
            rw.writeLock().unlock();
        }
    }

    /**
     * 更新语句---要考虑到缓存的更新策略
     * @param sql
     * @param args
     * @return
     */
    @Override
    public int update(String sql, Object... args) {
        // 加写锁
        rw.writeLock().lock();
        try {
            // 先更新库
            int update = dao.update(sql, args);
            // 清空缓存
            map.clear();
            return update;
        } finally {
            rw.writeLock().unlock();
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
