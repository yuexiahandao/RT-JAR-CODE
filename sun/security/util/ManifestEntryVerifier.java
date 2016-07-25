/*     */ package sun.security.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.CodeSigner;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.Provider;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Locale;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarException;
/*     */ import java.util.jar.Manifest;
/*     */ import sun.misc.BASE64Decoder;
/*     */ import sun.security.jca.Providers;
/*     */ 
/*     */ public class ManifestEntryVerifier
/*     */ {
/*  45 */   private static final Debug debug = Debug.getInstance("jar");
/*     */   HashMap<String, MessageDigest> createdDigests;
/*     */   ArrayList<MessageDigest> digests;
/*     */   ArrayList<byte[]> manifestHashes;
/*  66 */   private BASE64Decoder decoder = null;
/*  67 */   private String name = null;
/*     */   private Manifest man;
/*  70 */   private boolean skip = true;
/*     */   private JarEntry entry;
/*  74 */   private CodeSigner[] signers = null;
/*     */ 
/* 233 */   private static final char[] hexc = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*     */ 
/*     */   public ManifestEntryVerifier(Manifest paramManifest)
/*     */   {
/*  81 */     this.createdDigests = new HashMap(11);
/*  82 */     this.digests = new ArrayList();
/*  83 */     this.manifestHashes = new ArrayList();
/*  84 */     this.decoder = new BASE64Decoder();
/*  85 */     this.man = paramManifest;
/*     */   }
/*     */ 
/*     */   public void setEntry(String paramString, JarEntry paramJarEntry)
/*     */     throws IOException
/*     */   {
/*  97 */     this.digests.clear();
/*  98 */     this.manifestHashes.clear();
/*  99 */     this.name = paramString;
/* 100 */     this.entry = paramJarEntry;
/*     */ 
/* 102 */     this.skip = true;
/* 103 */     this.signers = null;
/*     */ 
/* 105 */     if ((this.man == null) || (paramString == null)) {
/* 106 */       return;
/*     */     }
/*     */ 
/* 112 */     Attributes localAttributes = this.man.getAttributes(paramString);
/* 113 */     if (localAttributes == null)
/*     */     {
/* 117 */       localAttributes = this.man.getAttributes("./" + paramString);
/* 118 */       if (localAttributes == null) {
/* 119 */         localAttributes = this.man.getAttributes("/" + paramString);
/* 120 */         if (localAttributes == null) {
/* 121 */           return;
/*     */         }
/*     */       }
/*     */     }
/* 125 */     for (Map.Entry localEntry : localAttributes.entrySet()) {
/* 126 */       String str1 = localEntry.getKey().toString();
/*     */ 
/* 128 */       if (str1.toUpperCase(Locale.ENGLISH).endsWith("-DIGEST"))
/*     */       {
/* 130 */         String str2 = str1.substring(0, str1.length() - 7);
/*     */ 
/* 132 */         MessageDigest localMessageDigest = (MessageDigest)this.createdDigests.get(str2);
/*     */ 
/* 134 */         if (localMessageDigest == null) {
/*     */           try
/*     */           {
/* 137 */             localMessageDigest = MessageDigest.getInstance(str2, SunProviderHolder.instance);
/*     */ 
/* 139 */             this.createdDigests.put(str2, localMessageDigest);
/*     */           }
/*     */           catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*     */           {
/*     */           }
/*     */         }
/* 145 */         if (localMessageDigest != null) {
/* 146 */           this.skip = false;
/* 147 */           localMessageDigest.reset();
/* 148 */           this.digests.add(localMessageDigest);
/* 149 */           this.manifestHashes.add(this.decoder.decodeBuffer((String)localEntry.getValue()));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void update(byte paramByte)
/*     */   {
/* 160 */     if (this.skip) return;
/*     */ 
/* 162 */     for (int i = 0; i < this.digests.size(); i++)
/* 163 */       ((MessageDigest)this.digests.get(i)).update(paramByte);
/*     */   }
/*     */ 
/*     */   public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 171 */     if (this.skip) return;
/*     */ 
/* 173 */     for (int i = 0; i < this.digests.size(); i++)
/* 174 */       ((MessageDigest)this.digests.get(i)).update(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public JarEntry getEntry()
/*     */   {
/* 183 */     return this.entry;
/*     */   }
/*     */ 
/*     */   public CodeSigner[] verify(Hashtable<String, CodeSigner[]> paramHashtable1, Hashtable<String, CodeSigner[]> paramHashtable2)
/*     */     throws JarException
/*     */   {
/* 198 */     if (this.skip) {
/* 199 */       return null;
/*     */     }
/*     */ 
/* 202 */     if (this.signers != null) {
/* 203 */       return this.signers;
/*     */     }
/* 205 */     for (int i = 0; i < this.digests.size(); i++)
/*     */     {
/* 207 */       MessageDigest localMessageDigest = (MessageDigest)this.digests.get(i);
/* 208 */       byte[] arrayOfByte1 = (byte[])this.manifestHashes.get(i);
/* 209 */       byte[] arrayOfByte2 = localMessageDigest.digest();
/*     */ 
/* 211 */       if (debug != null) {
/* 212 */         debug.println("Manifest Entry: " + this.name + " digest=" + localMessageDigest.getAlgorithm());
/*     */ 
/* 214 */         debug.println("  manifest " + toHex(arrayOfByte1));
/* 215 */         debug.println("  computed " + toHex(arrayOfByte2));
/* 216 */         debug.println();
/*     */       }
/*     */ 
/* 219 */       if (!MessageDigest.isEqual(arrayOfByte2, arrayOfByte1)) {
/* 220 */         throw new SecurityException(localMessageDigest.getAlgorithm() + " digest error for " + this.name);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 225 */     this.signers = ((CodeSigner[])paramHashtable2.remove(this.name));
/* 226 */     if (this.signers != null) {
/* 227 */       paramHashtable1.put(this.name, this.signers);
/*     */     }
/* 229 */     return this.signers;
/*     */   }
/*     */ 
/*     */   static String toHex(byte[] paramArrayOfByte)
/*     */   {
/* 243 */     StringBuffer localStringBuffer = new StringBuffer(paramArrayOfByte.length * 2);
/*     */ 
/* 245 */     for (int i = 0; i < paramArrayOfByte.length; i++) {
/* 246 */       localStringBuffer.append(hexc[(paramArrayOfByte[i] >> 4 & 0xF)]);
/* 247 */       localStringBuffer.append(hexc[(paramArrayOfByte[i] & 0xF)]);
/*     */     }
/* 249 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static class SunProviderHolder
/*     */   {
/*  54 */     private static final Provider instance = Providers.getSunProvider();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.ManifestEntryVerifier
 * JD-Core Version:    0.6.2
 */