/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class AuthorityInfoAccessExtension extends Extension
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String IDENT = "x509.info.extensions.AuthorityInfoAccess";
/*     */   public static final String NAME = "AuthorityInfoAccess";
/*     */   public static final String DESCRIPTIONS = "descriptions";
/*     */   private List<AccessDescription> accessDescriptions;
/*     */ 
/*     */   public AuthorityInfoAccessExtension(List<AccessDescription> paramList)
/*     */     throws IOException
/*     */   {
/*  96 */     this.extensionId = PKIXExtensions.AuthInfoAccess_Id;
/*  97 */     this.critical = false;
/*  98 */     this.accessDescriptions = paramList;
/*  99 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public AuthorityInfoAccessExtension(Boolean paramBoolean, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 111 */     this.extensionId = PKIXExtensions.AuthInfoAccess_Id;
/* 112 */     this.critical = paramBoolean.booleanValue();
/*     */ 
/* 114 */     if (!(paramObject instanceof byte[])) {
/* 115 */       throw new IOException("Illegal argument type");
/*     */     }
/*     */ 
/* 118 */     this.extensionValue = ((byte[])paramObject);
/* 119 */     DerValue localDerValue1 = new DerValue(this.extensionValue);
/* 120 */     if (localDerValue1.tag != 48) {
/* 121 */       throw new IOException("Invalid encoding for AuthorityInfoAccessExtension.");
/*     */     }
/*     */ 
/* 124 */     this.accessDescriptions = new ArrayList();
/* 125 */     while (localDerValue1.data.available() != 0) {
/* 126 */       DerValue localDerValue2 = localDerValue1.data.getDerValue();
/* 127 */       AccessDescription localAccessDescription = new AccessDescription(localDerValue2);
/* 128 */       this.accessDescriptions.add(localAccessDescription);
/*     */     }
/*     */   }
/*     */ 
/*     */   public List<AccessDescription> getAccessDescriptions()
/*     */   {
/* 136 */     return this.accessDescriptions;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 143 */     return "AuthorityInfoAccess";
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 153 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 154 */     if (this.extensionValue == null) {
/* 155 */       this.extensionId = PKIXExtensions.AuthInfoAccess_Id;
/* 156 */       this.critical = false;
/* 157 */       encodeThis();
/*     */     }
/* 159 */     super.encode(localDerOutputStream);
/* 160 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 167 */     if (paramString.equalsIgnoreCase("descriptions")) {
/* 168 */       if (!(paramObject instanceof List)) {
/* 169 */         throw new IOException("Attribute value should be of type List.");
/*     */       }
/* 171 */       this.accessDescriptions = ((List)paramObject);
/*     */     } else {
/* 173 */       throw new IOException("Attribute name [" + paramString + "] not recognized by " + "CertAttrSet:AuthorityInfoAccessExtension.");
/*     */     }
/*     */ 
/* 177 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 184 */     if (paramString.equalsIgnoreCase("descriptions")) {
/* 185 */       return this.accessDescriptions;
/*     */     }
/* 187 */     throw new IOException("Attribute name [" + paramString + "] not recognized by " + "CertAttrSet:AuthorityInfoAccessExtension.");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 197 */     if (paramString.equalsIgnoreCase("descriptions"))
/* 198 */       this.accessDescriptions = new ArrayList();
/*     */     else {
/* 200 */       throw new IOException("Attribute name [" + paramString + "] not recognized by " + "CertAttrSet:AuthorityInfoAccessExtension.");
/*     */     }
/*     */ 
/* 204 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 212 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 213 */     localAttributeNameEnumeration.addElement("descriptions");
/* 214 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   private void encodeThis() throws IOException
/*     */   {
/* 219 */     if (this.accessDescriptions.isEmpty()) {
/* 220 */       this.extensionValue = null;
/*     */     } else {
/* 222 */       DerOutputStream localDerOutputStream = new DerOutputStream();
/* 223 */       for (Object localObject = this.accessDescriptions.iterator(); ((Iterator)localObject).hasNext(); ) { AccessDescription localAccessDescription = (AccessDescription)((Iterator)localObject).next();
/* 224 */         localAccessDescription.encode(localDerOutputStream);
/*     */       }
/* 226 */       localObject = new DerOutputStream();
/* 227 */       ((DerOutputStream)localObject).write((byte)48, localDerOutputStream);
/* 228 */       this.extensionValue = ((DerOutputStream)localObject).toByteArray();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 236 */     return super.toString() + "AuthorityInfoAccess [\n  " + this.accessDescriptions + "\n]\n";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.AuthorityInfoAccessExtension
 * JD-Core Version:    0.6.2
 */