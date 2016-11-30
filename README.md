# Widget
Some custom controls, tools such as collection

[![](https://jitpack.io/v/LiXiaoGuo/Widget.svg)](https://jitpack.io/#LiXiaoGuo/Widget)

```java

    //使用前必须配置 -----start
    public static String HTTPURL = "http://www.baidu.com";//网络连接的地址
    public static String SP_NAME = "Extends";//默认的SharedPreferences的文件名
    public static int PLACEHOLDER = 0;//占位图
    public static int ERROR = 0;//错误图
    public static int COLOR_DEFAULT = Color.parseColor("#00000000");//BaseActivity默认的状态栏颜色
    //使用前必须配置 -----end

```

```
allprojects {
		   repositories {
			      ...
   			   maven { url "https://jitpack.io" }
		   }
	}
```
```
dependencies {
	        compile 'com.github.LiXiaoGuo:Widget:0.0.3'
	}
```
