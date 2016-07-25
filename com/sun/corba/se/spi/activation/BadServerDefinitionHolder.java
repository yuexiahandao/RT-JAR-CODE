/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class BadServerDefinitionHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public BadServerDefinition value = null;
/*    */ 
/*    */   public BadServerDefinitionHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public BadServerDefinitionHolder(BadServerDefinition paramBadServerDefinition)
/*    */   {
/* 20 */     this.value = paramBadServerDefinition;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = BadServerDefinitionHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     BadServerDefinitionHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return BadServerDefinitionHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.BadServerDefinitionHolder
 * JD-Core Version:    0.6.2
 */