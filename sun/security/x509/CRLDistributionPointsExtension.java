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
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class CRLDistributionPointsExtension extends Extension
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String IDENT = "x509.info.extensions.CRLDistributionPoints";
/*     */   public static final String NAME = "CRLDistributionPoints";
/*     */   public static final String POINTS = "points";
/*     */   private List<DistributionPoint> distributionPoints;
/*     */   private String extensionName;
/*     */ 
/*     */   public CRLDistributionPointsExtension(List<DistributionPoint> paramList)
/*     */     throws IOException
/*     */   {
/* 115 */     this(false, paramList);
/*     */   }
/*     */ 
/*     */   public CRLDistributionPointsExtension(boolean paramBoolean, List<DistributionPoint> paramList)
/*     */     throws IOException
/*     */   {
/* 129 */     this(PKIXExtensions.CRLDistributionPoints_Id, paramBoolean, paramList, "CRLDistributionPoints");
/*     */   }
/*     */ 
/*     */   protected CRLDistributionPointsExtension(ObjectIdentifier paramObjectIdentifier, boolean paramBoolean, List<DistributionPoint> paramList, String paramString)
/*     */     throws IOException
/*     */   {
/* 140 */     this.extensionId = paramObjectIdentifier;
/* 141 */     this.critical = paramBoolean;
/* 142 */     this.distributionPoints = paramList;
/* 143 */     encodeThis();
/* 144 */     this.extensionName = paramString;
/*     */   }
/*     */ 
/*     */   public CRLDistributionPointsExtension(Boolean paramBoolean, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 156 */     this(PKIXExtensions.CRLDistributionPoints_Id, paramBoolean, paramObject, "CRLDistributionPoints");
/*     */   }
/*     */ 
/*     */   protected CRLDistributionPointsExtension(ObjectIdentifier paramObjectIdentifier, Boolean paramBoolean, Object paramObject, String paramString)
/*     */     throws IOException
/*     */   {
/* 166 */     this.extensionId = paramObjectIdentifier;
/* 167 */     this.critical = paramBoolean.booleanValue();
/*     */ 
/* 169 */     if (!(paramObject instanceof byte[])) {
/* 170 */       throw new IOException("Illegal argument type");
/*     */     }
/*     */ 
/* 173 */     this.extensionValue = ((byte[])paramObject);
/* 174 */     DerValue localDerValue1 = new DerValue(this.extensionValue);
/* 175 */     if (localDerValue1.tag != 48) {
/* 176 */       throw new IOException("Invalid encoding for " + paramString + " extension.");
/*     */     }
/*     */ 
/* 179 */     this.distributionPoints = new ArrayList();
/* 180 */     while (localDerValue1.data.available() != 0) {
/* 181 */       DerValue localDerValue2 = localDerValue1.data.getDerValue();
/* 182 */       DistributionPoint localDistributionPoint = new DistributionPoint(localDerValue2);
/* 183 */       this.distributionPoints.add(localDistributionPoint);
/*     */     }
/* 185 */     this.extensionName = paramString;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 192 */     return this.extensionName;
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 202 */     encode(paramOutputStream, PKIXExtensions.CRLDistributionPoints_Id, false);
/*     */   }
/*     */ 
/*     */   protected void encode(OutputStream paramOutputStream, ObjectIdentifier paramObjectIdentifier, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 212 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 213 */     if (this.extensionValue == null) {
/* 214 */       this.extensionId = paramObjectIdentifier;
/* 215 */       this.critical = paramBoolean;
/* 216 */       encodeThis();
/*     */     }
/* 218 */     super.encode(localDerOutputStream);
/* 219 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 226 */     if (paramString.equalsIgnoreCase("points")) {
/* 227 */       if (!(paramObject instanceof List)) {
/* 228 */         throw new IOException("Attribute value should be of type List.");
/*     */       }
/* 230 */       this.distributionPoints = ((List)paramObject);
/*     */     } else {
/* 232 */       throw new IOException("Attribute name [" + paramString + "] not recognized by " + "CertAttrSet:" + this.extensionName + ".");
/*     */     }
/*     */ 
/* 236 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 243 */     if (paramString.equalsIgnoreCase("points")) {
/* 244 */       return this.distributionPoints;
/*     */     }
/* 246 */     throw new IOException("Attribute name [" + paramString + "] not recognized by " + "CertAttrSet:" + this.extensionName + ".");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 256 */     if (paramString.equalsIgnoreCase("points"))
/* 257 */       this.distributionPoints = new ArrayList();
/*     */     else {
/* 259 */       throw new IOException("Attribute name [" + paramString + "] not recognized by " + "CertAttrSet:" + this.extensionName + ".");
/*     */     }
/*     */ 
/* 263 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 271 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 272 */     localAttributeNameEnumeration.addElement("points");
/* 273 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   private void encodeThis() throws IOException
/*     */   {
/* 278 */     if (this.distributionPoints.isEmpty()) {
/* 279 */       this.extensionValue = null;
/*     */     } else {
/* 281 */       DerOutputStream localDerOutputStream = new DerOutputStream();
/* 282 */       for (Object localObject = this.distributionPoints.iterator(); ((Iterator)localObject).hasNext(); ) { DistributionPoint localDistributionPoint = (DistributionPoint)((Iterator)localObject).next();
/* 283 */         localDistributionPoint.encode(localDerOutputStream);
/*     */       }
/* 285 */       localObject = new DerOutputStream();
/* 286 */       ((DerOutputStream)localObject).write((byte)48, localDerOutputStream);
/* 287 */       this.extensionValue = ((DerOutputStream)localObject).toByteArray();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 295 */     return super.toString() + this.extensionName + " [\n  " + this.distributionPoints + "]\n";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.CRLDistributionPointsExtension
 * JD-Core Version:    0.6.2
 */