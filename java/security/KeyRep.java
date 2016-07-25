/*     */ package java.security;
/*     */ 
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.security.spec.PKCS8EncodedKeySpec;
/*     */ import java.security.spec.X509EncodedKeySpec;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ 
/*     */ public class KeyRep
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4757683898830641853L;
/*     */   private static final String PKCS8 = "PKCS#8";
/*     */   private static final String X509 = "X.509";
/*     */   private static final String RAW = "RAW";
/*     */   private Type type;
/*     */   private String algorithm;
/*     */   private String format;
/*     */   private byte[] encoded;
/*     */ 
/*     */   public KeyRep(Type paramType, String paramString1, String paramString2, byte[] paramArrayOfByte)
/*     */   {
/* 133 */     if ((paramType == null) || (paramString1 == null) || (paramString2 == null) || (paramArrayOfByte == null))
/*     */     {
/* 135 */       throw new NullPointerException("invalid null input(s)");
/*     */     }
/*     */ 
/* 138 */     this.type = paramType;
/* 139 */     this.algorithm = paramString1;
/* 140 */     this.format = paramString2.toUpperCase();
/* 141 */     this.encoded = ((byte[])paramArrayOfByte.clone());
/*     */   }
/*     */ 
/*     */   protected Object readResolve()
/*     */     throws ObjectStreamException
/*     */   {
/*     */     try
/*     */     {
/* 170 */       if ((this.type == Type.SECRET) && ("RAW".equals(this.format)))
/* 171 */         return new SecretKeySpec(this.encoded, this.algorithm);
/*     */       KeyFactory localKeyFactory;
/* 172 */       if ((this.type == Type.PUBLIC) && ("X.509".equals(this.format))) {
/* 173 */         localKeyFactory = KeyFactory.getInstance(this.algorithm);
/* 174 */         return localKeyFactory.generatePublic(new X509EncodedKeySpec(this.encoded));
/* 175 */       }if ((this.type == Type.PRIVATE) && ("PKCS#8".equals(this.format))) {
/* 176 */         localKeyFactory = KeyFactory.getInstance(this.algorithm);
/* 177 */         return localKeyFactory.generatePrivate(new PKCS8EncodedKeySpec(this.encoded));
/*     */       }
/* 179 */       throw new NotSerializableException("unrecognized type/format combination: " + this.type + "/" + this.format);
/*     */     }
/*     */     catch (NotSerializableException localNotSerializableException1)
/*     */     {
/* 184 */       throw localNotSerializableException1;
/*     */     } catch (Exception localException) {
/* 186 */       NotSerializableException localNotSerializableException2 = new NotSerializableException("java.security.Key: [" + this.type + "] " + "[" + this.algorithm + "] " + "[" + this.format + "]");
/*     */ 
/* 191 */       localNotSerializableException2.initCause(localException);
/* 192 */       throw localNotSerializableException2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum Type
/*     */   {
/*  69 */     SECRET, 
/*     */ 
/*  72 */     PUBLIC, 
/*     */ 
/*  75 */     PRIVATE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.KeyRep
 * JD-Core Version:    0.6.2
 */