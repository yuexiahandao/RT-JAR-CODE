/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.xs.SchemaDVFactoryImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl;
/*     */ 
/*     */ public final class XSDeclarationPool
/*     */ {
/*     */   private static final int CHUNK_SHIFT = 8;
/*     */   private static final int CHUNK_SIZE = 256;
/*     */   private static final int CHUNK_MASK = 255;
/*     */   private static final int INITIAL_CHUNK_COUNT = 4;
/*  52 */   private XSElementDecl[][] fElementDecl = new XSElementDecl[4][];
/*  53 */   private int fElementDeclIndex = 0;
/*     */ 
/*  56 */   private XSParticleDecl[][] fParticleDecl = new XSParticleDecl[4][];
/*  57 */   private int fParticleDeclIndex = 0;
/*     */ 
/*  60 */   private XSModelGroupImpl[][] fModelGroup = new XSModelGroupImpl[4][];
/*  61 */   private int fModelGroupIndex = 0;
/*     */ 
/*  64 */   private XSAttributeDecl[][] fAttrDecl = new XSAttributeDecl[4][];
/*  65 */   private int fAttrDeclIndex = 0;
/*     */ 
/*  68 */   private XSComplexTypeDecl[][] fCTDecl = new XSComplexTypeDecl[4][];
/*  69 */   private int fCTDeclIndex = 0;
/*     */ 
/*  72 */   private XSSimpleTypeDecl[][] fSTDecl = new XSSimpleTypeDecl[4][];
/*  73 */   private int fSTDeclIndex = 0;
/*     */ 
/*  76 */   private XSAttributeUseImpl[][] fAttributeUse = new XSAttributeUseImpl[4][];
/*  77 */   private int fAttributeUseIndex = 0;
/*     */   private SchemaDVFactoryImpl dvFactory;
/*     */ 
/*     */   public void setDVFactory(SchemaDVFactoryImpl dvFactory)
/*     */   {
/*  81 */     this.dvFactory = dvFactory;
/*     */   }
/*     */ 
/*     */   public final XSElementDecl getElementDecl() {
/*  85 */     int chunk = this.fElementDeclIndex >> 8;
/*  86 */     int index = this.fElementDeclIndex & 0xFF;
/*  87 */     ensureElementDeclCapacity(chunk);
/*  88 */     if (this.fElementDecl[chunk][index] == null)
/*  89 */       this.fElementDecl[chunk][index] = new XSElementDecl();
/*     */     else {
/*  91 */       this.fElementDecl[chunk][index].reset();
/*     */     }
/*  93 */     this.fElementDeclIndex += 1;
/*  94 */     return this.fElementDecl[chunk][index];
/*     */   }
/*     */ 
/*     */   public final XSAttributeDecl getAttributeDecl() {
/*  98 */     int chunk = this.fAttrDeclIndex >> 8;
/*  99 */     int index = this.fAttrDeclIndex & 0xFF;
/* 100 */     ensureAttrDeclCapacity(chunk);
/* 101 */     if (this.fAttrDecl[chunk][index] == null)
/* 102 */       this.fAttrDecl[chunk][index] = new XSAttributeDecl();
/*     */     else {
/* 104 */       this.fAttrDecl[chunk][index].reset();
/*     */     }
/* 106 */     this.fAttrDeclIndex += 1;
/* 107 */     return this.fAttrDecl[chunk][index];
/*     */   }
/*     */ 
/*     */   public final XSAttributeUseImpl getAttributeUse()
/*     */   {
/* 112 */     int chunk = this.fAttributeUseIndex >> 8;
/* 113 */     int index = this.fAttributeUseIndex & 0xFF;
/* 114 */     ensureAttributeUseCapacity(chunk);
/* 115 */     if (this.fAttributeUse[chunk][index] == null)
/* 116 */       this.fAttributeUse[chunk][index] = new XSAttributeUseImpl();
/*     */     else {
/* 118 */       this.fAttributeUse[chunk][index].reset();
/*     */     }
/* 120 */     this.fAttributeUseIndex += 1;
/* 121 */     return this.fAttributeUse[chunk][index];
/*     */   }
/*     */ 
/*     */   public final XSComplexTypeDecl getComplexTypeDecl()
/*     */   {
/* 126 */     int chunk = this.fCTDeclIndex >> 8;
/* 127 */     int index = this.fCTDeclIndex & 0xFF;
/* 128 */     ensureCTDeclCapacity(chunk);
/* 129 */     if (this.fCTDecl[chunk][index] == null)
/*     */     {
/* 131 */       this.fCTDecl[chunk][index] = new XSComplexTypeDecl();
/*     */     }
/* 133 */     else this.fCTDecl[chunk][index].reset();
/*     */ 
/* 135 */     this.fCTDeclIndex += 1;
/* 136 */     return this.fCTDecl[chunk][index];
/*     */   }
/*     */ 
/*     */   public final XSSimpleTypeDecl getSimpleTypeDecl() {
/* 140 */     int chunk = this.fSTDeclIndex >> 8;
/* 141 */     int index = this.fSTDeclIndex & 0xFF;
/* 142 */     ensureSTDeclCapacity(chunk);
/* 143 */     if (this.fSTDecl[chunk][index] == null)
/* 144 */       this.fSTDecl[chunk][index] = this.dvFactory.newXSSimpleTypeDecl();
/*     */     else {
/* 146 */       this.fSTDecl[chunk][index].reset();
/*     */     }
/* 148 */     this.fSTDeclIndex += 1;
/* 149 */     return this.fSTDecl[chunk][index];
/*     */   }
/*     */ 
/*     */   public final XSParticleDecl getParticleDecl()
/*     */   {
/* 154 */     int chunk = this.fParticleDeclIndex >> 8;
/* 155 */     int index = this.fParticleDeclIndex & 0xFF;
/* 156 */     ensureParticleDeclCapacity(chunk);
/* 157 */     if (this.fParticleDecl[chunk][index] == null)
/* 158 */       this.fParticleDecl[chunk][index] = new XSParticleDecl();
/*     */     else {
/* 160 */       this.fParticleDecl[chunk][index].reset();
/*     */     }
/* 162 */     this.fParticleDeclIndex += 1;
/* 163 */     return this.fParticleDecl[chunk][index];
/*     */   }
/*     */ 
/*     */   public final XSModelGroupImpl getModelGroup() {
/* 167 */     int chunk = this.fModelGroupIndex >> 8;
/* 168 */     int index = this.fModelGroupIndex & 0xFF;
/* 169 */     ensureModelGroupCapacity(chunk);
/* 170 */     if (this.fModelGroup[chunk][index] == null)
/* 171 */       this.fModelGroup[chunk][index] = new XSModelGroupImpl();
/*     */     else {
/* 173 */       this.fModelGroup[chunk][index].reset();
/*     */     }
/* 175 */     this.fModelGroupIndex += 1;
/* 176 */     return this.fModelGroup[chunk][index];
/*     */   }
/*     */ 
/*     */   private boolean ensureElementDeclCapacity(int chunk)
/*     */   {
/* 186 */     if (chunk >= this.fElementDecl.length)
/* 187 */       this.fElementDecl = resize(this.fElementDecl, this.fElementDecl.length * 2);
/* 188 */     else if (this.fElementDecl[chunk] != null) {
/* 189 */       return false;
/*     */     }
/*     */ 
/* 192 */     this.fElementDecl[chunk] = new XSElementDecl[256];
/* 193 */     return true;
/*     */   }
/*     */ 
/*     */   private static XSElementDecl[][] resize(XSElementDecl[][] array, int newsize) {
/* 197 */     XSElementDecl[][] newarray = new XSElementDecl[newsize][];
/* 198 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 199 */     return newarray;
/*     */   }
/*     */ 
/*     */   private boolean ensureParticleDeclCapacity(int chunk) {
/* 203 */     if (chunk >= this.fParticleDecl.length)
/* 204 */       this.fParticleDecl = resize(this.fParticleDecl, this.fParticleDecl.length * 2);
/* 205 */     else if (this.fParticleDecl[chunk] != null) {
/* 206 */       return false;
/*     */     }
/*     */ 
/* 209 */     this.fParticleDecl[chunk] = new XSParticleDecl[256];
/* 210 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean ensureModelGroupCapacity(int chunk) {
/* 214 */     if (chunk >= this.fModelGroup.length)
/* 215 */       this.fModelGroup = resize(this.fModelGroup, this.fModelGroup.length * 2);
/* 216 */     else if (this.fModelGroup[chunk] != null) {
/* 217 */       return false;
/*     */     }
/*     */ 
/* 220 */     this.fModelGroup[chunk] = new XSModelGroupImpl[256];
/* 221 */     return true;
/*     */   }
/*     */ 
/*     */   private static XSParticleDecl[][] resize(XSParticleDecl[][] array, int newsize) {
/* 225 */     XSParticleDecl[][] newarray = new XSParticleDecl[newsize][];
/* 226 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 227 */     return newarray;
/*     */   }
/*     */ 
/*     */   private static XSModelGroupImpl[][] resize(XSModelGroupImpl[][] array, int newsize) {
/* 231 */     XSModelGroupImpl[][] newarray = new XSModelGroupImpl[newsize][];
/* 232 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 233 */     return newarray;
/*     */   }
/*     */ 
/*     */   private boolean ensureAttrDeclCapacity(int chunk) {
/* 237 */     if (chunk >= this.fAttrDecl.length)
/* 238 */       this.fAttrDecl = resize(this.fAttrDecl, this.fAttrDecl.length * 2);
/* 239 */     else if (this.fAttrDecl[chunk] != null) {
/* 240 */       return false;
/*     */     }
/*     */ 
/* 243 */     this.fAttrDecl[chunk] = new XSAttributeDecl[256];
/* 244 */     return true;
/*     */   }
/*     */ 
/*     */   private static XSAttributeDecl[][] resize(XSAttributeDecl[][] array, int newsize) {
/* 248 */     XSAttributeDecl[][] newarray = new XSAttributeDecl[newsize][];
/* 249 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 250 */     return newarray;
/*     */   }
/*     */ 
/*     */   private boolean ensureAttributeUseCapacity(int chunk) {
/* 254 */     if (chunk >= this.fAttributeUse.length)
/* 255 */       this.fAttributeUse = resize(this.fAttributeUse, this.fAttributeUse.length * 2);
/* 256 */     else if (this.fAttributeUse[chunk] != null) {
/* 257 */       return false;
/*     */     }
/*     */ 
/* 260 */     this.fAttributeUse[chunk] = new XSAttributeUseImpl[256];
/* 261 */     return true;
/*     */   }
/*     */ 
/*     */   private static XSAttributeUseImpl[][] resize(XSAttributeUseImpl[][] array, int newsize) {
/* 265 */     XSAttributeUseImpl[][] newarray = new XSAttributeUseImpl[newsize][];
/* 266 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 267 */     return newarray;
/*     */   }
/*     */ 
/*     */   private boolean ensureSTDeclCapacity(int chunk) {
/* 271 */     if (chunk >= this.fSTDecl.length)
/* 272 */       this.fSTDecl = resize(this.fSTDecl, this.fSTDecl.length * 2);
/* 273 */     else if (this.fSTDecl[chunk] != null) {
/* 274 */       return false;
/*     */     }
/*     */ 
/* 277 */     this.fSTDecl[chunk] = new XSSimpleTypeDecl[256];
/* 278 */     return true;
/*     */   }
/*     */ 
/*     */   private static XSSimpleTypeDecl[][] resize(XSSimpleTypeDecl[][] array, int newsize) {
/* 282 */     XSSimpleTypeDecl[][] newarray = new XSSimpleTypeDecl[newsize][];
/* 283 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 284 */     return newarray;
/*     */   }
/*     */ 
/*     */   private boolean ensureCTDeclCapacity(int chunk)
/*     */   {
/* 289 */     if (chunk >= this.fCTDecl.length)
/* 290 */       this.fCTDecl = resize(this.fCTDecl, this.fCTDecl.length * 2);
/* 291 */     else if (this.fCTDecl[chunk] != null) {
/* 292 */       return false;
/*     */     }
/*     */ 
/* 295 */     this.fCTDecl[chunk] = new XSComplexTypeDecl[256];
/* 296 */     return true;
/*     */   }
/*     */ 
/*     */   private static XSComplexTypeDecl[][] resize(XSComplexTypeDecl[][] array, int newsize) {
/* 300 */     XSComplexTypeDecl[][] newarray = new XSComplexTypeDecl[newsize][];
/* 301 */     System.arraycopy(array, 0, newarray, 0, array.length);
/* 302 */     return newarray;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 308 */     this.fElementDeclIndex = 0;
/* 309 */     this.fParticleDeclIndex = 0;
/* 310 */     this.fModelGroupIndex = 0;
/* 311 */     this.fSTDeclIndex = 0;
/* 312 */     this.fCTDeclIndex = 0;
/* 313 */     this.fAttrDeclIndex = 0;
/* 314 */     this.fAttributeUseIndex = 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XSDeclarationPool
 * JD-Core Version:    0.6.2
 */