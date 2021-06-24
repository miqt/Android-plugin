package com.miqt.asm.method_hook;

public abstract class Extension {
   public boolean enable = true;
   public String runVariant = RunVariant.ALWAYS.name();
   //是否关注jar包进行字节码处理
   public boolean injectJar = false;
   //是否输出log日志
   public boolean buildLog = true;

   public abstract String getExtensionName();
}
