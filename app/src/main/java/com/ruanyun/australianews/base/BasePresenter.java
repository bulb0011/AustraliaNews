package com.ruanyun.australianews.base;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Description:
 * author: zhangsan on 17/1/3 下午3:18.
 */

public abstract class BasePresenter<T extends MvpView> implements Presenter<T>, InvocationHandler {
    protected String TAG = getClass().getSimpleName();
    protected T mvpView;

    //  protected Class<T> mvpClazz;
    // protected T emptyService;
    protected T getMvpView() {
        //   return checkNotNull(mvpView);
        //   return mvpView==null?(T) Proxy.newProxyInstance(mvpView.getClass().getClassLoader(), mvpView.getClass().getInterfaces(), this):mvpView;
        return mvpView;
    }

    @Override
    public void attachView(T mvpView) {
        this.mvpView = mvpView;
        //   mvpClazz= (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        //  emptyService=creatEmptyService();
    }

    @Override
    public void detachView() {
        // mvpView = null;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        method.invoke(mvpView, args);
        return null;
    }

    /* public T creatEmptyService(){
        return (T) Proxy.newProxyInstance(mvpClazz.getClassLoader(), new Class<?>[] { mvpClazz },
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        LogX.e("BasePresenter","service null");
                        return mvpClazz.newInstance();
                    }
                });
    }*/
}
