为所有方法自动加入 try catch 监控错误日志


```
apply plugin: 'com.miqt.plugin.autotrycatch'
AutoTryCatch {
    buildLog = true
    //是否监控引用的依赖
    injectJar = false
    enable = true
    //异常处理，为空则不回调
    handleClass='com.xxx.Handler'
    handleMethod='print'
}
```

```
maven { url 'https://raw.fastgit.org/miqt/maven/master' }
```

```
classpath 'com.miqt:auto-trycatch:0.3.8'
```