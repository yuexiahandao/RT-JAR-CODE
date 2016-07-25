/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class EDIPartyName
/*     */   implements GeneralNameInterface
/*     */ {
/*     */   private static final byte TAG_ASSIGNER = 0;
/*     */   private static final byte TAG_PARTYNAME = 1;
/*  51 */   private String assigner = null;
/*  52 */   private String party = null;
/*     */ 
/*  54 */   private int myhash = -1;
/*     */ 
/*     */   public EDIPartyName(String paramString1, String paramString2)
/*     */   {
/*  63 */     this.assigner = paramString1;
/*  64 */     this.party = paramString2;
/*     */   }
/*     */ 
/*     */   public EDIPartyName(String paramString)
/*     */   {
/*  73 */     this.party = paramString;
/*     */   }
/*     */ 
/*     */   public EDIPartyName(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/*  83 */     DerInputStream localDerInputStream = new DerInputStream(paramDerValue.toByteArray());
/*  84 */     DerValue[] arrayOfDerValue = localDerInputStream.getSequence(2);
/*     */ 
/*  86 */     int i = arrayOfDerValue.length;
/*  87 */     if ((i < 1) || (i > 2)) {
/*  88 */       throw new IOException("Invalid encoding of EDIPartyName");
/*     */     }
/*  90 */     for (int j = 0; j < i; j++) {
/*  91 */       DerValue localDerValue = arrayOfDerValue[j];
/*  92 */       if ((localDerValue.isContextSpecific((byte)0)) && (!localDerValue.isConstructed()))
/*     */       {
/*  94 */         if (this.assigner != null) {
/*  95 */           throw new IOException("Duplicate nameAssigner found in EDIPartyName");
/*     */         }
/*  97 */         localDerValue = localDerValue.data.getDerValue();
/*  98 */         this.assigner = localDerValue.getAsString();
/*     */       }
/* 100 */       if ((localDerValue.isContextSpecific((byte)1)) && (!localDerValue.isConstructed()))
/*     */       {
/* 102 */         if (this.party != null) {
/* 103 */           throw new IOException("Duplicate partyName found in EDIPartyName");
/*     */         }
/* 105 */         localDerValue = localDerValue.data.getDerValue();
/* 106 */         this.party = localDerValue.getAsString();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 115 */     return 5;
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 125 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/* 126 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*     */ 
/* 128 */     if (this.assigner != null) {
/* 129 */       DerOutputStream localDerOutputStream3 = new DerOutputStream();
/*     */ 
/* 131 */       localDerOutputStream3.putPrintableString(this.assigner);
/* 132 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, false, (byte)0), localDerOutputStream3);
/*     */     }
/*     */ 
/* 135 */     if (this.party == null) {
/* 136 */       throw new IOException("Cannot have null partyName");
/*     */     }
/*     */ 
/* 139 */     localDerOutputStream2.putPrintableString(this.party);
/* 140 */     localDerOutputStream1.write(DerValue.createTag((byte)-128, false, (byte)1), localDerOutputStream2);
/*     */ 
/* 143 */     paramDerOutputStream.write((byte)48, localDerOutputStream1);
/*     */   }
/*     */ 
/*     */   public String getAssignerName()
/*     */   {
/* 152 */     return this.assigner;
/*     */   }
/*     */ 
/*     */   public String getPartyName()
/*     */   {
/* 161 */     return this.party;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 172 */     if (!(paramObject instanceof EDIPartyName))
/* 173 */       return false;
/* 174 */     String str1 = ((EDIPartyName)paramObject).assigner;
/* 175 */     if (this.assigner == null) {
/* 176 */       if (str1 != null)
/* 177 */         return false;
/*     */     }
/* 179 */     else if (!this.assigner.equals(str1)) {
/* 180 */       return false;
/*     */     }
/* 182 */     String str2 = ((EDIPartyName)paramObject).party;
/* 183 */     if (this.party == null) {
/* 184 */       if (str2 != null)
/* 185 */         return false;
/*     */     }
/* 187 */     else if (!this.party.equals(str2)) {
/* 188 */       return false;
/*     */     }
/* 190 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 199 */     if (this.myhash == -1) {
/* 200 */       this.myhash = (37 + this.party.hashCode());
/* 201 */       if (this.assigner != null) {
/* 202 */         this.myhash = (37 * this.myhash + this.assigner.hashCode());
/*     */       }
/*     */     }
/* 205 */     return this.myhash;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 212 */     return "EDIPartyName: " + (this.assigner == null ? "" : new StringBuilder().append("  nameAssigner = ").append(this.assigner).append(",").toString()) + "  partyName = " + this.party;
/*     */   }
/*     */ 
/*     */   public int constrains(GeneralNameInterface paramGeneralNameInterface)
/*     */     throws UnsupportedOperationException
/*     */   {
/*     */     int i;
/* 235 */     if (paramGeneralNameInterface == null)
/* 236 */       i = -1;
/* 237 */     else if (paramGeneralNameInterface.getType() != 5)
/* 238 */       i = -1;
/*     */     else {
/* 240 */       throw new UnsupportedOperationException("Narrowing, widening, and matching of names not supported for EDIPartyName");
/*     */     }
/* 242 */     return i;
/*     */   }
/*     */ 
/*     */   public int subtreeDepth()
/*     */     throws UnsupportedOperationException
/*     */   {
/* 254 */     throw new UnsupportedOperationException("subtreeDepth() not supported for EDIPartyName");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.EDIPartyName
 * JD-Core Version:    0.6.2
 */