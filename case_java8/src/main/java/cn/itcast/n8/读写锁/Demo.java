package cn.itcast.n8.读写锁;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写案例：Reen自带案例
 * @Author: 曾睿
 * @Date: 2021/4/19 10:38
 */
public class Demo {
}


class CachedData{

    /**
     * 缓存数据
     */
    private Object data;
    private volatile boolean cacheValid;

    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();

    public void getCache(){
        // 读取数据
        readLock.lock();
        try{
            // 数据失效，重新写入
            if(!cacheValid){
                // 释放读锁（已获得读锁，无法继续获得写锁）
                readLock.unlock();
                // 获得写锁
                writeLock.lock();
                try{
                    // 双重验证
                    if(!cacheValid){
                        data = getNewData();
                        cacheValid = true;
                    }
                    // 已获得写锁，继续获得读锁
                    readLock.lock();
                }finally {
                    // 释放写锁
                    writeLock.unlock();
                }
            }
            // 数据有效，读取数据
            useData(data);
            data = 1;
        }finally {
            // 释放写锁
            readLock.unlock();
        }
    }

    public void useData(Object o) {
    }

    public Object getNewData(){
        return new Object();
    }

}
