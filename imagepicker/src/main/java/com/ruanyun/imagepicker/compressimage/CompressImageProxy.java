package com.ruanyun.imagepicker.compressimage;

import android.text.TextUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:
 * author: zhangsan on 16/11/28 下午2:00.
 */
public class CompressImageProxy  {
    //  CompressImageProxyService compressImageProxyService;
     /** 压缩比例 默认最高压缩 **/
    private int compressGear = CompressImageTask.THIRD_GEAR;
    private CompressImageResultAdapter compressImageResultAdapter;

    private final ExecutorService taskExecutor= Executors.newSingleThreadExecutor();
    public CompressImageProxyService getProxyService(final Class<CompressImageProxyService> service) {

        return (CompressImageProxyService) Proxy.newProxyInstance(service.getClassLoader()
                , new Class<?>[]{service},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        CompressImageTask task=buidTask(args);
                        return task;
                    }
                });
    }
    
    /**
      * 设置压缩图片结果转换适配器
      *@author zhangsan
      *@date   16/11/30 上午10:12
      */
    public CompressImageProxy setResultConverter(CompressImageResultAdapter converter){
        this.compressImageResultAdapter=converter;
        return this;
    }
   /**
     * 设置压缩比率
     *@author zhangsan
     *@date   16/11/30 上午10:13
     */
    public CompressImageProxy setCompressGear(int compressGear) {
        this.compressGear = compressGear;
        return this;
    }
    /**
      * 如果传入参数设置glbalparamsname  将替换原来请求参数名
      *@author zhangsan
      *@date   16/11/30 下午3:22
      */
    private CompressImageInfoGetter checkGlobalParams(CompressImageInfoGetter imageInfoGetter, String value){
        if(!TextUtils.isEmpty(value)) {
            imageInfoGetter.setParamsName(value);
        }
        return  imageInfoGetter;
    }

    private CompressImageTask buidTask(Object[] args) {
        CompressImageTask task = new CompressImageTask(compressGear,taskExecutor,compressImageResultAdapter);
       CompressImageInfoGetter[] imageInfoGetters=null;
        if (args != null && args.length ==2) {
            if(args[1] instanceof List){
                List<CompressImageInfoGetter> compressImageInfoGetterList= (List<CompressImageInfoGetter>) args[1];
                String globalPatamsName = (String) args[0];
                imageInfoGetters = new CompressImageInfoGetter[compressImageInfoGetterList.size()];
                for (int i = 0, size = compressImageInfoGetterList.size(); i < size; i++) {
                    imageInfoGetters[i] = checkGlobalParams(compressImageInfoGetterList.get(i), globalPatamsName);
                }
            }else {
                CompressImageInfoGetter[] param = (CompressImageInfoGetter[]) args[1];
                String globalPatamsName = (String) args[0];
                imageInfoGetters = new CompressImageInfoGetter[param.length];
                for (int i = 0, size = param.length; i < size; i++) {
                    imageInfoGetters[i] = checkGlobalParams(param[i], globalPatamsName);
                }
            }
         /*   if (param instanceof ArrayList) {
                 ArrayList<CompressImageInfoGetter> params= (ArrayList<CompressImageInfoGetter>) param;

            } else if (param instanceof CompressImageInfoGetter) {
                CompressImageInfoGetter imageInfoGetter= (CompressImageInfoGetter) param;
                checkGlobalParams(imageInfoGetter,globalPatamsName);
                imageInfoGetters=new CompressImageInfoGetter[]{imageInfoGetter};

            }*/
            task.setParams(imageInfoGetters);
        }
        return task;
    }




}
