/*    */ package com.sun.jndi.ldap;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ import java.util.Arrays;
/*    */ import javax.naming.ldap.Control;
/*    */ 
/*    */ class SimpleClientId extends ClientId
/*    */ {
/*    */   private final String username;
/*    */   private final Object passwd;
/*    */   private final int myHash;
/*    */ 
/*    */   SimpleClientId(int paramInt1, String paramString1, int paramInt2, String paramString2, Control[] paramArrayOfControl, OutputStream paramOutputStream, String paramString3, String paramString4, Object paramObject)
/*    */   {
/* 48 */     super(paramInt1, paramString1, paramInt2, paramString2, paramArrayOfControl, paramOutputStream, paramString3);
/*    */ 
/* 51 */     this.username = paramString4;
/* 52 */     if (paramObject == null)
/* 53 */       this.passwd = null;
/* 54 */     else if ((paramObject instanceof String))
/* 55 */       this.passwd = paramObject;
/* 56 */     else if ((paramObject instanceof byte[]))
/* 57 */       this.passwd = ((byte[])((byte[])paramObject).clone());
/* 58 */     else if ((paramObject instanceof char[]))
/* 59 */       this.passwd = ((char[])((char[])paramObject).clone());
/*    */     else {
/* 61 */       this.passwd = paramObject;
/*    */     }
/*    */ 
/* 64 */     this.myHash = (super.hashCode() + (paramString4 != null ? paramString4.hashCode() : 0) + (paramObject != null ? paramObject.hashCode() : 0));
/*    */   }
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 70 */     if ((paramObject == null) || (!(paramObject instanceof SimpleClientId))) {
/* 71 */       return false;
/*    */     }
/*    */ 
/* 74 */     SimpleClientId localSimpleClientId = (SimpleClientId)paramObject;
/*    */ 
/* 76 */     return (super.equals(paramObject)) && ((this.username == localSimpleClientId.username) || ((this.username != null) && (this.username.equals(localSimpleClientId.username)))) && ((this.passwd == localSimpleClientId.passwd) || ((this.passwd != null) && (localSimpleClientId.passwd != null) && ((((this.passwd instanceof String)) && (this.passwd.equals(localSimpleClientId.passwd))) || (((this.passwd instanceof byte[])) && ((localSimpleClientId.passwd instanceof byte[])) && (Arrays.equals((byte[])this.passwd, (byte[])localSimpleClientId.passwd))) || (((this.passwd instanceof char[])) && ((localSimpleClientId.passwd instanceof char[])) && (Arrays.equals((char[])this.passwd, (char[])localSimpleClientId.passwd))))));
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 92 */     return this.myHash;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 96 */     return super.toString() + ":" + this.username;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.SimpleClientId
 * JD-Core Version:    0.6.2
 */