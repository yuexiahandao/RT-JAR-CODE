/*    */ package javax.tools;
/*    */ 
/*    */ import javax.lang.model.element.Modifier;
/*    */ import javax.lang.model.element.NestingKind;
/*    */ 
/*    */ public class ForwardingJavaFileObject<F extends JavaFileObject> extends ForwardingFileObject<F>
/*    */   implements JavaFileObject
/*    */ {
/*    */   protected ForwardingJavaFileObject(F paramF)
/*    */   {
/* 50 */     super(paramF);
/*    */   }
/*    */ 
/*    */   public JavaFileObject.Kind getKind() {
/* 54 */     return ((JavaFileObject)this.fileObject).getKind();
/*    */   }
/*    */ 
/*    */   public boolean isNameCompatible(String paramString, JavaFileObject.Kind paramKind) {
/* 58 */     return ((JavaFileObject)this.fileObject).isNameCompatible(paramString, paramKind);
/*    */   }
/*    */   public NestingKind getNestingKind() {
/* 61 */     return ((JavaFileObject)this.fileObject).getNestingKind();
/*    */   }
/* 63 */   public Modifier getAccessLevel() { return ((JavaFileObject)this.fileObject).getAccessLevel(); }
/*    */ 
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.tools.ForwardingJavaFileObject
 * JD-Core Version:    0.6.2
 */