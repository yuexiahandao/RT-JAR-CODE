/*     */ package javax.security.auth.kerberos;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.security.auth.DestroyFailedException;
/*     */ import javax.security.auth.Destroyable;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ class KeyImpl
/*     */   implements SecretKey, Destroyable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7889313790214321193L;
/*     */   private transient byte[] keyBytes;
/*     */   private transient int keyType;
/*  57 */   private volatile transient boolean destroyed = false;
/*     */ 
/*     */   public KeyImpl(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/*  69 */     this.keyBytes = ((byte[])paramArrayOfByte.clone());
/*  70 */     this.keyType = paramInt;
/*     */   }
/*     */ 
/*     */   public KeyImpl(KerberosPrincipal paramKerberosPrincipal, char[] paramArrayOfChar, String paramString)
/*     */   {
/*     */     try
/*     */     {
/*  88 */       PrincipalName localPrincipalName = new PrincipalName(paramKerberosPrincipal.getName());
/*  89 */       EncryptionKey localEncryptionKey = new EncryptionKey(paramArrayOfChar, localPrincipalName.getSalt(), paramString);
/*     */ 
/*  91 */       this.keyBytes = localEncryptionKey.getBytes();
/*  92 */       this.keyType = localEncryptionKey.getEType();
/*     */     } catch (KrbException localKrbException) {
/*  94 */       throw new IllegalArgumentException(localKrbException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int getKeyType()
/*     */   {
/* 102 */     if (this.destroyed)
/* 103 */       throw new IllegalStateException("This key is no longer valid");
/* 104 */     return this.keyType;
/*     */   }
/*     */ 
/*     */   public final String getAlgorithm()
/*     */   {
/* 112 */     return getAlgorithmName(this.keyType);
/*     */   }
/*     */ 
/*     */   private String getAlgorithmName(int paramInt) {
/* 116 */     if (this.destroyed) {
/* 117 */       throw new IllegalStateException("This key is no longer valid");
/*     */     }
/* 119 */     switch (paramInt) {
/*     */     case 1:
/*     */     case 3:
/* 122 */       return "DES";
/*     */     case 16:
/* 125 */       return "DESede";
/*     */     case 23:
/* 128 */       return "ArcFourHmac";
/*     */     case 17:
/* 131 */       return "AES128";
/*     */     case 18:
/* 134 */       return "AES256";
/*     */     case 0:
/* 137 */       return "NULL";
/*     */     case 2:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     case 14:
/*     */     case 15:
/*     */     case 19:
/*     */     case 20:
/*     */     case 21:
/* 140 */     case 22: } throw new IllegalArgumentException("Unsupported encryption type: " + paramInt);
/*     */   }
/*     */ 
/*     */   public final String getFormat()
/*     */   {
/* 146 */     if (this.destroyed)
/* 147 */       throw new IllegalStateException("This key is no longer valid");
/* 148 */     return "RAW";
/*     */   }
/*     */ 
/*     */   public final byte[] getEncoded() {
/* 152 */     if (this.destroyed)
/* 153 */       throw new IllegalStateException("This key is no longer valid");
/* 154 */     return (byte[])this.keyBytes.clone();
/*     */   }
/*     */ 
/*     */   public void destroy() throws DestroyFailedException {
/* 158 */     if (!this.destroyed) {
/* 159 */       this.destroyed = true;
/* 160 */       Arrays.fill(this.keyBytes, (byte)0);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isDestroyed() {
/* 165 */     return this.destroyed;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 179 */     if (this.destroyed) {
/* 180 */       throw new IOException("This key is no longer valid");
/*     */     }
/*     */     try
/*     */     {
/* 184 */       paramObjectOutputStream.writeObject(new EncryptionKey(this.keyType, this.keyBytes).asn1Encode());
/*     */     } catch (Asn1Exception localAsn1Exception) {
/* 186 */       throw new IOException(localAsn1Exception.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/*     */     try {
/* 193 */       EncryptionKey localEncryptionKey = new EncryptionKey(new DerValue((byte[])paramObjectInputStream.readObject()));
/*     */ 
/* 195 */       this.keyType = localEncryptionKey.getEType();
/* 196 */       this.keyBytes = localEncryptionKey.getBytes();
/*     */     } catch (Asn1Exception localAsn1Exception) {
/* 198 */       throw new IOException(localAsn1Exception.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 203 */     HexDumpEncoder localHexDumpEncoder = new HexDumpEncoder();
/* 204 */     return "EncryptionKey: keyType=" + this.keyType + " keyBytes (hex dump)=" + ((this.keyBytes == null) || (this.keyBytes.length == 0) ? " Empty Key" : new StringBuilder().append('\n').append(localHexDumpEncoder.encodeBuffer(this.keyBytes)).append('\n').toString());
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 215 */     int i = 17;
/* 216 */     if (isDestroyed()) {
/* 217 */       return i;
/*     */     }
/* 219 */     i = 37 * i + Arrays.hashCode(this.keyBytes);
/* 220 */     return 37 * i + this.keyType;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 225 */     if (paramObject == this) {
/* 226 */       return true;
/*     */     }
/* 228 */     if (!(paramObject instanceof KeyImpl)) {
/* 229 */       return false;
/*     */     }
/*     */ 
/* 232 */     KeyImpl localKeyImpl = (KeyImpl)paramObject;
/* 233 */     if ((isDestroyed()) || (localKeyImpl.isDestroyed())) {
/* 234 */       return false;
/*     */     }
/*     */ 
/* 237 */     if ((this.keyType != localKeyImpl.getKeyType()) || (!Arrays.equals(this.keyBytes, localKeyImpl.getEncoded())))
/*     */     {
/* 239 */       return false;
/*     */     }
/*     */ 
/* 242 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.kerberos.KeyImpl
 * JD-Core Version:    0.6.2
 */