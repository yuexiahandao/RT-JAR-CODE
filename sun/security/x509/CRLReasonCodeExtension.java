/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.cert.CRLReason;
/*     */ import java.util.Enumeration;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class CRLReasonCodeExtension extends Extension
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String NAME = "CRLReasonCode";
/*     */   public static final String REASON = "reason";
/*     */   public static final int UNSPECIFIED = 0;
/*     */   public static final int KEY_COMPROMISE = 1;
/*     */   public static final int CA_COMPROMISE = 2;
/*     */   public static final int AFFLIATION_CHANGED = 3;
/*     */   public static final int SUPERSEDED = 4;
/*     */   public static final int CESSATION_OF_OPERATION = 5;
/*     */   public static final int CERTIFICATE_HOLD = 6;
/*     */   public static final int REMOVE_FROM_CRL = 8;
/*     */   public static final int PRIVILEGE_WITHDRAWN = 9;
/*     */   public static final int AA_COMPROMISE = 10;
/*  84 */   private static CRLReason[] values = CRLReason.values();
/*     */ 
/*  86 */   private int reasonCode = 0;
/*     */ 
/*     */   private void encodeThis() throws IOException {
/*  89 */     if (this.reasonCode == 0) {
/*  90 */       this.extensionValue = null;
/*  91 */       return;
/*     */     }
/*  93 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/*  94 */     localDerOutputStream.putEnumerated(this.reasonCode);
/*  95 */     this.extensionValue = localDerOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public CRLReasonCodeExtension(int paramInt)
/*     */     throws IOException
/*     */   {
/* 105 */     this(false, paramInt);
/*     */   }
/*     */ 
/*     */   public CRLReasonCodeExtension(boolean paramBoolean, int paramInt)
/*     */     throws IOException
/*     */   {
/* 116 */     this.extensionId = PKIXExtensions.ReasonCode_Id;
/* 117 */     this.critical = paramBoolean;
/* 118 */     this.reasonCode = paramInt;
/* 119 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public CRLReasonCodeExtension(Boolean paramBoolean, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 132 */     this.extensionId = PKIXExtensions.ReasonCode_Id;
/* 133 */     this.critical = paramBoolean.booleanValue();
/* 134 */     this.extensionValue = ((byte[])paramObject);
/* 135 */     DerValue localDerValue = new DerValue(this.extensionValue);
/* 136 */     this.reasonCode = localDerValue.getEnumerated();
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 143 */     if (!(paramObject instanceof Integer)) {
/* 144 */       throw new IOException("Attribute must be of type Integer.");
/*     */     }
/* 146 */     if (paramString.equalsIgnoreCase("reason"))
/* 147 */       this.reasonCode = ((Integer)paramObject).intValue();
/*     */     else {
/* 149 */       throw new IOException("Name not supported by CRLReasonCodeExtension");
/*     */     }
/*     */ 
/* 152 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 159 */     if (paramString.equalsIgnoreCase("reason")) {
/* 160 */       return new Integer(this.reasonCode);
/*     */     }
/* 162 */     throw new IOException("Name not supported by CRLReasonCodeExtension");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 171 */     if (paramString.equalsIgnoreCase("reason"))
/* 172 */       this.reasonCode = 0;
/*     */     else {
/* 174 */       throw new IOException("Name not supported by CRLReasonCodeExtension");
/*     */     }
/*     */ 
/* 177 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 184 */     return super.toString() + "    Reason Code: " + values[this.reasonCode];
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 194 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/*     */ 
/* 196 */     if (this.extensionValue == null) {
/* 197 */       this.extensionId = PKIXExtensions.ReasonCode_Id;
/* 198 */       this.critical = false;
/* 199 */       encodeThis();
/*     */     }
/* 201 */     super.encode(localDerOutputStream);
/* 202 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 210 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 211 */     localAttributeNameEnumeration.addElement("reason");
/*     */ 
/* 213 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 220 */     return "CRLReasonCode";
/*     */   }
/*     */ 
/*     */   public CRLReason getReasonCode()
/*     */   {
/* 228 */     if ((this.reasonCode > 0) && (this.reasonCode < values.length)) {
/* 229 */       return values[this.reasonCode];
/*     */     }
/* 231 */     return CRLReason.UNSPECIFIED;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.CRLReasonCodeExtension
 * JD-Core Version:    0.6.2
 */