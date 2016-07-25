/*    */ package com.sun.org.apache.xml.internal.security.keys;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*    */ import com.sun.org.apache.xml.internal.security.keys.content.KeyName;
/*    */ import com.sun.org.apache.xml.internal.security.keys.content.KeyValue;
/*    */ import com.sun.org.apache.xml.internal.security.keys.content.MgmtData;
/*    */ import com.sun.org.apache.xml.internal.security.keys.content.X509Data;
/*    */ import java.io.PrintStream;
/*    */ import java.security.PublicKey;
/*    */ 
/*    */ public class KeyUtils
/*    */ {
/*    */   public static void prinoutKeyInfo(KeyInfo paramKeyInfo, PrintStream paramPrintStream)
/*    */     throws XMLSecurityException
/*    */   {
/*    */     Object localObject;
/* 56 */     for (int i = 0; i < paramKeyInfo.lengthKeyName(); i++) {
/* 57 */       localObject = paramKeyInfo.itemKeyName(i);
/*    */ 
/* 59 */       paramPrintStream.println("KeyName(" + i + ")=\"" + ((KeyName)localObject).getKeyName() + "\"");
/*    */     }
/*    */ 
/* 62 */     for (i = 0; i < paramKeyInfo.lengthKeyValue(); i++) {
/* 63 */       localObject = paramKeyInfo.itemKeyValue(i);
/* 64 */       PublicKey localPublicKey = ((KeyValue)localObject).getPublicKey();
/*    */ 
/* 66 */       paramPrintStream.println("KeyValue Nr. " + i);
/* 67 */       paramPrintStream.println(localPublicKey);
/*    */     }
/*    */ 
/* 70 */     for (i = 0; i < paramKeyInfo.lengthMgmtData(); i++) {
/* 71 */       localObject = paramKeyInfo.itemMgmtData(i);
/*    */ 
/* 73 */       paramPrintStream.println("MgmtData(" + i + ")=\"" + ((MgmtData)localObject).getMgmtData() + "\"");
/*    */     }
/*    */ 
/* 76 */     for (i = 0; i < paramKeyInfo.lengthX509Data(); i++) {
/* 77 */       localObject = paramKeyInfo.itemX509Data(i);
/*    */ 
/* 79 */       paramPrintStream.println("X509Data(" + i + ")=\"" + (((X509Data)localObject).containsCertificate() ? "Certificate " : "") + (((X509Data)localObject).containsIssuerSerial() ? "IssuerSerial " : "") + "\"");
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.keys.KeyUtils
 * JD-Core Version:    0.6.2
 */