/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.cert.PolicyQualifierInfo;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class PolicyInformation
/*     */ {
/*     */   public static final String NAME = "PolicyInformation";
/*     */   public static final String ID = "id";
/*     */   public static final String QUALIFIERS = "qualifiers";
/*     */   private CertificatePolicyId policyIdentifier;
/*     */   private Set<PolicyQualifierInfo> policyQualifiers;
/*     */ 
/*     */   public PolicyInformation(CertificatePolicyId paramCertificatePolicyId, Set<PolicyQualifierInfo> paramSet)
/*     */     throws IOException
/*     */   {
/*  87 */     if (paramSet == null) {
/*  88 */       throw new NullPointerException("policyQualifiers is null");
/*     */     }
/*  90 */     this.policyQualifiers = new LinkedHashSet(paramSet);
/*     */ 
/*  92 */     this.policyIdentifier = paramCertificatePolicyId;
/*     */   }
/*     */ 
/*     */   public PolicyInformation(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/* 103 */     if (paramDerValue.tag != 48) {
/* 104 */       throw new IOException("Invalid encoding of PolicyInformation");
/*     */     }
/* 106 */     this.policyIdentifier = new CertificatePolicyId(paramDerValue.data.getDerValue());
/* 107 */     if (paramDerValue.data.available() != 0) {
/* 108 */       this.policyQualifiers = new LinkedHashSet();
/* 109 */       DerValue localDerValue = paramDerValue.data.getDerValue();
/* 110 */       if (localDerValue.tag != 48)
/* 111 */         throw new IOException("Invalid encoding of PolicyInformation");
/* 112 */       if (localDerValue.data.available() == 0)
/* 113 */         throw new IOException("No data available in policyQualifiers");
/* 114 */       while (localDerValue.data.available() != 0)
/* 115 */         this.policyQualifiers.add(new PolicyQualifierInfo(localDerValue.data.getDerValue().toByteArray()));
/*     */     }
/*     */     else {
/* 118 */       this.policyQualifiers = Collections.emptySet();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 129 */     if (!(paramObject instanceof PolicyInformation))
/* 130 */       return false;
/* 131 */     PolicyInformation localPolicyInformation = (PolicyInformation)paramObject;
/*     */ 
/* 133 */     if (!this.policyIdentifier.equals(localPolicyInformation.getPolicyIdentifier())) {
/* 134 */       return false;
/*     */     }
/* 136 */     return this.policyQualifiers.equals(localPolicyInformation.getPolicyQualifiers());
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 145 */     int i = 37 + this.policyIdentifier.hashCode();
/* 146 */     i = 37 * i + this.policyQualifiers.hashCode();
/* 147 */     return i;
/*     */   }
/*     */ 
/*     */   public CertificatePolicyId getPolicyIdentifier()
/*     */   {
/* 157 */     return this.policyIdentifier;
/*     */   }
/*     */ 
/*     */   public Set<PolicyQualifierInfo> getPolicyQualifiers()
/*     */   {
/* 169 */     return this.policyQualifiers;
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 176 */     if (paramString.equalsIgnoreCase("id"))
/* 177 */       return this.policyIdentifier;
/* 178 */     if (paramString.equalsIgnoreCase("qualifiers")) {
/* 179 */       return this.policyQualifiers;
/*     */     }
/* 181 */     throw new IOException("Attribute name [" + paramString + "] not recognized by PolicyInformation.");
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 190 */     if (paramString.equalsIgnoreCase("id")) {
/* 191 */       if ((paramObject instanceof CertificatePolicyId))
/* 192 */         this.policyIdentifier = ((CertificatePolicyId)paramObject);
/*     */       else
/* 194 */         throw new IOException("Attribute value must be instance of CertificatePolicyId.");
/*     */     }
/* 196 */     else if (paramString.equalsIgnoreCase("qualifiers")) {
/* 197 */       if (this.policyIdentifier == null) {
/* 198 */         throw new IOException("Attribute must have a CertificatePolicyIdentifier value before PolicyQualifierInfo can be set.");
/*     */       }
/*     */ 
/* 202 */       if ((paramObject instanceof Set)) {
/* 203 */         Iterator localIterator = ((Set)paramObject).iterator();
/* 204 */         while (localIterator.hasNext()) {
/* 205 */           Object localObject = localIterator.next();
/* 206 */           if (!(localObject instanceof PolicyQualifierInfo)) {
/* 207 */             throw new IOException("Attribute value must be aSet of PolicyQualifierInfo objects.");
/*     */           }
/*     */         }
/*     */ 
/* 211 */         this.policyQualifiers = ((Set)paramObject);
/*     */       } else {
/* 213 */         throw new IOException("Attribute value must be of type Set.");
/*     */       }
/*     */     } else {
/* 216 */       throw new IOException("Attribute name [" + paramString + "] not recognized by PolicyInformation");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 225 */     if (paramString.equalsIgnoreCase("qualifiers")) {
/* 226 */       this.policyQualifiers = Collections.emptySet(); } else {
/* 227 */       if (paramString.equalsIgnoreCase("id")) {
/* 228 */         throw new IOException("Attribute ID may not be deleted from PolicyInformation.");
/*     */       }
/*     */ 
/* 232 */       throw new IOException("Attribute name [" + paramString + "] not recognized by PolicyInformation.");
/*     */     }
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 242 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 243 */     localAttributeNameEnumeration.addElement("id");
/* 244 */     localAttributeNameEnumeration.addElement("qualifiers");
/*     */ 
/* 246 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 253 */     return "PolicyInformation";
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 260 */     StringBuilder localStringBuilder = new StringBuilder("  [" + this.policyIdentifier.toString());
/* 261 */     localStringBuilder.append(this.policyQualifiers + "  ]\n");
/* 262 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 272 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 273 */     this.policyIdentifier.encode(localDerOutputStream1);
/* 274 */     if (!this.policyQualifiers.isEmpty()) {
/* 275 */       DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 276 */       for (PolicyQualifierInfo localPolicyQualifierInfo : this.policyQualifiers) {
/* 277 */         localDerOutputStream2.write(localPolicyQualifierInfo.getEncoded());
/*     */       }
/* 279 */       localDerOutputStream1.write((byte)48, localDerOutputStream2);
/*     */     }
/* 281 */     paramDerOutputStream.write((byte)48, localDerOutputStream1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.PolicyInformation
 * JD-Core Version:    0.6.2
 */