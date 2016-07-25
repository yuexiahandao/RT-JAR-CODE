/*     */ package com.sun.jndi.ldap.sasl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.security.auth.callback.Callback;
/*     */ import javax.security.auth.callback.CallbackHandler;
/*     */ import javax.security.auth.callback.NameCallback;
/*     */ import javax.security.auth.callback.PasswordCallback;
/*     */ import javax.security.auth.callback.UnsupportedCallbackException;
/*     */ import javax.security.sasl.RealmCallback;
/*     */ import javax.security.sasl.RealmChoiceCallback;
/*     */ 
/*     */ final class DefaultCallbackHandler
/*     */   implements CallbackHandler
/*     */ {
/*     */   private char[] passwd;
/*     */   private String authenticationID;
/*     */   private String authRealm;
/*     */ 
/*     */   DefaultCallbackHandler(String paramString1, Object paramObject, String paramString2)
/*     */     throws IOException
/*     */   {
/*  55 */     this.authenticationID = paramString1;
/*  56 */     this.authRealm = paramString2;
/*  57 */     if ((paramObject instanceof String)) {
/*  58 */       this.passwd = ((String)paramObject).toCharArray();
/*  59 */     } else if ((paramObject instanceof char[])) {
/*  60 */       this.passwd = ((char[])((char[])paramObject).clone());
/*  61 */     } else if (paramObject != null)
/*     */     {
/*  63 */       String str = new String((byte[])paramObject, "UTF8");
/*  64 */       this.passwd = str.toCharArray();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handle(Callback[] paramArrayOfCallback) throws IOException, UnsupportedCallbackException
/*     */   {
/*  70 */     for (int i = 0; i < paramArrayOfCallback.length; i++)
/*  71 */       if ((paramArrayOfCallback[i] instanceof NameCallback)) {
/*  72 */         ((NameCallback)paramArrayOfCallback[i]).setName(this.authenticationID);
/*     */       }
/*  74 */       else if ((paramArrayOfCallback[i] instanceof PasswordCallback)) {
/*  75 */         ((PasswordCallback)paramArrayOfCallback[i]).setPassword(this.passwd);
/*     */       }
/*     */       else
/*     */       {
/*     */         Object localObject;
/*  77 */         if ((paramArrayOfCallback[i] instanceof RealmChoiceCallback))
/*     */         {
/*  79 */           localObject = ((RealmChoiceCallback)paramArrayOfCallback[i]).getChoices();
/*     */ 
/*  81 */           int j = 0;
/*     */ 
/*  83 */           if ((this.authRealm != null) && (this.authRealm.length() > 0)) {
/*  84 */             j = -1;
/*  85 */             for (int k = 0; k < localObject.length; k++) {
/*  86 */               if (localObject[k].equals(this.authRealm)) {
/*  87 */                 j = k;
/*     */               }
/*     */             }
/*  90 */             if (j == -1) {
/*  91 */               StringBuffer localStringBuffer = new StringBuffer();
/*  92 */               for (int m = 0; m < localObject.length; m++) {
/*  93 */                 localStringBuffer.append(localObject[m] + ",");
/*     */               }
/*  95 */               throw new IOException("Cannot match 'java.naming.security.sasl.realm' property value, '" + this.authRealm + "' with choices " + localStringBuffer + "in RealmChoiceCallback");
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 102 */           ((RealmChoiceCallback)paramArrayOfCallback[i]).setSelectedIndex(j);
/*     */         }
/* 104 */         else if ((paramArrayOfCallback[i] instanceof RealmCallback))
/*     */         {
/* 106 */           localObject = (RealmCallback)paramArrayOfCallback[i];
/* 107 */           if (this.authRealm != null) {
/* 108 */             ((RealmCallback)localObject).setText(this.authRealm);
/*     */           } else {
/* 110 */             String str = ((RealmCallback)localObject).getDefaultText();
/* 111 */             if (str != null)
/* 112 */               ((RealmCallback)localObject).setText(str);
/*     */             else
/* 114 */               ((RealmCallback)localObject).setText("");
/*     */           }
/*     */         }
/*     */         else {
/* 118 */           throw new UnsupportedCallbackException(paramArrayOfCallback[i]);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   void clearPassword() {
/* 124 */     if (this.passwd != null) {
/* 125 */       for (int i = 0; i < this.passwd.length; i++) {
/* 126 */         this.passwd[i] = '\000';
/*     */       }
/* 128 */       this.passwd = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void finalize() throws Throwable {
/* 133 */     clearPassword();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.sasl.DefaultCallbackHandler
 * JD-Core Version:    0.6.2
 */