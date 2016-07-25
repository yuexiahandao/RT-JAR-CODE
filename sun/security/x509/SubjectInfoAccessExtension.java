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
/*     */ public class SubjectInfoAccessExtension extends Extension
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String IDENT = "x509.info.extensions.SubjectInfoAccess";
/*     */   public static final String NAME = "SubjectInfoAccess";
/*     */   public static final String DESCRIPTIONS = "descriptions";
/*     */   private List<AccessDescription> accessDescriptions;
/*     */ 
/*     */   public SubjectInfoAccessExtension(List<AccessDescription> paramList)
/*     */     throws IOException
/*     */   {
/* 100 */     this.extensionId = PKIXExtensions.SubjectInfoAccess_Id;
/* 101 */     this.critical = false;
/* 102 */     this.accessDescriptions = paramList;
/* 103 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public SubjectInfoAccessExtension(Boolean paramBoolean, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 115 */     this.extensionId = PKIXExtensions.SubjectInfoAccess_Id;
/* 116 */     this.critical = paramBoolean.booleanValue();
/*     */ 
/* 118 */     if (!(paramObject instanceof byte[])) {
/* 119 */       throw new IOException("Illegal argument type");
/*     */     }
/*     */ 
/* 122 */     this.extensionValue = ((byte[])paramObject);
/* 123 */     DerValue localDerValue1 = new DerValue(this.extensionValue);
/* 124 */     if (localDerValue1.tag != 48) {
/* 125 */       throw new IOException("Invalid encoding for SubjectInfoAccessExtension.");
/*     */     }
/*     */ 
/* 128 */     this.accessDescriptions = new ArrayList();
/* 129 */     while (localDerValue1.data.available() != 0) {
/* 130 */       DerValue localDerValue2 = localDerValue1.data.getDerValue();
/* 131 */       AccessDescription localAccessDescription = new AccessDescription(localDerValue2);
/* 132 */       this.accessDescriptions.add(localAccessDescription);
/*     */     }
/*     */   }
/*     */ 
/*     */   public List<AccessDescription> getAccessDescriptions()
/*     */   {
/* 140 */     return this.accessDescriptions;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 147 */     return "SubjectInfoAccess";
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 157 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 158 */     if (this.extensionValue == null) {
/* 159 */       this.extensionId = PKIXExtensions.SubjectInfoAccess_Id;
/* 160 */       this.critical = false;
/* 161 */       encodeThis();
/*     */     }
/* 163 */     super.encode(localDerOutputStream);
/* 164 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 171 */     if (paramString.equalsIgnoreCase("descriptions")) {
/* 172 */       if (!(paramObject instanceof List)) {
/* 173 */         throw new IOException("Attribute value should be of type List.");
/*     */       }
/* 175 */       this.accessDescriptions = ((List)paramObject);
/*     */     } else {
/* 177 */       throw new IOException("Attribute name [" + paramString + "] not recognized by " + "CertAttrSet:SubjectInfoAccessExtension.");
/*     */     }
/*     */ 
/* 181 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 188 */     if (paramString.equalsIgnoreCase("descriptions")) {
/* 189 */       return this.accessDescriptions;
/*     */     }
/* 191 */     throw new IOException("Attribute name [" + paramString + "] not recognized by " + "CertAttrSet:SubjectInfoAccessExtension.");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 201 */     if (paramString.equalsIgnoreCase("descriptions"))
/* 202 */       this.accessDescriptions = new ArrayList();
/*     */     else {
/* 204 */       throw new IOException("Attribute name [" + paramString + "] not recognized by " + "CertAttrSet:SubjectInfoAccessExtension.");
/*     */     }
/*     */ 
/* 208 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 216 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 217 */     localAttributeNameEnumeration.addElement("descriptions");
/* 218 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   private void encodeThis() throws IOException
/*     */   {
/* 223 */     if (this.accessDescriptions.isEmpty()) {
/* 224 */       this.extensionValue = null;
/*     */     } else {
/* 226 */       DerOutputStream localDerOutputStream = new DerOutputStream();
/* 227 */       for (Object localObject = this.accessDescriptions.iterator(); ((Iterator)localObject).hasNext(); ) { AccessDescription localAccessDescription = (AccessDescription)((Iterator)localObject).next();
/* 228 */         localAccessDescription.encode(localDerOutputStream);
/*     */       }
/* 230 */       localObject = new DerOutputStream();
/* 231 */       ((DerOutputStream)localObject).write((byte)48, localDerOutputStream);
/* 232 */       this.extensionValue = ((DerOutputStream)localObject).toByteArray();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 240 */     return super.toString() + "SubjectInfoAccess [\n  " + this.accessDescriptions + "\n]\n";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.SubjectInfoAccessExtension
 * JD-Core Version:    0.6.2
 */