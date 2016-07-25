/*    */ package java.lang.invoke;
/*    */ 
/*    */ class MethodHandleInfo
/*    */ {
/*    */   public static final int REF_NONE = 0;
/*    */   public static final int REF_getField = 1;
/*    */   public static final int REF_getStatic = 2;
/*    */   public static final int REF_putField = 3;
/*    */   public static final int REF_putStatic = 4;
/*    */   public static final int REF_invokeVirtual = 5;
/*    */   public static final int REF_invokeStatic = 6;
/*    */   public static final int REF_invokeSpecial = 7;
/*    */   public static final int REF_newInvokeSpecial = 8;
/*    */   public static final int REF_invokeInterface = 9;
/*    */   private final Class<?> declaringClass;
/*    */   private final String name;
/*    */   private final MethodType methodType;
/*    */   private final int referenceKind;
/*    */ 
/*    */   public MethodHandleInfo(MethodHandle paramMethodHandle)
/*    */     throws ReflectiveOperationException
/*    */   {
/* 49 */     MemberName localMemberName = paramMethodHandle.internalMemberName();
/* 50 */     this.declaringClass = localMemberName.getDeclaringClass();
/* 51 */     this.name = localMemberName.getName();
/* 52 */     this.methodType = localMemberName.getMethodType();
/* 53 */     this.referenceKind = localMemberName.getReferenceKind();
/*    */   }
/*    */ 
/*    */   public Class<?> getDeclaringClass() {
/* 57 */     return this.declaringClass;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 61 */     return this.name;
/*    */   }
/*    */ 
/*    */   public MethodType getMethodType() {
/* 65 */     return this.methodType;
/*    */   }
/*    */ 
/*    */   public int getReferenceKind() {
/* 69 */     return this.referenceKind;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.MethodHandleInfo
 * JD-Core Version:    0.6.2
 */