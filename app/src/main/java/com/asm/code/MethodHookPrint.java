package com.asm.code;

import android.os.SystemClock;
import android.util.Log;

import com.miqt.pluginlib.tools.IMethodHookHandler;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class MethodHookPrint implements IMethodHookHandler {
    private static final ThreadLocal<Queue<Long>> local = new ThreadLocal<>();

    @Override
    public void onMethodEnter(Object thisObj, String className, String methodName, String argsType, String returnType, Object... args) {

        Queue<Long> queue = local.get();
        if (queue == null) {
            queue = new LinkedBlockingDeque<>();
            local.set(queue);
        }
        String value = getSpace(queue.size()) +
                "┌" + (thisObj == null ? "static " + className : thisObj.toString()) +
                "." + methodName +
                "():";
        Log.i("MethodHookHandler", value);
        queue.offer(SystemClock.elapsedRealtime());
    }

    @Override
    public void onMethodReturn(Object returnObj, Object thisObj, String className, String methodName, String argsType, String returnType, Object... args) {

        Queue<Long> queue = local.get();
        assert queue != null;
        Long time = queue.poll();
        if (time != null) {
            long duc = SystemClock.elapsedRealtime() - time;
            String value = getSpace(queue.size()) +
                    "└" + (thisObj == null ? "static " + className : thisObj.toString()) +
                    "." + methodName +
                    "():" + duc;

            if (duc >= 1000) {
                Log.e("MethodHookHandler", value);
            } else if (duc >= 600) {
                Log.w("MethodHookHandler", value);
            } else if (duc >= 300) {
                Log.d("MethodHookHandler", value);
            } else {
                Log.i("MethodHookHandler", value);
            }
        }
    }


    public String getSpace(int size) {
        char[] value = new char[size];
        Arrays.fill(value, '┆');
        return new String(value);
    }
    public static void point(Throwable throwable) {
        throwable.printStackTrace();
    }


}
