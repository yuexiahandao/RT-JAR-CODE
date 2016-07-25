/*    */ package org.omg.PortableServer.ServantLocatorPackage;
/*    */ 
/*    */ import org.omg.CORBA.NO_IMPLEMENT;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class CookieHolder
/*    */   implements Streamable
/*    */ {
/*    */   public Object value;
/*    */ 
/*    */   public CookieHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public CookieHolder(Object paramObject)
/*    */   {
/* 43 */     this.value = paramObject;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream) {
/* 47 */     throw new NO_IMPLEMENT();
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream) {
/* 51 */     throw new NO_IMPLEMENT();
/*    */   }
/*    */ 
/*    */   public TypeCode _type() {
/* 55 */     throw new NO_IMPLEMENT();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.ServantLocatorPackage.CookieHolder
 * JD-Core Version:    0.6.2
 */