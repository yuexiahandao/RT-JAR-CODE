/*     */ package sun.security.pkcs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class ContentInfo
/*     */ {
/*  41 */   private static int[] pkcs7 = { 1, 2, 840, 113549, 1, 7 };
/*  42 */   private static int[] data = { 1, 2, 840, 113549, 1, 7, 1 };
/*  43 */   private static int[] sdata = { 1, 2, 840, 113549, 1, 7, 2 };
/*  44 */   private static int[] edata = { 1, 2, 840, 113549, 1, 7, 3 };
/*  45 */   private static int[] sedata = { 1, 2, 840, 113549, 1, 7, 4 };
/*  46 */   private static int[] ddata = { 1, 2, 840, 113549, 1, 7, 5 };
/*  47 */   private static int[] crdata = { 1, 2, 840, 113549, 1, 7, 6 };
/*  48 */   private static int[] nsdata = { 2, 16, 840, 1, 113730, 2, 5 };
/*     */ 
/*  50 */   private static int[] tstInfo = { 1, 2, 840, 113549, 1, 9, 16, 1, 4 };
/*     */ 
/*  52 */   private static final int[] OLD_SDATA = { 1, 2, 840, 1113549, 1, 7, 2 };
/*  53 */   private static final int[] OLD_DATA = { 1, 2, 840, 1113549, 1, 7, 1 };
/*     */ 
/*  67 */   public static ObjectIdentifier PKCS7_OID = ObjectIdentifier.newInternal(pkcs7);
/*  68 */   public static ObjectIdentifier DATA_OID = ObjectIdentifier.newInternal(data);
/*  69 */   public static ObjectIdentifier SIGNED_DATA_OID = ObjectIdentifier.newInternal(sdata);
/*  70 */   public static ObjectIdentifier ENVELOPED_DATA_OID = ObjectIdentifier.newInternal(edata);
/*  71 */   public static ObjectIdentifier SIGNED_AND_ENVELOPED_DATA_OID = ObjectIdentifier.newInternal(sedata);
/*  72 */   public static ObjectIdentifier DIGESTED_DATA_OID = ObjectIdentifier.newInternal(ddata);
/*  73 */   public static ObjectIdentifier ENCRYPTED_DATA_OID = ObjectIdentifier.newInternal(crdata);
/*  74 */   public static ObjectIdentifier OLD_SIGNED_DATA_OID = ObjectIdentifier.newInternal(OLD_SDATA);
/*  75 */   public static ObjectIdentifier OLD_DATA_OID = ObjectIdentifier.newInternal(OLD_DATA);
/*     */ 
/*  82 */   public static ObjectIdentifier NETSCAPE_CERT_SEQUENCE_OID = ObjectIdentifier.newInternal(nsdata);
/*  83 */   public static ObjectIdentifier TIMESTAMP_TOKEN_INFO_OID = ObjectIdentifier.newInternal(tstInfo);
/*     */   ObjectIdentifier contentType;
/*     */   DerValue content;
/*     */ 
/*     */   public ContentInfo(ObjectIdentifier paramObjectIdentifier, DerValue paramDerValue)
/*     */   {
/*  90 */     this.contentType = paramObjectIdentifier;
/*  91 */     this.content = paramDerValue;
/*     */   }
/*     */ 
/*     */   public ContentInfo(byte[] paramArrayOfByte)
/*     */   {
/*  98 */     DerValue localDerValue = new DerValue((byte)4, paramArrayOfByte);
/*  99 */     this.contentType = DATA_OID;
/* 100 */     this.content = localDerValue;
/*     */   }
/*     */ 
/*     */   public ContentInfo(DerInputStream paramDerInputStream)
/*     */     throws IOException, ParsingException
/*     */   {
/* 109 */     this(paramDerInputStream, false);
/*     */   }
/*     */ 
/*     */   public ContentInfo(DerInputStream paramDerInputStream, boolean paramBoolean)
/*     */     throws IOException, ParsingException
/*     */   {
/* 132 */     DerValue[] arrayOfDerValue1 = paramDerInputStream.getSequence(2);
/*     */ 
/* 135 */     DerValue localDerValue1 = arrayOfDerValue1[0];
/* 136 */     DerInputStream localDerInputStream1 = new DerInputStream(localDerValue1.toByteArray());
/* 137 */     this.contentType = localDerInputStream1.getOID();
/*     */ 
/* 139 */     if (paramBoolean)
/*     */     {
/* 141 */       this.content = arrayOfDerValue1[1];
/*     */     }
/* 147 */     else if (arrayOfDerValue1.length > 1) {
/* 148 */       DerValue localDerValue2 = arrayOfDerValue1[1];
/* 149 */       DerInputStream localDerInputStream2 = new DerInputStream(localDerValue2.toByteArray());
/*     */ 
/* 151 */       DerValue[] arrayOfDerValue2 = localDerInputStream2.getSet(1, true);
/* 152 */       this.content = arrayOfDerValue2[0];
/*     */     }
/*     */   }
/*     */ 
/*     */   public DerValue getContent()
/*     */   {
/* 158 */     return this.content;
/*     */   }
/*     */ 
/*     */   public ObjectIdentifier getContentType() {
/* 162 */     return this.contentType;
/*     */   }
/*     */ 
/*     */   public byte[] getData() throws IOException {
/* 166 */     if ((this.contentType.equals(DATA_OID)) || (this.contentType.equals(OLD_DATA_OID)) || (this.contentType.equals(TIMESTAMP_TOKEN_INFO_OID)))
/*     */     {
/* 169 */       if (this.content == null) {
/* 170 */         return null;
/*     */       }
/* 172 */       return this.content.getOctetString();
/*     */     }
/* 174 */     throw new IOException("content type is not DATA: " + this.contentType);
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 181 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 182 */     localDerOutputStream2.putOID(this.contentType);
/*     */ 
/* 185 */     if (this.content != null) {
/* 186 */       DerValue localDerValue = null;
/* 187 */       DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 188 */       this.content.encode(localDerOutputStream1);
/*     */ 
/* 191 */       localDerValue = new DerValue((byte)-96, localDerOutputStream1.toByteArray());
/*     */ 
/* 193 */       localDerOutputStream2.putDerValue(localDerValue);
/*     */     }
/*     */ 
/* 196 */     paramDerOutputStream.write((byte)48, localDerOutputStream2);
/*     */   }
/*     */ 
/*     */   public byte[] getContentBytes()
/*     */     throws IOException
/*     */   {
/* 204 */     if (this.content == null) {
/* 205 */       return null;
/*     */     }
/* 207 */     DerInputStream localDerInputStream = new DerInputStream(this.content.toByteArray());
/* 208 */     return localDerInputStream.getOctetString();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 212 */     String str = "";
/*     */ 
/* 214 */     str = str + "Content Info Sequence\n\tContent type: " + this.contentType + "\n";
/* 215 */     str = str + "\tContent: " + this.content;
/* 216 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.pkcs.ContentInfo
 * JD-Core Version:    0.6.2
 */