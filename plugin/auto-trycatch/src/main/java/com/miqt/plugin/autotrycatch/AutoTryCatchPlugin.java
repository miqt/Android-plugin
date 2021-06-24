package com.miqt.plugin.autotrycatch;

import com.miqt.asm.method_hook.BasePlugin;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.io.File;
import java.util.jar.JarEntry;

import static org.objectweb.asm.Opcodes.ASM5;

public class AutoTryCatchPlugin extends BasePlugin<AutoTryCatchExtension> {
    @Override
    public AutoTryCatchExtension initExtension() {
        return new AutoTryCatchExtension();
    }

    @Override
    public byte[] transform(byte[] classBytes, File file) {
        ClassReader cr = new ClassReader(classBytes);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        cr.accept(new ClassVisitor(ASM5, cw) {

            private String className = "null";

            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                super.visit(version, access, name, signature, superName, interfaces);
                className = name.replace("/", ".");
            }

            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                if (className.equals(getExtension().handleClass)) {
                    return mv;
                }
                return new AddTryCatchAdviceAdapter(ASM5, mv, access, name,
                        descriptor, getExtension().getHandleClass(),
                        getExtension().getHandleMethod());
            }
        }, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }

    @Override
    public byte[] transformJar(byte[] classBytes, File file, JarEntry entry) {
        return classBytes;
    }

    @Override
    public String getName() {
        return "AutoTryCatch-plugin";
    }
}
