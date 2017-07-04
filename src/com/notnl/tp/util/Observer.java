/*
 * Copyright (C) 2017 noti0na1 <i@notnl.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.notnl.tp.util;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author noti0na1
 * @param <T>
 */
public abstract class Observer<T> {

    private static final Logger LOG = Logger.getLogger(Observer.class.getName());

    /**
     * 计数器
     */
    private static int count = 0;

    private String name;
    /**
     * 事件列队的最大长度,默认为50
     */
    private int queueLength = 50;
    /**
     * 放弃之前等待的时间长度，默认为0
     */
    private long timeout = 0;
    /**
     * 简单事件列队
     */
    private BlockingQueue<ObservedEvent<T>> eventQueue;
    /**
     * 事件读取线程
     */
    private Thread eventHandler;

    /**
     *
     */
    public Observer() {
        this.name = "";
        //初始化事件列队
        eventQueue = new ArrayBlockingQueue<>(queueLength);
        initThread();
    }

    /**
     *
     * @param name
     */
    public Observer(String name) {
        this.name = name;
        //初始化事件列队
        eventQueue = new ArrayBlockingQueue<>(queueLength);
        initThread();
    }

    /**
     *
     * @param name
     * @param queueLength 事件列队长度
     * @param timeout 放弃之前等待的时间长度
     */
    public Observer(String name, int queueLength, long timeout) {
        this.name = name;
        this.queueLength = queueLength;
        this.timeout = timeout;
        //初始化事件列队
        eventQueue = new ArrayBlockingQueue<>(queueLength);
        initThread();
    }

    /**
     *
     */
    private void initThread() {
        String threadName = "Observer-"
                + (StringUtil.isEmpty(name) ? "" : name + "-")
                + count++;
        //创建线程
        eventHandler = new Thread(threadName) {
            @Override
            public void run() {
                try {
                    while (true) {
                        //读取事件，如列队为空，则阻塞线程
                        ObservedEvent<T> oe = eventQueue.take();
                        //处理事件
                        handle(oe.getSubject(), oe.getObject());
                    }
                } catch (InterruptedException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            }
        };
        LOG.log(Level.FINE, (threadName + " starts running."));
        //开始处理事件
        eventHandler.start();
    }

    /**
     *
     * @param event
     * @return
     */
    boolean update(ObservedEvent<T> event) {
        //往事件列队中放入事件包
        if (timeout == 0) {
            return this.eventQueue.offer(event);
        }
        try {
            return this.eventQueue.offer(event, timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            //失败则返回false
            LOG.log(Level.FINE, null, ex);
            return false;
        }
    }

    /**
     * 抽象事件处理
     *
     * @param subject
     * @param object
     */
    public abstract void handle(Observable<T> subject, T object);

    /**
     * 判断线程是否正在处理事件
     *
     * @return
     */
    public boolean isHandling() {
        return this.eventHandler.isAlive();
    }

    /**
     * 获得放弃之前等待的时间长度
     *
     * @return
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * 设置放弃之前等待的时间长度
     *
     * @param timeout
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * 获得列队尺寸
     *
     * @return
     */
    public int getQueueSize() {
        return eventQueue.size();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.queueLength;
        hash = 79 * hash + (int) (this.timeout ^ (this.timeout >>> 32));
        hash = 79 * hash + Objects.hashCode(this.eventQueue);
        hash = 79 * hash + Objects.hashCode(this.eventHandler);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Observer<?> other = (Observer<?>) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.queueLength != other.queueLength) {
            return false;
        }
        if (this.timeout != other.timeout) {
            return false;
        }
        if (!Objects.equals(this.eventQueue, other.eventQueue)) {
            return false;
        }
        return Objects.equals(this.eventHandler, other.eventHandler);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }

}

/**
 *
 * @author noti0na1
 */
class ObservedEvent<T> {

    private Observable subject;

    private T object;

    public ObservedEvent() {
    }

    public ObservedEvent(T object) {
        this.object = object;
    }

    public Observable getSubject() {
        return subject;
    }

    public void setSubject(Observable subject) {
        this.subject = subject;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.subject);
        hash = 67 * hash + Objects.hashCode(this.object);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ObservedEvent other = (ObservedEvent) obj;
        if (!Objects.equals(this.subject, other.subject)) {
            return false;
        }
        return Objects.equals(this.object, other.object);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
