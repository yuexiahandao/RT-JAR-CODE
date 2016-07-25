/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class GeneralSubtree
/*     */ {
/*     */   private static final byte TAG_MIN = 0;
/*     */   private static final byte TAG_MAX = 1;
/*     */   private static final int MIN_DEFAULT = 0;
/*     */   private GeneralName name;
/*  51 */   private int minimum = 0;
/*  52 */   private int maximum = -1;
/*     */ 
/*  54 */   private int myhash = -1;
/*     */ 
/*     */   public GeneralSubtree(GeneralName paramGeneralName, int paramInt1, int paramInt2)
/*     */   {
/*  64 */     this.name = paramGeneralName;
/*  65 */     this.minimum = paramInt1;
/*  66 */     this.maximum = paramInt2;
/*     */   }
/*     */ 
/*     */   public GeneralSubtree(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/*  75 */     if (paramDerValue.tag != 48) {
/*  76 */       throw new IOException("Invalid encoding for GeneralSubtree.");
/*     */     }
/*  78 */     this.name = new GeneralName(paramDerValue.data.getDerValue(), true);
/*     */ 
/*  83 */     while (paramDerValue.data.available() != 0) {
/*  84 */       DerValue localDerValue = paramDerValue.data.getDerValue();
/*     */ 
/*  86 */       if ((localDerValue.isContextSpecific((byte)0)) && (!localDerValue.isConstructed())) {
/*  87 */         localDerValue.resetTag((byte)2);
/*  88 */         this.minimum = localDerValue.getInteger();
/*     */       }
/*  90 */       else if ((localDerValue.isContextSpecific((byte)1)) && (!localDerValue.isConstructed())) {
/*  91 */         localDerValue.resetTag((byte)2);
/*  92 */         this.maximum = localDerValue.getInteger();
/*     */       } else {
/*  94 */         throw new IOException("Invalid encoding of GeneralSubtree.");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public GeneralName getName()
/*     */   {
/* 105 */     return this.name;
/*     */   }
/*     */ 
/*     */   public int getMinimum()
/*     */   {
/* 114 */     return this.minimum;
/*     */   }
/*     */ 
/*     */   public int getMaximum()
/*     */   {
/* 123 */     return this.maximum;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 130 */     String str = "\n   GeneralSubtree: [\n    GeneralName: " + (this.name == null ? "" : this.name.toString()) + "\n    Minimum: " + this.minimum;
/*     */ 
/* 133 */     if (this.maximum == -1)
/* 134 */       str = str + "\t    Maximum: undefined";
/*     */     else
/* 136 */       str = str + "\t    Maximum: " + this.maximum;
/* 137 */     str = str + "    ]\n";
/* 138 */     return str;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 148 */     if (!(paramObject instanceof GeneralSubtree))
/* 149 */       return false;
/* 150 */     GeneralSubtree localGeneralSubtree = (GeneralSubtree)paramObject;
/* 151 */     if (this.name == null) {
/* 152 */       if (localGeneralSubtree.name != null) {
/* 153 */         return false;
/*     */       }
/*     */     }
/* 156 */     else if (!this.name.equals(localGeneralSubtree.name)) {
/* 157 */       return false;
/*     */     }
/* 159 */     if (this.minimum != localGeneralSubtree.minimum)
/* 160 */       return false;
/* 161 */     if (this.maximum != localGeneralSubtree.maximum)
/* 162 */       return false;
/* 163 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 172 */     if (this.myhash == -1) {
/* 173 */       this.myhash = 17;
/* 174 */       if (this.name != null) {
/* 175 */         this.myhash = (37 * this.myhash + this.name.hashCode());
/*     */       }
/* 177 */       if (this.minimum != 0) {
/* 178 */         this.myhash = (37 * this.myhash + this.minimum);
/*     */       }
/* 180 */       if (this.maximum != -1) {
/* 181 */         this.myhash = (37 * this.myhash + this.maximum);
/*     */       }
/*     */     }
/* 184 */     return this.myhash;
/*     */   }
/*     */ 
/*     */   public void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 193 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*     */ 
/* 195 */     this.name.encode(localDerOutputStream1);
/*     */     DerOutputStream localDerOutputStream2;
/* 197 */     if (this.minimum != 0) {
/* 198 */       localDerOutputStream2 = new DerOutputStream();
/* 199 */       localDerOutputStream2.putInteger(this.minimum);
/* 200 */       localDerOutputStream1.writeImplicit(DerValue.createTag((byte)-128, false, (byte)0), localDerOutputStream2);
/*     */     }
/*     */ 
/* 203 */     if (this.maximum != -1) {
/* 204 */       localDerOutputStream2 = new DerOutputStream();
/* 205 */       localDerOutputStream2.putInteger(this.maximum);
/* 206 */       localDerOutputStream1.writeImplicit(DerValue.createTag((byte)-128, false, (byte)1), localDerOutputStream2);
/*     */     }
/*     */ 
/* 209 */     paramDerOutputStream.write((byte)48, localDerOutputStream1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.GeneralSubtree
 * JD-Core Version:    0.6.2
 */