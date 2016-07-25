/*    */ package org.omg.PortableInterceptor;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class ObjectReferenceTemplateSeqHolder
/*    */   implements Streamable
/*    */ {
/* 17 */   public ObjectReferenceTemplate[] value = null;
/*    */ 
/*    */   public ObjectReferenceTemplateSeqHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ObjectReferenceTemplateSeqHolder(ObjectReferenceTemplate[] paramArrayOfObjectReferenceTemplate)
/*    */   {
/* 25 */     this.value = paramArrayOfObjectReferenceTemplate;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 30 */     this.value = ObjectReferenceTemplateSeqHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 35 */     ObjectReferenceTemplateSeqHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 40 */     return ObjectReferenceTemplateSeqHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableInterceptor.ObjectReferenceTemplateSeqHolder
 * JD-Core Version:    0.6.2
 */