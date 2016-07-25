/*     */ package com.sun.security.sasl.gsskerb;
/*     */ 
/*     */ import com.sun.security.sasl.util.AbstractSaslImpl;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.security.sasl.SaslException;
/*     */ import org.ietf.jgss.GSSContext;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.MessageProp;
/*     */ import org.ietf.jgss.Oid;
/*     */ 
/*     */ abstract class GssKrb5Base extends AbstractSaslImpl
/*     */ {
/*     */   private static final String KRB5_OID_STR = "1.2.840.113554.1.2.2";
/*     */   protected static Oid KRB5_OID;
/*  41 */   protected static final byte[] EMPTY = new byte[0];
/*     */ 
/*  49 */   protected GSSContext secCtx = null;
/*     */   protected static final int JGSS_QOP = 0;
/*     */ 
/*     */   protected GssKrb5Base(Map paramMap, String paramString)
/*     */     throws SaslException
/*     */   {
/*  53 */     super(paramMap, paramString);
/*     */   }
/*     */ 
/*     */   public String getMechanismName()
/*     */   {
/*  62 */     return "GSSAPI";
/*     */   }
/*     */ 
/*     */   public byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws SaslException
/*     */   {
/*  67 */     if (!this.completed) {
/*  68 */       throw new IllegalStateException("GSSAPI authentication not completed");
/*     */     }
/*     */ 
/*  72 */     if (!this.integrity) {
/*  73 */       throw new IllegalStateException("No security layer negotiated");
/*     */     }
/*     */     try
/*     */     {
/*  77 */       MessageProp localMessageProp = new MessageProp(0, this.privacy);
/*  78 */       byte[] arrayOfByte = this.secCtx.unwrap(paramArrayOfByte, paramInt1, paramInt2, localMessageProp);
/*  79 */       if (logger.isLoggable(Level.FINEST)) {
/*  80 */         traceOutput(this.myClassName, "KRB501:Unwrap", "incoming: ", paramArrayOfByte, paramInt1, paramInt2);
/*     */ 
/*  82 */         traceOutput(this.myClassName, "KRB502:Unwrap", "unwrapped: ", arrayOfByte, 0, arrayOfByte.length);
/*     */       }
/*     */ 
/*  85 */       return arrayOfByte;
/*     */     } catch (GSSException localGSSException) {
/*  87 */       throw new SaslException("Problems unwrapping SASL buffer", localGSSException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws SaslException {
/*  92 */     if (!this.completed) {
/*  93 */       throw new IllegalStateException("GSSAPI authentication not completed");
/*     */     }
/*     */ 
/*  97 */     if (!this.integrity) {
/*  98 */       throw new IllegalStateException("No security layer negotiated");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 103 */       MessageProp localMessageProp = new MessageProp(0, this.privacy);
/* 104 */       byte[] arrayOfByte = this.secCtx.wrap(paramArrayOfByte, paramInt1, paramInt2, localMessageProp);
/* 105 */       if (logger.isLoggable(Level.FINEST)) {
/* 106 */         traceOutput(this.myClassName, "KRB503:Wrap", "outgoing: ", paramArrayOfByte, paramInt1, paramInt2);
/*     */ 
/* 108 */         traceOutput(this.myClassName, "KRB504:Wrap", "wrapped: ", arrayOfByte, 0, arrayOfByte.length);
/*     */       }
/*     */ 
/* 111 */       return arrayOfByte;
/*     */     }
/*     */     catch (GSSException localGSSException) {
/* 114 */       throw new SaslException("Problem performing GSS wrap", localGSSException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void dispose() throws SaslException {
/* 119 */     if (this.secCtx != null) {
/*     */       try {
/* 121 */         this.secCtx.dispose();
/*     */       } catch (GSSException localGSSException) {
/* 123 */         throw new SaslException("Problem disposing GSS context", localGSSException);
/*     */       }
/* 125 */       this.secCtx = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void finalize() throws Throwable {
/* 130 */     dispose();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  45 */       KRB5_OID = new Oid("1.2.840.113554.1.2.2");
/*     */     }
/*     */     catch (GSSException localGSSException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.sasl.gsskerb.GssKrb5Base
 * JD-Core Version:    0.6.2
 */