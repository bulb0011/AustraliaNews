# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep class com.ruanyun.australianews.data.** { *; }
-keep class com.ruanyun.australianews.base.** { *; }
-keep class com.ruanyun.australianews.di.** { *; }
-keep class com.ruanyun.australianews.model.** { *; }
-keep class com.ruanyun.australianews.util.** { *; }
-keep class com.ruanyun.australianews.imagelist.** { *; }
-keep class com.ruanyun.australianews.widget.** { *; }



-dontshrink
-dontpreverify
-dontoptimize
-dontusemixedcaseclassnames
-flattenpackagehierarchy
-allowaccessmodification
-printmapping map.txt

-optimizationpasses 7
-verbose
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-ignorewarnings
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.content.Context
-keep public class * extends java.lang.Throwable {*;}
-keep public class * extends java.lang.Exception {*;}

-keepclassmembers class * implements java.io.Serializable { #保护实现接口Serializable的类中，指定规则的类成员不被混淆
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep class android.**{*;}
-keep class android.support.v4.** {*;}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-dontwarn android.os.**
-keep public class android.os.**{*;}

-keepclassmembers class **.R$* {
    public static <fields>;
}
#tencent.bugly
-dontwarn com.tencent.**
-keep public class com.tencent.**{*;}
-keep class internal.org.**{ *;}
-keep class org.apache.**{ *;}
-keep class android.net.**{ *;}
-keep class com.android.**{ *;}
#高德 map
-keep class com.amap.** { *; }
-keep class com.autonavi.** { *; }
-keep class com.maploc.** { *; }
-dontwarn com.amap.**
-dontwarn com.autonavi.**
-dontwarn com.maploc.**

#百度 map
-keep class com.baidu.** {*;}
-keep class mapsdkvi.com.** {*;}
-dontwarn com.baidu.**

#极光推送
-keep class cn.jpush.** { *; }
-dontwarn cn.jpush.**

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

 -keepclassmembers class ** {
     public void onEvent*(**);
 }

-keep class jiguang.chat.** { *; }
-dontwarn  jiguang.chat.**

-keep class com.ta.utdid2.** { *; }
-dontwarn  com.ta.utdid2.**
-keep class com.ut.device.** { *; }
-dontwarn  com.ut.device.**
-keep class org.json.alipay.** { *; }
-dontwarn  org.json.alipay.**
-keep class com.alipay.** { *; }
-dontwarn  com.alipay.**

#桌面图标数量
-keep class me.leolin.shortcutbadger.** { *; }
-dontwarn me.leolin.shortcutbadger.**
#网易云信
-keep class com.nostra13.** { *; }
-keep class com.alibaba.** { *; }
-keep class com.netease.** { *; }
-dontwarn com.nostra13.**
-dontwarn com.alibaba.**
-dontwarn com.netease.**

-keep class vi.com.gdi.bgl.android.**{*;}
##butterknife
-keep class butterknife.internal.** { *;}
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
##green dao
-keep class org.greenrobot.** { *;}
-keep class org.greenrobot.greendao.async** { *;}
-keep class org.greenrobot.greendao.database** { *;}
-keep class org.greenrobot.greendao.identityscope** { *;}
-keep class org.greenrobot.greendao.internal** { *;}
-keep class org.greenrobot.greendao.query** { *;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
     public static java.lang.String TABLENAME;
 }
-keep class **$Properties
-dontwarn org.greenrobot.greendao.**

-keep class cn.addapp.pickers.** { *; }


-keep class com.google.gson.** {*;}
#-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
-keep class com.google.** {
    <fields>;
    <methods>;
}
-dontwarn com.google.gson.**

## ----------------------------------
##      sharesdk
## ----------------------------------
-keep class com.mob.**{*;}
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
-dontwarn **.R$*

#retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
#-keepattributes Signature
#-keepattributes Exceptions

#eventbus
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

## ----------------------------------
##      七牛
## ----------------------------------
-dontwarn com.qiniu.**
-keep class  com.qiniu.** { *; }



## ----------------------------------
##      okhttp
## ----------------------------------


-dontwarn okhttp3.**
-keep class  okhttp3.** { *; }


## ----------------------------------
##      okio
## ----------------------------------

-dontwarn okio.**
-keep class  okio.** { *; }


## ----------------------------------
##      glide
## ----------------------------------

-dontwarn com.bumptech.glide.**
-keep class  com.bumptech.glide.** { *; }

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}


## ----------------------------------
##     autolayout
## ----------------------------------
-keep class com.zhy.autolayout.** { *; }
-dontwarn  com.zhy.autolayout.**
## ----------------------------------

## ----------------------------------
##     oginotihiro
## ----------------------------------
-keep class com.oginotihiro.** { *; }
-dontwarn  com.zhy.autolayout.**
## ----------------------------------

## ----------------------------------
##     v7 v4
## ----------------------------------
-keep class android.support.** { *; }
-keep class android.support.annotation.**
-keep class android.support.v7.** { *; }
-dontwarn  android.support.v7.**
##convenientbanner-----------
-keep class com.bigkoo.convenientbanner.** { *; }
-dontwarn com.bigkoo.convenientbanner.**

##bga refresh layout

-keep class cn.bingoogolapple.refreshlayout.** { *; }
-dontwarn cn.bingoogolapple.refreshlayout.**

-keep class com.nineoldandroids.** { *; }
-dontwarn com.nineoldandroids.**

#imagepicker
-keep class com.ruanyun.imagepicker.** { *; }
-dontwarn com.ruanyun.imagepicker.**

#javax
-keep class javax.** { *; }
-dontwarn javax.**

#rxjava
-keep class rx.** { *; }
-dontwarn rx.**

# adding this in to preserve line numbers so that the stack traces
# can be remapped
-renamesourcefileattribute SourceFile
#-keepattributes SourceFile,LineNumberTable

#aliyun push
-keepclasseswithmembernames class ** {
    native <methods>;
}
#-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-keep class org.json.alipay.** {*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-dontwarn org.json.alipay.**
-keep class com.ut.** {*;}
-dontwarn com.ut.**
-keep class com.ta.** {*;}
-dontwarn com.ta.**
-keep class anet.**{*;}
-keep class org.android.spdy.**{*;}
-keep class org.android.agoo.**{*;}
-dontwarn anet.**
-dontwarn org.android.spdy.**
-dontwarn org.android.agoo.**



-keep class com.shuyu.gsyvideoplayer.video.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.**
-keep class com.shuyu.gsyvideoplayer.video.base.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.base.**
-keep class com.shuyu.gsyvideoplayer.utils.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.utils.**
-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}















#极光
-dontoptimize
-dontpreverify
-keepattributes  EnclosingMethod,Signature
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

 -keepclassmembers class ** {
     public void onEvent*(**);
 }


-keep class jiguang.chat.** { *; }
-dontwarn  jiguang.chat.**

-keep class com.sj.emoji.** { *; }
-dontwarn  com.sj.emoji.**

-keep class uk.co.senab.photoview.** { *; }
-dontwarn  uk.co.senab.photoview.**

-keep class com.facebook.** { *; }
-dontwarn  com.facebook.**

-keep class sj.qqkeyboard.** { *; }
-dontwarn  sj.qqkeyboard.**

-keep class com.bigkoo.pickerview.** { *; }
-dontwarn  com.bigkoo.pickerview.**

-keep class com.squareup.picasso.** { *; }
-dontwarn  com.squareup.picasso.**

-keep class com.nostra13.universalimageloader.** { *; }
-dontwarn  com.nostra13.universalimageloader.**


-keep class com.activeandroid.** { *; }
-dontwarn  com.activeandroid.**

#========================gson================================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

#========================protobuf================================
-keep class com.google.protobuf.** {*;}

#========================support=================================
-dontwarn cn.jmessage.support.**
-keep class cn.jmessage.support.**{*;}


-keep class com.iflytek.**{*;}
-keepattributes Signature
