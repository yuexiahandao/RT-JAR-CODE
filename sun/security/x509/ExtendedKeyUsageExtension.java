/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class ExtendedKeyUsageExtension extends Extension
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String IDENT = "x509.info.extensions.ExtendedKeyUsage";
/*     */   public static final String NAME = "ExtendedKeyUsage";
/*     */   public static final String USAGES = "usages";
/*  99 */   private static final Map<ObjectIdentifier, String> map = new HashMap();
/*     */ 
/* 102 */   private static final int[] anyExtendedKeyUsageOidData = { 2, 5, 29, 37, 0 };
/* 103 */   private static final int[] serverAuthOidData = { 1, 3, 6, 1, 5, 5, 7, 3, 1 };
/* 104 */   private static final int[] clientAuthOidData = { 1, 3, 6, 1, 5, 5, 7, 3, 2 };
/* 105 */   private static final int[] codeSigningOidData = { 1, 3, 6, 1, 5, 5, 7, 3, 3 };
/* 106 */   private static final int[] emailProtectionOidData = { 1, 3, 6, 1, 5, 5, 7, 3, 4 };
/* 107 */   private static final int[] ipsecEndSystemOidData = { 1, 3, 6, 1, 5, 5, 7, 3, 5 };
/* 108 */   private static final int[] ipsecTunnelOidData = { 1, 3, 6, 1, 5, 5, 7, 3, 6 };
/* 109 */   private static final int[] ipsecUserOidData = { 1, 3, 6, 1, 5, 5, 7, 3, 7 };
/* 110 */   private static final int[] timeStampingOidData = { 1, 3, 6, 1, 5, 5, 7, 3, 8 };
/* 111 */   private static final int[] OCSPSigningOidData = { 1, 3, 6, 1, 5, 5, 7, 3, 9 };
/*     */   private Vector<ObjectIdentifier> keyUsages;
/*     */ 
/*     */   private void encodeThis()
/*     */     throws IOException
/*     */   {
/* 133 */     if ((this.keyUsages == null) || (this.keyUsages.isEmpty())) {
/* 134 */       this.extensionValue = null;
/* 135 */       return;
/*     */     }
/* 137 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 138 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*     */ 
/* 140 */     for (int i = 0; i < this.keyUsages.size(); i++) {
/* 141 */       localDerOutputStream2.putOID((ObjectIdentifier)this.keyUsages.elementAt(i));
/*     */     }
/*     */ 
/* 144 */     localDerOutputStream1.write((byte)48, localDerOutputStream2);
/* 145 */     this.extensionValue = localDerOutputStream1.toByteArray();
/*     */   }
/*     */ 
/*     */   public ExtendedKeyUsageExtension(Vector<ObjectIdentifier> paramVector)
/*     */     throws IOException
/*     */   {
/* 156 */     this(Boolean.FALSE, paramVector);
/*     */   }
/*     */ 
/*     */   public ExtendedKeyUsageExtension(Boolean paramBoolean, Vector<ObjectIdentifier> paramVector)
/*     */     throws IOException
/*     */   {
/* 168 */     this.keyUsages = paramVector;
/* 169 */     this.extensionId = PKIXExtensions.ExtendedKeyUsage_Id;
/* 170 */     this.critical = paramBoolean.booleanValue();
/* 171 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public ExtendedKeyUsageExtension(Boolean paramBoolean, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 184 */     this.extensionId = PKIXExtensions.ExtendedKeyUsage_Id;
/* 185 */     this.critical = paramBoolean.booleanValue();
/* 186 */     this.extensionValue = ((byte[])paramObject);
/* 187 */     DerValue localDerValue1 = new DerValue(this.extensionValue);
/* 188 */     if (localDerValue1.tag != 48) {
/* 189 */       throw new IOException("Invalid encoding for ExtendedKeyUsageExtension.");
/*     */     }
/*     */ 
/* 192 */     this.keyUsages = new Vector();
/* 193 */     while (localDerValue1.data.available() != 0) {
/* 194 */       DerValue localDerValue2 = localDerValue1.data.getDerValue();
/* 195 */       ObjectIdentifier localObjectIdentifier = localDerValue2.getOID();
/* 196 */       this.keyUsages.addElement(localObjectIdentifier);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 204 */     if (this.keyUsages == null) return "";
/* 205 */     String str1 = "  ";
/* 206 */     int i = 1;
/* 207 */     for (ObjectIdentifier localObjectIdentifier : this.keyUsages) {
/* 208 */       if (i == 0) {
/* 209 */         str1 = str1 + "\n  ";
/*     */       }
/*     */ 
/* 212 */       String str2 = (String)map.get(localObjectIdentifier);
/* 213 */       if (str2 != null)
/* 214 */         str1 = str1 + str2;
/*     */       else {
/* 216 */         str1 = str1 + localObjectIdentifier.toString();
/*     */       }
/* 218 */       i = 0;
/*     */     }
/* 220 */     return super.toString() + "ExtendedKeyUsages [\n" + str1 + "\n]\n";
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 231 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 232 */     if (this.extensionValue == null) {
/* 233 */       this.extensionId = PKIXExtensions.ExtendedKeyUsage_Id;
/* 234 */       this.critical = false;
/* 235 */       encodeThis();
/*     */     }
/* 237 */     super.encode(localDerOutputStream);
/* 238 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 245 */     if (paramString.equalsIgnoreCase("usages")) {
/* 246 */       if (!(paramObject instanceof Vector)) {
/* 247 */         throw new IOException("Attribute value should be of type Vector.");
/*     */       }
/* 249 */       this.keyUsages = ((Vector)paramObject);
/*     */     } else {
/* 251 */       throw new IOException("Attribute name [" + paramString + "] not recognized by " + "CertAttrSet:ExtendedKeyUsageExtension.");
/*     */     }
/*     */ 
/* 255 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 262 */     if (paramString.equalsIgnoreCase("usages"))
/*     */     {
/* 264 */       return this.keyUsages;
/*     */     }
/* 266 */     throw new IOException("Attribute name [" + paramString + "] not recognized by " + "CertAttrSet:ExtendedKeyUsageExtension.");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 276 */     if (paramString.equalsIgnoreCase("usages"))
/* 277 */       this.keyUsages = null;
/*     */     else {
/* 279 */       throw new IOException("Attribute name [" + paramString + "] not recognized by " + "CertAttrSet:ExtendedKeyUsageExtension.");
/*     */     }
/*     */ 
/* 283 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 291 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 292 */     localAttributeNameEnumeration.addElement("usages");
/*     */ 
/* 294 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 301 */     return "ExtendedKeyUsage";
/*     */   }
/*     */ 
/*     */   public List<String> getExtendedKeyUsage() {
/* 305 */     ArrayList localArrayList = new ArrayList(this.keyUsages.size());
/* 306 */     for (ObjectIdentifier localObjectIdentifier : this.keyUsages) {
/* 307 */       localArrayList.add(localObjectIdentifier.toString());
/*     */     }
/* 309 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 114 */     map.put(ObjectIdentifier.newInternal(anyExtendedKeyUsageOidData), "anyExtendedKeyUsage");
/* 115 */     map.put(ObjectIdentifier.newInternal(serverAuthOidData), "serverAuth");
/* 116 */     map.put(ObjectIdentifier.newInternal(clientAuthOidData), "clientAuth");
/* 117 */     map.put(ObjectIdentifier.newInternal(codeSigningOidData), "codeSigning");
/* 118 */     map.put(ObjectIdentifier.newInternal(emailProtectionOidData), "emailProtection");
/* 119 */     map.put(ObjectIdentifier.newInternal(ipsecEndSystemOidData), "ipsecEndSystem");
/* 120 */     map.put(ObjectIdentifier.newInternal(ipsecTunnelOidData), "ipsecTunnel");
/* 121 */     map.put(ObjectIdentifier.newInternal(ipsecUserOidData), "ipsecUser");
/* 122 */     map.put(ObjectIdentifier.newInternal(timeStampingOidData), "timeStamping");
/* 123 */     map.put(ObjectIdentifier.newInternal(OCSPSigningOidData), "OCSPSigning");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.ExtendedKeyUsageExtension
 * JD-Core Version:    0.6.2
 */