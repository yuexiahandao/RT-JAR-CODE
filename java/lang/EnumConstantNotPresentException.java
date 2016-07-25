/*    */ package java.lang;
/*    */ 
/*    */ public class EnumConstantNotPresentException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -6046998521960521108L;
/*    */   private Class<? extends Enum> enumType;
/*    */   private String constantName;
/*    */ 
/*    */   public EnumConstantNotPresentException(Class<? extends Enum> paramClass, String paramString)
/*    */   {
/* 61 */     super(paramClass.getName() + "." + paramString);
/* 62 */     this.enumType = paramClass;
/* 63 */     this.constantName = paramString;
/*    */   }
/*    */ 
/*    */   public Class<? extends Enum> enumType()
/*    */   {
/* 71 */     return this.enumType;
/*    */   }
/*    */ 
/*    */   public String constantName()
/*    */   {
/* 78 */     return this.constantName;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.EnumConstantNotPresentException
 * JD-Core Version:    0.6.2
 */