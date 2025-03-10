package com.example.agent;


import org.objectweb.asm.*;

public class MappingRegistryVisitor extends ClassVisitor {
    public MappingRegistryVisitor(ClassVisitor cv) {
        super(Opcodes.ASM9, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor,
                                     String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if ("register".equals(name) && "(Ljava/lang/Object;Lorg/springframework/web/servlet/handler/AbstractHandlerMethodMapping$MappingRegistration;)V".equals(descriptor)) {
            return new RegisterMethodVisitor(mv);
        }
        return mv;
    }

    static class RegisterMethodVisitor extends MethodVisitor {
        RegisterMethodVisitor(MethodVisitor mv) {
            super(Opcodes.ASM9, mv);
        }

        @Override
        public void visitCode() {
            mv.visitVarInsn(Opcodes.ALOAD, 2);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "org/springframework/web/servlet/handler/AbstractHandlerMethodMapping$MappingRegistration",
                    "getHandlerMethod",
                    "()Lorg/springframework/web/method/HandlerMethod;", false);
            mv.visitVarInsn(Opcodes.ALOAD, 2);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "org/springframework/web/servlet/handler/AbstractHandlerMethodMapping$MappingRegistration",
                    "getMappingInfo",
                    "()Lorg/springframework/web/servlet/mvc/condition/RequestMappingInfo;", false);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                    "com/example/agent/APICollector",
                    "collectAPI",
                    "(Lorg/springframework/web/method/HandlerMethod;Lorg/springframework/web/servlet/mvc/condition/RequestMappingInfo;)V", false);
            super.visitCode();
        }
    }
}
