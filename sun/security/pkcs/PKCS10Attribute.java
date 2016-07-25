/*     */ package sun.security.pkcs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import sun.security.util.DerEncoder;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class PKCS10Attribute
/*     */   implements DerEncoder
/*     */ {
/*  58 */   protected ObjectIdentifier attributeId = null;
/*  59 */   protected Object attributeValue = null;
/*     */ 
/*     */   public PKCS10Attribute(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/*  72 */     PKCS9Attribute localPKCS9Attribute = new PKCS9Attribute(paramDerValue);
/*  73 */     this.attributeId = localPKCS9Attribute.getOID();
/*  74 */     this.attributeValue = localPKCS9Attribute.getValue();
/*     */   }
/*     */ 
/*     */   public PKCS10Attribute(ObjectIdentifier paramObjectIdentifier, Object paramObject)
/*     */   {
/*  87 */     this.attributeId = paramObjectIdentifier;
/*  88 */     this.attributeValue = paramObject;
/*     */   }
/*     */ 
/*     */   public PKCS10Attribute(PKCS9Attribute paramPKCS9Attribute)
/*     */   {
/*  97 */     this.attributeId = paramPKCS9Attribute.getOID();
/*  98 */     this.attributeValue = paramPKCS9Attribute.getValue();
/*     */   }
/*     */ 
/*     */   public void derEncode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 111 */     PKCS9Attribute localPKCS9Attribute = new PKCS9Attribute(this.attributeId, this.attributeValue);
/* 112 */     localPKCS9Attribute.derEncode(paramOutputStream);
/*     */   }
/*     */ 
/*     */   public ObjectIdentifier getAttributeId()
/*     */   {
/* 119 */     return this.attributeId;
/*     */   }
/*     */ 
/*     */   public Object getAttributeValue()
/*     */   {
/* 126 */     return this.attributeValue;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 133 */     return this.attributeValue.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.pkcs.PKCS10Attribute
 * JD-Core Version:    0.6.2
 */