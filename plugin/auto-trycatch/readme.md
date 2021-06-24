为所有方法自动加入 try catch 监控错误日志

```
apply plugin: 'com.miqt.plugin.autotrycatch'
AutoTryCatch {
    buildLog = true
    injectJar = false
    enable = true
    handleClass='com.xxx.Handler'
    handleMethod='print'
}
```