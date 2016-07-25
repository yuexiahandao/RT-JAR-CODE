/*    */ package javax.lang.model.type;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ public class MirroredTypesException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 269L;
/*    */   transient List<? extends TypeMirror> types;
/*    */ 
/*    */   MirroredTypesException(String paramString, TypeMirror paramTypeMirror)
/*    */   {
/* 58 */     super(paramString);
/* 59 */     ArrayList localArrayList = new ArrayList();
/* 60 */     localArrayList.add(paramTypeMirror);
/* 61 */     this.types = Collections.unmodifiableList(localArrayList);
/*    */   }
/*    */ 
/*    */   public MirroredTypesException(List<? extends TypeMirror> paramList)
/*    */   {
/* 70 */     super("Attempt to access Class objects for TypeMirrors " + (paramList = new ArrayList(paramList)).toString());
/*    */ 
/* 73 */     this.types = Collections.unmodifiableList(paramList);
/*    */   }
/*    */ 
/*    */   public List<? extends TypeMirror> getTypeMirrors()
/*    */   {
/* 84 */     return this.types;
/*    */   }
/*    */ 
/*    */   private void readObject(ObjectInputStream paramObjectInputStream)
/*    */     throws IOException, ClassNotFoundException
/*    */   {
/* 92 */     paramObjectInputStream.defaultReadObject();
/* 93 */     this.types = null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.type.MirroredTypesException
 * JD-Core Version:    0.6.2
 */