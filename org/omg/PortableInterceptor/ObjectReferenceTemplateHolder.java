/*    */ package org.omg.PortableInterceptor;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class ObjectReferenceTemplateHolder
/*    */   implements Streamable
/*    */ {
/* 20 */   public ObjectReferenceTemplate value = null;
/*    */ 
/*    */   public ObjectReferenceTemplateHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ObjectReferenceTemplateHolder(ObjectReferenceTemplate paramObjectReferenceTemplate)
/*    */   {
/* 28 */     this.value = paramObjectReferenceTemplate;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 33 */     this.value = ObjectReferenceTemplateHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 38 */     ObjectReferenceTemplateHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 43 */     return ObjectReferenceTemplateHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableInterceptor.ObjectReferenceTemplateHolder
 * JD-Core Version:    0.6.2
 */