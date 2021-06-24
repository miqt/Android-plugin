package com.miqt.plugin.autotrycatch;

import com.miqt.asm.method_hook.Extension;

public class AutoTryCatchExtension extends Extension {
    public String handleClass;
    public String handleMethod;

    @Override
    public String getExtensionName() {
        return "AutoTryCatch";
    }


    public String getHandleClass() {
        return handleClass;
    }

    public String getHandleMethod() {
        return handleMethod;
    }
}
