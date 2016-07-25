/*     */ package com.sun.jndi.ldap.sasl;
/*     */ 
/*     */ import com.sun.jndi.ldap.Connection;
/*     */ import com.sun.jndi.ldap.LdapClient;
/*     */ import com.sun.jndi.ldap.LdapResult;
/*     */ import java.io.IOException;
/*     */ import java.util.Hashtable;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import javax.naming.AuthenticationException;
/*     */ import javax.naming.AuthenticationNotSupportedException;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.ldap.Control;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.sasl.Sasl;
/*     */ import javax.security.sasl.SaslClient;
/*     */ import javax.security.sasl.SaslException;
/*     */ 
/*     */ public final class LdapSasl
/*     */ {
/*     */   private static final String SASL_CALLBACK = "java.naming.security.sasl.callback";
/*     */   private static final String SASL_AUTHZ_ID = "java.naming.security.sasl.authorizationId";
/*     */   private static final String SASL_REALM = "java.naming.security.sasl.realm";
/*     */   private static final int LDAP_SUCCESS = 0;
/*     */   private static final int LDAP_SASL_BIND_IN_PROGRESS = 14;
/* 199 */   private static final byte[] NO_BYTES = new byte[0];
/*     */ 
/*     */   public static LdapResult saslBind(LdapClient paramLdapClient, Connection paramConnection, String paramString1, String paramString2, Object paramObject, String paramString3, Hashtable paramHashtable, Control[] paramArrayOfControl)
/*     */     throws IOException, NamingException
/*     */   {
/*  97 */     SaslClient localSaslClient = null;
/*  98 */     int i = 0;
/*     */ 
/* 101 */     DefaultCallbackHandler localDefaultCallbackHandler = paramHashtable != null ? (CallbackHandler)paramHashtable.get("java.naming.security.sasl.callback") : null;
/*     */ 
/* 103 */     if (localDefaultCallbackHandler == null) {
/* 104 */       localDefaultCallbackHandler = new DefaultCallbackHandler(paramString2, paramObject, (String)paramHashtable.get("java.naming.security.sasl.realm"));
/* 105 */       i = 1;
/*     */     }
/*     */ 
/* 109 */     String str = paramHashtable != null ? (String)paramHashtable.get("java.naming.security.sasl.authorizationId") : null;
/* 110 */     String[] arrayOfString = getSaslMechanismNames(paramString3);
/*     */     try
/*     */     {
/* 114 */       localSaslClient = Sasl.createSaslClient(arrayOfString, str, "ldap", paramString1, paramHashtable, localDefaultCallbackHandler);
/*     */ 
/* 117 */       if (localSaslClient == null) {
/* 118 */         throw new AuthenticationNotSupportedException(paramString3);
/*     */       }
/*     */ 
/* 122 */       localObject1 = localSaslClient.getMechanismName();
/* 123 */       byte[] arrayOfByte = localSaslClient.hasInitialResponse() ? localSaslClient.evaluateChallenge(NO_BYTES) : null;
/*     */ 
/* 126 */       LdapResult localLdapResult = paramLdapClient.ldapBind(null, arrayOfByte, paramArrayOfControl, (String)localObject1, true);
/*     */ 
/* 128 */       while ((!localSaslClient.isComplete()) && ((localLdapResult.status == 14) || (localLdapResult.status == 0)))
/*     */       {
/* 132 */         arrayOfByte = localSaslClient.evaluateChallenge(localLdapResult.serverCreds != null ? localLdapResult.serverCreds : NO_BYTES);
/*     */ 
/* 134 */         if (localLdapResult.status == 0) {
/* 135 */           if (arrayOfByte == null) break;
/* 136 */           throw new AuthenticationException("SASL client generated response after success");
/*     */         }
/*     */ 
/* 141 */         localLdapResult = paramLdapClient.ldapBind(null, arrayOfByte, paramArrayOfControl, (String)localObject1, true);
/*     */       }
/*     */       Object localObject2;
/* 144 */       if (localLdapResult.status == 0) {
/* 145 */         if (!localSaslClient.isComplete()) {
/* 146 */           throw new AuthenticationException("SASL authentication not complete despite server claims");
/*     */         }
/*     */ 
/* 150 */         localObject2 = (String)localSaslClient.getNegotiatedProperty("javax.security.sasl.qop");
/*     */ 
/* 153 */         if ((localObject2 != null) && ((((String)localObject2).equalsIgnoreCase("auth-int")) || (((String)localObject2).equalsIgnoreCase("auth-conf"))))
/*     */         {
/* 156 */           SaslInputStream localSaslInputStream = new SaslInputStream(localSaslClient, paramConnection.inStream);
/*     */ 
/* 158 */           SaslOutputStream localSaslOutputStream = new SaslOutputStream(localSaslClient, paramConnection.outStream);
/*     */ 
/* 161 */           paramConnection.replaceStreams(localSaslInputStream, localSaslOutputStream);
/*     */         } else {
/* 163 */           localSaslClient.dispose();
/*     */         }
/*     */       }
/* 166 */       return localLdapResult;
/*     */     } catch (SaslException localSaslException) {
/* 168 */       Object localObject1 = new AuthenticationException(paramString3);
/*     */ 
/* 170 */       ((NamingException)localObject1).setRootCause(localSaslException);
/* 171 */       throw ((Throwable)localObject1);
/*     */     } finally {
/* 173 */       if (i != 0)
/* 174 */         ((DefaultCallbackHandler)localDefaultCallbackHandler).clearPassword();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String[] getSaslMechanismNames(String paramString)
/*     */   {
/* 187 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString);
/* 188 */     Vector localVector = new Vector(10);
/* 189 */     while (localStringTokenizer.hasMoreTokens()) {
/* 190 */       localVector.addElement(localStringTokenizer.nextToken());
/*     */     }
/* 192 */     String[] arrayOfString = new String[localVector.size()];
/* 193 */     for (int i = 0; i < localVector.size(); i++) {
/* 194 */       arrayOfString[i] = ((String)localVector.elementAt(i));
/*     */     }
/* 196 */     return arrayOfString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.sasl.LdapSasl
 * JD-Core Version:    0.6.2
 */