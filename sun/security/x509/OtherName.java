/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Arrays;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class OtherName
/*     */   implements GeneralNameInterface
/*     */ {
/*     */   private String name;
/*     */   private ObjectIdentifier oid;
/*  53 */   private byte[] nameValue = null;
/*  54 */   private GeneralNameInterface gni = null;
/*     */   private static final byte TAG_VALUE = 0;
/*  58 */   private int myhash = -1;
/*     */ 
/*     */   public OtherName(ObjectIdentifier paramObjectIdentifier, byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  69 */     if ((paramObjectIdentifier == null) || (paramArrayOfByte == null)) {
/*  70 */       throw new NullPointerException("parameters may not be null");
/*     */     }
/*  72 */     this.oid = paramObjectIdentifier;
/*  73 */     this.nameValue = paramArrayOfByte;
/*  74 */     this.gni = getGNI(paramObjectIdentifier, paramArrayOfByte);
/*  75 */     if (this.gni != null)
/*  76 */       this.name = this.gni.toString();
/*     */     else
/*  78 */       this.name = ("Unrecognized ObjectIdentifier: " + paramObjectIdentifier.toString());
/*     */   }
/*     */ 
/*     */   public OtherName(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/*  89 */     DerInputStream localDerInputStream = paramDerValue.toDerInputStream();
/*     */ 
/*  91 */     this.oid = localDerInputStream.getOID();
/*  92 */     DerValue localDerValue = localDerInputStream.getDerValue();
/*  93 */     this.nameValue = localDerValue.toByteArray();
/*  94 */     this.gni = getGNI(this.oid, this.nameValue);
/*  95 */     if (this.gni != null)
/*  96 */       this.name = this.gni.toString();
/*     */     else
/*  98 */       this.name = ("Unrecognized ObjectIdentifier: " + this.oid.toString());
/*     */   }
/*     */ 
/*     */   public ObjectIdentifier getOID()
/*     */   {
/* 107 */     return this.oid;
/*     */   }
/*     */ 
/*     */   public byte[] getNameValue()
/*     */   {
/* 114 */     return (byte[])this.nameValue.clone();
/*     */   }
/*     */ 
/*     */   private GeneralNameInterface getGNI(ObjectIdentifier paramObjectIdentifier, byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 123 */       Class localClass = OIDMap.getClass(paramObjectIdentifier);
/* 124 */       if (localClass == null) {
/* 125 */         return null;
/*     */       }
/* 127 */       Class[] arrayOfClass = { Object.class };
/* 128 */       Constructor localConstructor = localClass.getConstructor(arrayOfClass);
/*     */ 
/* 130 */       Object[] arrayOfObject = { paramArrayOfByte };
/* 131 */       return (GeneralNameInterface)localConstructor.newInstance(arrayOfObject);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 135 */       throw ((IOException)new IOException("Instantiation error: " + localException).initCause(localException));
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 143 */     return 0;
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 153 */     if (this.gni != null)
/*     */     {
/* 155 */       this.gni.encode(paramDerOutputStream);
/* 156 */       return;
/*     */     }
/*     */ 
/* 159 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 160 */     localDerOutputStream.putOID(this.oid);
/* 161 */     localDerOutputStream.write(DerValue.createTag((byte)-128, true, (byte)0), this.nameValue);
/* 162 */     paramDerOutputStream.write((byte)48, localDerOutputStream);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 172 */     if (this == paramObject) {
/* 173 */       return true;
/*     */     }
/* 175 */     if (!(paramObject instanceof OtherName)) {
/* 176 */       return false;
/*     */     }
/* 178 */     OtherName localOtherName = (OtherName)paramObject;
/* 179 */     if (!localOtherName.oid.equals(this.oid)) {
/* 180 */       return false;
/*     */     }
/* 182 */     GeneralNameInterface localGeneralNameInterface = null;
/*     */     try {
/* 184 */       localGeneralNameInterface = getGNI(localOtherName.oid, localOtherName.nameValue);
/*     */     } catch (IOException localIOException) {
/* 186 */       return false;
/*     */     }
/*     */     boolean bool;
/* 190 */     if (localGeneralNameInterface != null)
/*     */       try {
/* 192 */         bool = localGeneralNameInterface.constrains(this) == 0;
/*     */       } catch (UnsupportedOperationException localUnsupportedOperationException) {
/* 194 */         bool = false;
/*     */       }
/*     */     else {
/* 197 */       bool = Arrays.equals(this.nameValue, localOtherName.nameValue);
/*     */     }
/*     */ 
/* 200 */     return bool;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 209 */     if (this.myhash == -1) {
/* 210 */       this.myhash = (37 + this.oid.hashCode());
/* 211 */       for (int i = 0; i < this.nameValue.length; i++) {
/* 212 */         this.myhash = (37 * this.myhash + this.nameValue[i]);
/*     */       }
/*     */     }
/* 215 */     return this.myhash;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 222 */     return "Other-Name: " + this.name;
/*     */   }
/*     */ 
/*     */   public int constrains(GeneralNameInterface paramGeneralNameInterface)
/*     */   {
/*     */     int i;
/* 246 */     if (paramGeneralNameInterface == null)
/* 247 */       i = -1;
/* 248 */     else if (paramGeneralNameInterface.getType() != 0)
/* 249 */       i = -1;
/*     */     else {
/* 251 */       throw new UnsupportedOperationException("Narrowing, widening, and matching are not supported for OtherName.");
/*     */     }
/*     */ 
/* 254 */     return i;
/*     */   }
/*     */ 
/*     */   public int subtreeDepth()
/*     */   {
/* 265 */     throw new UnsupportedOperationException("subtreeDepth() not supported for generic OtherName");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.OtherName
 * JD-Core Version:    0.6.2
 */