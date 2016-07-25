/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public final class AccessDescription
/*     */ {
/*  39 */   private int myhash = -1;
/*     */   private ObjectIdentifier accessMethod;
/*     */   private GeneralName accessLocation;
/*  45 */   public static final ObjectIdentifier Ad_OCSP_Id = ObjectIdentifier.newInternal(new int[] { 1, 3, 6, 1, 5, 5, 7, 48, 1 });
/*     */ 
/*  48 */   public static final ObjectIdentifier Ad_CAISSUERS_Id = ObjectIdentifier.newInternal(new int[] { 1, 3, 6, 1, 5, 5, 7, 48, 2 });
/*     */ 
/*  51 */   public static final ObjectIdentifier Ad_TIMESTAMPING_Id = ObjectIdentifier.newInternal(new int[] { 1, 3, 6, 1, 5, 5, 7, 48, 3 });
/*     */ 
/*  54 */   public static final ObjectIdentifier Ad_CAREPOSITORY_Id = ObjectIdentifier.newInternal(new int[] { 1, 3, 6, 1, 5, 5, 7, 48, 5 });
/*     */ 
/*     */   public AccessDescription(ObjectIdentifier paramObjectIdentifier, GeneralName paramGeneralName)
/*     */   {
/*  58 */     this.accessMethod = paramObjectIdentifier;
/*  59 */     this.accessLocation = paramGeneralName;
/*     */   }
/*     */ 
/*     */   public AccessDescription(DerValue paramDerValue) throws IOException {
/*  63 */     DerInputStream localDerInputStream = paramDerValue.getData();
/*  64 */     this.accessMethod = localDerInputStream.getOID();
/*  65 */     this.accessLocation = new GeneralName(localDerInputStream.getDerValue());
/*     */   }
/*     */ 
/*     */   public ObjectIdentifier getAccessMethod() {
/*  69 */     return this.accessMethod;
/*     */   }
/*     */ 
/*     */   public GeneralName getAccessLocation() {
/*  73 */     return this.accessLocation;
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream) throws IOException {
/*  77 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/*  78 */     localDerOutputStream.putOID(this.accessMethod);
/*  79 */     this.accessLocation.encode(localDerOutputStream);
/*  80 */     paramDerOutputStream.write((byte)48, localDerOutputStream);
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  84 */     if (this.myhash == -1) {
/*  85 */       this.myhash = (this.accessMethod.hashCode() + this.accessLocation.hashCode());
/*     */     }
/*  87 */     return this.myhash;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/*  91 */     if ((paramObject == null) || (!(paramObject instanceof AccessDescription))) {
/*  92 */       return false;
/*     */     }
/*  94 */     AccessDescription localAccessDescription = (AccessDescription)paramObject;
/*     */ 
/*  96 */     if (this == localAccessDescription) {
/*  97 */       return true;
/*     */     }
/*  99 */     return (this.accessMethod.equals(localAccessDescription.getAccessMethod())) && (this.accessLocation.equals(localAccessDescription.getAccessLocation()));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 104 */     String str = null;
/* 105 */     if (this.accessMethod.equals(Ad_CAISSUERS_Id))
/* 106 */       str = "caIssuers";
/* 107 */     else if (this.accessMethod.equals(Ad_CAREPOSITORY_Id))
/* 108 */       str = "caRepository";
/* 109 */     else if (this.accessMethod.equals(Ad_TIMESTAMPING_Id))
/* 110 */       str = "timeStamping";
/* 111 */     else if (this.accessMethod.equals(Ad_OCSP_Id))
/* 112 */       str = "ocsp";
/*     */     else {
/* 114 */       str = this.accessMethod.toString();
/*     */     }
/* 116 */     return "\n   accessMethod: " + str + "\n   accessLocation: " + this.accessLocation.toString() + "\n";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.AccessDescription
 * JD-Core Version:    0.6.2
 */