package com.ruanyun.australianews.util

import com.flyco.tablayout.listener.OnTabSelectListener
import rx.Subscriber
import rx.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * description: tabselect 去除连续点击数据不正常
 * <p/>
 * Created by ycw on 2018/12/18.
 */
abstract class OnTabDebounceSelectListener : OnTabSelectListener {

    val TAG = "OnTabSelectDebounceListener"
//    constructor(timeOut:Long) :this()

    override fun onTabSelect(position: Int) {
        publishSubject.onNext(position)
    }

    override fun onTabReselect(position: Int) {
    }

    private val publishSubject: PublishSubject<Int> = PublishSubject.create()

    init {
        publishSubject.debounce(100, TimeUnit.MILLISECONDS)
                .compose(RxUtil.normalSchedulers())
                .subscribe(object : Subscriber<Int>() {
                    override fun onNext(position: Int) {
                        LogX.d(TAG, "onNext() called with: position = [$position]")
                        onTabDebounceSelect(position)
                    }

                    override fun onCompleted() {
                        LogX.d(TAG, "onCompleted() called")
                    }

                    override fun onError(e: Throwable?) {
                        LogX.d(TAG, "onError() called with: e = [$e]")
                    }
                })
    }

    /**
     * 去除抖动tab选择
     * @param position Int
     */
    abstract fun onTabDebounceSelect(position: Int)

}