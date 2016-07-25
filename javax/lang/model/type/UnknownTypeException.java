/*    */ package javax.lang.model.type;
/*    */ 
/*    */ import javax.lang.model.UnknownEntityException;
/*    */ 
/*    */ public class UnknownTypeException extends UnknownEntityException
/*    */ {
/*    */   private static final long serialVersionUID = 269L;
/*    */   private transient TypeMirror type;
/*    */   private transient Object parameter;
/*    */ 
/*    */   public UnknownTypeException(TypeMirror paramTypeMirror, Object paramObject)
/*    */   {
/* 61 */     super("Unknown type: " + paramTypeMirror);
/* 62 */     this.type = paramTypeMirror;
/* 63 */     this.parameter = paramObject;
/*    */   }
/*    */ 
/*    */   public TypeMirror getUnknownType()
/*    */   {
/* 74 */     return this.type;
/*    */   }
/*    */ 
/*    */   public Object getArgument()
/*    */   {
/* 83 */     return this.parameter;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.type.UnknownTypeException
 * JD-Core Version:    0.6.2
 */