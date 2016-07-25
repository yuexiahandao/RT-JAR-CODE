/*    */ package org.omg.DynamicAny;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public final class NameValuePair
/*    */   implements IDLEntity
/*    */ {
/* 17 */   public String id = null;
/*    */ 
/* 22 */   public Any value = null;
/*    */ 
/*    */   public NameValuePair()
/*    */   {
/*    */   }
/*    */ 
/*    */   public NameValuePair(String paramString, Any paramAny)
/*    */   {
/* 30 */     this.id = paramString;
/* 31 */     this.value = paramAny;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny.NameValuePair
 * JD-Core Version:    0.6.2
 */