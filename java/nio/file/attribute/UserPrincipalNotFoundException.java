/*    */ package java.nio.file.attribute;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class UserPrincipalNotFoundException extends IOException
/*    */ {
/*    */   static final long serialVersionUID = -5369283889045833024L;
/*    */   private final String name;
/*    */ 
/*    */   public UserPrincipalNotFoundException(String paramString)
/*    */   {
/* 52 */     this.name = paramString;
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 62 */     return this.name;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.attribute.UserPrincipalNotFoundException
 * JD-Core Version:    0.6.2
 */