package com.miqt.plugin.autotrycatch;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class AddTryCatchAdviceAdapter extends AdviceAdapter {

    private final boolean isStatic;
    Label l1;
    Label l2;
    Label l0;
    private final String exceptionHandleClass;
    private final String exceptionHandleMethod;

    protected AddTryCatchAdviceAdapter(int api, MethodVisitor mv, int access, String name, String desc, String exceptionHandleClass, String exceptionHandleMethod) {
        super(api, mv, access, name, desc);
        exceptionHandleClass = exceptionHandleClass.replace(".", "/");
        this.exceptionHandleClass = exceptionHandleClass;
        this.exceptionHandleMethod = exceptionHandleMethod;
        isStatic = (access & Opcodes.ACC_STATIC) != 0;
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        l0 = new Label();
        l1 = new Label();
        l2 = new Label();
        mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Throwable");
        mv.visitLabel(l0);
    }

    @Override
    protected void onMethodExit(int i) {
        super.onMethodExit(i);
        mv.visitLabel(l1);
        Label l3 = new Label();
        mv.visitJumpInsn(GOTO, l3);
        mv.visitLabel(l2);
        mv.visitVarInsn(ASTORE, 1);
        if (exceptionHandleClass != null && exceptionHandleMethod != null) {
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, exceptionHandleClass,
                    exceptionHandleMethod, "(Ljava/lang/Throwable;)V", false);
        }
        mv.visitLabel(l3);
    }
}