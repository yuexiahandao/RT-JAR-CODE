/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public final class NameValuePair
/*    */   implements IDLEntity
/*    */ {
/*    */   public String id;
/*    */   public Any value;
/*    */ 
/*    */   public NameValuePair()
/*    */   {
/*    */   }
/*    */ 
/*    */   public NameValuePair(String paramString, Any paramAny)
/*    */   {
/* 60 */     this.id = paramString;
/* 61 */     this.value = paramAny;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.NameValuePair
 * JD-Core Version:    0.6.2
 */