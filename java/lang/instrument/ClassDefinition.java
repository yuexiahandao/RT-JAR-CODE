/*    */ package java.lang.instrument;
/*    */ 
/*    */ public final class ClassDefinition
/*    */ {
/*    */   private final Class mClass;
/*    */   private final byte[] mClassFile;
/*    */ 
/*    */   public ClassDefinition(Class<?> paramClass, byte[] paramArrayOfByte)
/*    */   {
/* 62 */     if ((paramClass == null) || (paramArrayOfByte == null)) {
/* 63 */       throw new NullPointerException();
/*    */     }
/* 65 */     this.mClass = paramClass;
/* 66 */     this.mClassFile = paramArrayOfByte;
/*    */   }
/*    */ 
/*    */   public Class<?> getDefinitionClass()
/*    */   {
/* 76 */     return this.mClass;
/*    */   }
/*    */ 
/*    */   public byte[] getDefinitionClassFile()
/*    */   {
/* 86 */     return this.mClassFile;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.instrument.ClassDefinition
 * JD-Core Version:    0.6.2
 */