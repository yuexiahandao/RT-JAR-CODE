/*    */ package javax.tools;
/*    */ 
/*    */ import javax.lang.model.element.Modifier;
/*    */ import javax.lang.model.element.NestingKind;
/*    */ 
/*    */ public abstract interface JavaFileObject extends FileObject
/*    */ {
/*    */   public abstract Kind getKind();
/*    */ 
/*    */   public abstract boolean isNameCompatible(String paramString, Kind paramKind);
/*    */ 
/*    */   public abstract NestingKind getNestingKind();
/*    */ 
/*    */   public abstract Modifier getAccessLevel();
/*    */ 
/*    */   public static enum Kind
/*    */   {
/* 62 */     SOURCE(".java"), 
/*    */ 
/* 68 */     CLASS(".class"), 
/*    */ 
/* 74 */     HTML(".html"), 
/*    */ 
/* 79 */     OTHER("");
/*    */ 
/*    */     public final String extension;
/*    */ 
/*    */     private Kind(String paramString)
/*    */     {
/* 87 */       paramString.getClass();
/* 88 */       this.extension = paramString;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.tools.JavaFileObject
 * JD-Core Version:    0.6.2
 */