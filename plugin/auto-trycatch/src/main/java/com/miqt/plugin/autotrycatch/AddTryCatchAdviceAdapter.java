package com.miqt.plugin.autotrycatch;


import com.miqt.asm.method_hook.Logger;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class AddTryCatchAdviceAdapter extends AdviceAdapter {

    private final boolean isStatic;
    private final boolean isInit;
    private Label L0 = new Label();
    private Label L1 = new Label();
    private Label L2 = new Label();
    private Label L3 = new Label();
    private final String exceptionHandleClass;
    private final String exceptionHandleMethod;
    private final String mathodName;
    private final Logger logger;


    protected AddTryCatchAdviceAdapter(int api, MethodVisitor mv, int access,
                                       String name, String desc,
                                       String exceptionHandleClass,
                                       String exceptionHandleMethod,
                                       Logger logger) {
        super(api, mv, access, name, desc);
        mathodName = name;
        this.logger = logger;
        exceptionHandleClass = exceptionHandleClass.replace(".", "/");
        this.exceptionHandleClass = exceptionHandleClass;
        this.exceptionHandleMethod = exceptionHandleMethod;
        isStatic = (access & Opcodes.ACC_STATIC) != 0;
        isInit = name.equals("<init>") || name.equals("<clinit>");
    }

    @Override
    protected void onMethodEnter() {

        super.onMethodEnter();
        mv.visitTryCatchBlock(L0, L1, L2, "java/lang/Throwable");
        mv.visitLabel(L0);
    }

    @Override
    protected void onMethodExit(int opcode) {

        if (opcode == RETURN) {
            visitLabel(L1);
            visitJumpInsn(GOTO, L3);
            visitLabel(L2);
            visitVarInsn(ASTORE, 1);
            invokeHandler();
            visitLabel(L3);
        } else if (opcode == IRETURN) {
            visitLabel(L1);
            mv.visitInsn(opcode);

            visitLabel(L2);
            visitVarInsn(ASTORE, 1);
            invokeHandler();
            visitLabel(L3);
            visitInsn(ICONST_0);
            mv.visitInsn(opcode);
        } else if (opcode == FRETURN) {
            visitLabel(L1);
            mv.visitInsn(opcode);

            visitLabel(L2);
            visitVarInsn(ASTORE, 1);
            invokeHandler();
            visitLabel(L3);
            visitInsn(FCONST_0);
            mv.visitInsn(opcode);
        } else if (opcode == LRETURN) {
            visitLabel(L1);
            mv.visitInsn(opcode);

            visitLabel(L2);
            visitVarInsn(ASTORE, 1);
            invokeHandler();
            visitLabel(L3);
            visitInsn(LCONST_0);
            mv.visitInsn(opcode);
        } else if (opcode == DRETURN) {
            visitLabel(L1);
            mv.visitInsn(opcode);

            visitLabel(L2);
            visitVarInsn(ASTORE, 1);
            invokeHandler();
            visitLabel(L3);
            visitInsn(DCONST_0);
            mv.visitInsn(opcode);
        } else if (opcode == ARETURN) {
            visitLabel(L1);
            mv.visitInsn(opcode);

            visitLabel(L2);
            visitVarInsn(ASTORE, 1);
            invokeHandler();
            visitLabel(L3);
            visitInsn(ACONST_NULL);
            mv.visitInsn(opcode);
        }
        mv.visitInsn(opcode);
        super.onMethodExit(opcode);
    }


    private void invokeHandler() {
        if (exceptionHandleClass != null && exceptionHandleMethod != null) {
            mv.visitVarInsn(ALOAD, isInit ? 0 : 1);
            mv.visitMethodInsn(INVOKESTATIC, exceptionHandleClass,
                    exceptionHandleMethod, "(Ljava/lang/Throwable;)V", false);
        }
    }
}