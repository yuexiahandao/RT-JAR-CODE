/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Enumeration;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class PolicyConstraintsExtension extends Extension
/*     */   implements CertAttrSet<String>
/*     */ {
/*     */   public static final String IDENT = "x509.info.extensions.PolicyConstraints";
/*     */   public static final String NAME = "PolicyConstraints";
/*     */   public static final String REQUIRE = "require";
/*     */   public static final String INHIBIT = "inhibit";
/*     */   private static final byte TAG_REQUIRE = 0;
/*     */   private static final byte TAG_INHIBIT = 1;
/*  76 */   private int require = -1;
/*  77 */   private int inhibit = -1;
/*     */ 
/*     */   private void encodeThis() throws IOException
/*     */   {
/*  81 */     if ((this.require == -1) && (this.inhibit == -1)) {
/*  82 */       this.extensionValue = null;
/*  83 */       return;
/*     */     }
/*  85 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*  86 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*     */     DerOutputStream localDerOutputStream3;
/*  88 */     if (this.require != -1) {
/*  89 */       localDerOutputStream3 = new DerOutputStream();
/*  90 */       localDerOutputStream3.putInteger(this.require);
/*  91 */       localDerOutputStream1.writeImplicit(DerValue.createTag((byte)-128, false, (byte)0), localDerOutputStream3);
/*     */     }
/*     */ 
/*  94 */     if (this.inhibit != -1) {
/*  95 */       localDerOutputStream3 = new DerOutputStream();
/*  96 */       localDerOutputStream3.putInteger(this.inhibit);
/*  97 */       localDerOutputStream1.writeImplicit(DerValue.createTag((byte)-128, false, (byte)1), localDerOutputStream3);
/*     */     }
/*     */ 
/* 100 */     localDerOutputStream2.write((byte)48, localDerOutputStream1);
/* 101 */     this.extensionValue = localDerOutputStream2.toByteArray();
/*     */   }
/*     */ 
/*     */   public PolicyConstraintsExtension(int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 114 */     this(Boolean.FALSE, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public PolicyConstraintsExtension(Boolean paramBoolean, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 128 */     this.require = paramInt1;
/* 129 */     this.inhibit = paramInt2;
/* 130 */     this.extensionId = PKIXExtensions.PolicyConstraints_Id;
/* 131 */     this.critical = paramBoolean.booleanValue();
/* 132 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public PolicyConstraintsExtension(Boolean paramBoolean, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 145 */     this.extensionId = PKIXExtensions.PolicyConstraints_Id;
/* 146 */     this.critical = paramBoolean.booleanValue();
/*     */ 
/* 148 */     this.extensionValue = ((byte[])paramObject);
/* 149 */     DerValue localDerValue1 = new DerValue(this.extensionValue);
/* 150 */     if (localDerValue1.tag != 48) {
/* 151 */       throw new IOException("Sequence tag missing for PolicyConstraint.");
/*     */     }
/* 153 */     DerInputStream localDerInputStream = localDerValue1.data;
/* 154 */     while ((localDerInputStream != null) && (localDerInputStream.available() != 0)) {
/* 155 */       DerValue localDerValue2 = localDerInputStream.getDerValue();
/*     */ 
/* 157 */       if ((localDerValue2.isContextSpecific((byte)0)) && (!localDerValue2.isConstructed())) {
/* 158 */         if (this.require != -1) {
/* 159 */           throw new IOException("Duplicate requireExplicitPolicyfound in the PolicyConstraintsExtension");
/*     */         }
/* 161 */         localDerValue2.resetTag((byte)2);
/* 162 */         this.require = localDerValue2.getInteger();
/*     */       }
/* 164 */       else if ((localDerValue2.isContextSpecific((byte)1)) && (!localDerValue2.isConstructed()))
/*     */       {
/* 166 */         if (this.inhibit != -1) {
/* 167 */           throw new IOException("Duplicate inhibitPolicyMappingfound in the PolicyConstraintsExtension");
/*     */         }
/* 169 */         localDerValue2.resetTag((byte)2);
/* 170 */         this.inhibit = localDerValue2.getInteger();
/*     */       } else {
/* 172 */         throw new IOException("Invalid encoding of PolicyConstraint");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 181 */     String str = super.toString() + "PolicyConstraints: [" + "  Require: ";
/* 182 */     if (this.require == -1)
/* 183 */       str = str + "unspecified;";
/*     */     else
/* 185 */       str = str + this.require + ";";
/* 186 */     str = str + "\tInhibit: ";
/* 187 */     if (this.inhibit == -1)
/* 188 */       str = str + "unspecified";
/*     */     else
/* 190 */       str = str + this.inhibit;
/* 191 */     str = str + " ]\n";
/* 192 */     return str;
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 202 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 203 */     if (this.extensionValue == null) {
/* 204 */       this.extensionId = PKIXExtensions.PolicyConstraints_Id;
/* 205 */       this.critical = false;
/* 206 */       encodeThis();
/*     */     }
/* 208 */     super.encode(localDerOutputStream);
/* 209 */     paramOutputStream.write(localDerOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public void set(String paramString, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 216 */     if (!(paramObject instanceof Integer)) {
/* 217 */       throw new IOException("Attribute value should be of type Integer.");
/*     */     }
/* 219 */     if (paramString.equalsIgnoreCase("require"))
/* 220 */       this.require = ((Integer)paramObject).intValue();
/* 221 */     else if (paramString.equalsIgnoreCase("inhibit"))
/* 222 */       this.inhibit = ((Integer)paramObject).intValue();
/*     */     else {
/* 224 */       throw new IOException("Attribute name [" + paramString + "]" + " not recognized by " + "CertAttrSet:PolicyConstraints.");
/*     */     }
/*     */ 
/* 228 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Object get(String paramString)
/*     */     throws IOException
/*     */   {
/* 235 */     if (paramString.equalsIgnoreCase("require"))
/* 236 */       return new Integer(this.require);
/* 237 */     if (paramString.equalsIgnoreCase("inhibit")) {
/* 238 */       return new Integer(this.inhibit);
/*     */     }
/* 240 */     throw new IOException("Attribute name not recognized by CertAttrSet:PolicyConstraints.");
/*     */   }
/*     */ 
/*     */   public void delete(String paramString)
/*     */     throws IOException
/*     */   {
/* 249 */     if (paramString.equalsIgnoreCase("require"))
/* 250 */       this.require = -1;
/* 251 */     else if (paramString.equalsIgnoreCase("inhibit"))
/* 252 */       this.inhibit = -1;
/*     */     else {
/* 254 */       throw new IOException("Attribute name not recognized by CertAttrSet:PolicyConstraints.");
/*     */     }
/*     */ 
/* 257 */     encodeThis();
/*     */   }
/*     */ 
/*     */   public Enumeration<String> getElements()
/*     */   {
/* 265 */     AttributeNameEnumeration localAttributeNameEnumeration = new AttributeNameEnumeration();
/* 266 */     localAttributeNameEnumeration.addElement("require");
/* 267 */     localAttributeNameEnumeration.addElement("inhibit");
/*     */ 
/* 269 */     return localAttributeNameEnumeration.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 276 */     return "PolicyConstraints";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.PolicyConstraintsExtension
 * JD-Core Version:    0.6.2
 */