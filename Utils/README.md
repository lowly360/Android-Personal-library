## 工具类

#### 

1.ShareUtils:用于SharedPreferences存取等相关操作

> 初始化: 在Application 的onCreatex下调用ShareUtils.initUtils(this); 

 ```
// 存
ShareUtils.getInstance().setValue("key",key_value);
// 取 String 为对应取值的包装类,默认值查看源码
String key = ShareUtils.getInstance().getValue("key","String");
 ```


2.ToastUtils: 全局吐司工具类

> 初始化: 在Application 的onCreatex下调用ToastUtil.initUtils(this); 

```
ToastUtil.showToast(String str);
ToastUtil.showToast(@StringRes int textId);
```



3.LogUtils: Log后台输出工具类(支持写入手机根目录)

> 初始化: 
>
> ```
> LogUtils.LOG2FILE_ENABLE = false; // 是否记录在本地
> LogUtils.onCreate(fileName); // 出入记录文件名称 
> ```