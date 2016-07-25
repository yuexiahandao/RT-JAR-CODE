/*    */ package javax.lang.model.type;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ 
/*    */ public class MirroredTypeException extends MirroredTypesException
/*    */ {
/*    */   private static final long serialVersionUID = 269L;
/*    */   private transient TypeMirror type;
/*    */ 
/*    */   public MirroredTypeException(TypeMirror paramTypeMirror)
/*    */   {
/* 57 */     super("Attempt to access Class object for TypeMirror " + paramTypeMirror.toString(), paramTypeMirror);
/* 58 */     this.type = paramTypeMirror;
/*    */   }
/*    */ 
/*    */   public TypeMirror getTypeMirror()
/*    */   {
/* 69 */     return this.type;
/*    */   }
/*    */ 
/*    */   private void readObject(ObjectInputStream paramObjectInputStream)
/*    */     throws IOException, ClassNotFoundException
/*    */   {
/* 77 */     paramObjectInputStream.defaultReadObject();
/* 78 */     this.type = null;
/* 79 */     this.types = null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.type.MirroredTypeException
 * JD-Core Version:    0.6.2
 */