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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author noti0na1
 * @param <T>
 */
public class Observable<T> {

    /**
     * 事件观察者集合
     */
    private final List<Observer<T>> observers;
    /**
     * 通知重复次数，默认为2
     */
    private int repeatTime = 0b10;
    /**
     * 是否删除无效观察者，即通知重复次数以后仍失败的接收者
     */
    private boolean delFailures = false;

    /**
     *
     */
    public Observable() {
        //初始化观察者集合
        observers = new ArrayList<>();
    }

    /**
     * 增加观察者
     *
     * @param receiver
     */
    public synchronized void addObserver(Observer<T> receiver) {
        if (receiver == null) {
            throw new NullPointerException();
        }
        if (!observers.contains(receiver)) {
            observers.add(receiver);
        }
    }

    /**
     * 删除观察者
     *
     * @param receiver
     */
    public synchronized void deleteObserver(Observer<T> receiver) {
        observers.remove(receiver);
    }

    /**
     * 删除所有观察者
     */
    public synchronized void deleteObservers() {
        observers.clear();
    }

    /**
     * 获取观察者个数
     *
     * @return
     */
    public synchronized int countObservers() {
        return observers.size();
    }

    /**
     * 通知所有观察者
     *
     * @param arg
     */
    protected void notifyObservers(T arg) {
        ObservedEvent<T> event = new ObservedEvent<>(arg);
        event.setSubject(this);
        notifyObservers(observers, event, this.repeatTime);
    }

    /**
     * 通过指定观察者集合List通知
     *
     * @param ol
     * @param arg
     */
    protected void notifyObservers(List<Observer<T>> ol, T arg) {
        ObservedEvent event = new ObservedEvent(arg);
        event.setSubject(this);
        notifyObservers(ol, event, this.repeatTime);
    }

    /**
     * 通知所有观察者
     *
     * @param rl 观察者集合
     * @param event 事件事件
     * @param repeat 重复次数
     */
    private void notifyObservers(
            List<Observer<T>> ol, ObservedEvent<T> event, int repeat) {
        if (ol.isEmpty()) {
            return;
        }
        //判断重复次数是否为0，如为0，则做结束处理；如不为0，则重复次数递减
        if (repeat-- == 0) {
            //判断是否删除失效者
            if (delFailures) {
                //把所有失效者从观察者集合中移除
                this.observers.removeAll(ol);
            }
            return;
        }
        //定义失败者集合
        List<Observer<T>> fl = new ArrayList<>();
        for (Observer<T> mr : ol) {
            //判断此观察者是否通知成功
            if (!mr.update(event)) {
                //如未成功，则将次观察者加入失败者集合
                fl.add(mr);
            }
        }
        //迭代
        notifyObservers(fl, event, repeat);
    }

    /**
     * 获取重复次数
     *
     * @return
     */
    public int getRepeatTime() {
        return repeatTime;
    }

    /**
     * 设置重复次数
     *
     * @param repeatTime
     */
    public void setRepeatTime(int repeatTime) {
        this.repeatTime = repeatTime;
    }

    /**
     * 判断是否删除失败者
     *
     * @return
     */
    public boolean isDelFailures() {
        return delFailures;
    }

    /**
     * 设置是否删除失败者
     *
     * @param delFailures
     */
    public void setDelFailures(boolean delFailures) {
        this.delFailures = delFailures;
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }

}
