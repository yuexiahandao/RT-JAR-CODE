/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class PolicyMappingsExtension extends Extension
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String IDENT = "x509.info.extensions.PolicyMappings";
/*     */   public static final String NAME = "PolicyMappings";
/*     */   public static final String MAP = "map";
/*     */   private List<CertificatePolicyMap> maps;
/*     */ 
/*     */   private void encodeThis()
/*     */     throws IOException
/*     */   {
/*  73 */     if ((this.maps == null) || (this.maps.isEmpty())) {
/*  74 */       this.extensionValue = null;
/*  75 */       return;
/*     */     }
/*  77 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*  78 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*     */ 
/*  80 */     for (CertificatePolicyMap localCertificatePolicyMap : this.maps) {
/*  81 */       localCertificatePolicyMap.encode(localDerOutputStream2);
/*     */     }
/*     */ 
/*  84 */     localDerOutputStream1.write((byte)48, localDerOutputStream2);
/*  85 */     this.extensionValue = localDerOutputStream1.toByteArray();
/*     */   }
/*     */ 
/*     */   public PolicyMappingsExtension(List<CertificatePolicyMap> paramList)
/*     */     throws IOException
/*     */   {
/*  95 */     this.maps = paramList;
/*  96 */     this.extensionId = PKIXExtensions.PolicyMappings_Id;
/*  97 */     this.critical = false;
/*  98 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public PolicyMappingsExtension()
/*     */   {
/* 105 */     this.extensionId = PKIXExtensions.KeyUsage_Id;
/* 106 */     this.critical = false;
/* 107 */     this.maps = new ArrayList();
/*     */   }
/*     */ 
/*     */   public PolicyMappingsExtension(Boolean paramBoolean, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 120 */     this.extensionId = PKIXExtensions.PolicyMappings_Id;
/* 121 */     this.critical = paramBoolean.booleanValue();
/*     */ 
/* 123 */     this.extensionValue = ((byte[])paramObject);
/* 124 */     DerValue localDerValue1 = new DerValue(this.extensionValue);
/* 125 */     if (localDerValue1.tag != 48) {
/* 126 */       throw new IOException("Invalid encoding for PolicyMappingsExtension.");
/*     */     }
/*     */ 
/* 129 */     this.maps = new ArrayList();
/* 130 */     while (localDerValue1.data.available() != 0) {
/* 131 */       DerValue localDerValue2 = localDerValue1.data.getDerValue();
/* 132 */       CertificatePolicyMap localCertificatePolicyMap = new CertificatePolicyMap(localDerValue2);
/* 133 */       this.maps.add(localCertificatePolicyMap);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 141 */     if (this.maps == null) return "";
/* 142 */     String str = super.toString() + "PolicyMappings [\n" + this.maps.toString() + "]\n";
/*     */ 
/* 145 */     return str;
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 155 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 156 */     if (this.extensionValue == null) {
/* 157 */       this.extensionId = PKIXExtensions.PolicyMappings_Id;
/* 158 */       this.critical = false;
/* 159 */       encodeThis();
/*     */     }
/* 161 */     super.encode(localDerOutputStream);
/* 162 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 169 */     if (paramString.equalsIgnoreCase("map")) {
/* 170 */       if (!(paramObject instanceof List)) {
/* 171 */         throw new IOException("Attribute value should be of type List.");
/*     */       }
/*     */ 
/* 174 */       this.maps = ((List)paramObject);
/*     */     } else {
/* 176 */       throw new IOException("Attribute name not recognized by CertAttrSet:PolicyMappingsExtension.");
/*     */     }
/*     */ 
/* 179 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 186 */     if (paramString.equalsIgnoreCase("map")) {
/* 187 */       return this.maps;
/*     */     }
/* 189 */     throw new IOException("Attribute name not recognized by CertAttrSet:PolicyMappingsExtension.");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 198 */     if (paramString.equalsIgnoreCase("map"))
/* 199 */       this.maps = null;
/*     */     else {
/* 201 */       throw new IOException("Attribute name not recognized by CertAttrSet:PolicyMappingsExtension.");
/*     */     }
/*     */ 
/* 204 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 212 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 213 */     localAttributeNameEnumeration.addElement("map");
/*     */ 
/* 215 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 222 */     return "PolicyMappings";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.PolicyMappingsExtension
 * JD-Core Version:    0.6.2
 */