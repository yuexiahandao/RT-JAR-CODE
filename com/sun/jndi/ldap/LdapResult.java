/*    */ package com.sun.jndi.ldap;
/*    */ 
/*    */ import java.util.Vector;
/*    */ import javax.naming.directory.BasicAttributes;
/*    */ 
/*    */ public final class LdapResult
/*    */ {
/*    */   int msgId;
/*    */   public int status;
/*    */   String matchedDN;
/*    */   String errorMessage;
/* 40 */   Vector referrals = null;
/* 41 */   LdapReferralException refEx = null;
/* 42 */   Vector entries = null;
/* 43 */   Vector resControls = null;
/* 44 */   public byte[] serverCreds = null;
/* 45 */   String extensionId = null;
/* 46 */   byte[] extensionValue = null;
/*    */ 
/*    */   boolean compareToSearchResult(String paramString)
/*    */   {
/* 55 */     boolean bool = false;
/*    */ 
/* 57 */     switch (this.status) {
/*    */     case 6:
/* 59 */       this.status = 0;
/* 60 */       this.entries = new Vector(1, 1);
/* 61 */       BasicAttributes localBasicAttributes = new BasicAttributes(true);
/* 62 */       LdapEntry localLdapEntry = new LdapEntry(paramString, localBasicAttributes);
/* 63 */       this.entries.addElement(localLdapEntry);
/* 64 */       bool = true;
/* 65 */       break;
/*    */     case 5:
/* 68 */       this.status = 0;
/* 69 */       this.entries = new Vector(0);
/* 70 */       bool = true;
/* 71 */       break;
/*    */     default:
/* 74 */       bool = false;
/*    */     }
/*    */ 
/* 78 */     return bool;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.LdapResult
 * JD-Core Version:    0.6.2
 */