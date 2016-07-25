/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSModelGroup;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ 
/*     */ public class XSModelGroupImpl
/*     */   implements XSModelGroup
/*     */ {
/*     */   public static final short MODELGROUP_CHOICE = 101;
/*     */   public static final short MODELGROUP_SEQUENCE = 102;
/*     */   public static final short MODELGROUP_ALL = 103;
/*     */   public short fCompositor;
/*  53 */   public XSParticleDecl[] fParticles = null;
/*  54 */   public int fParticleCount = 0;
/*     */ 
/*  57 */   public XSObjectList fAnnotations = null;
/*     */ 
/* 148 */   private String fDescription = null;
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  61 */     for (int i = 0; i < this.fParticleCount; i++) {
/*  62 */       if (!this.fParticles[i].isEmpty())
/*  63 */         return false;
/*     */     }
/*  65 */     return true;
/*     */   }
/*     */ 
/*     */   public int minEffectiveTotalRange()
/*     */   {
/*  76 */     if (this.fCompositor == 101) {
/*  77 */       return minEffectiveTotalRangeChoice();
/*     */     }
/*  79 */     return minEffectiveTotalRangeAllSeq();
/*     */   }
/*     */ 
/*     */   private int minEffectiveTotalRangeAllSeq()
/*     */   {
/*  84 */     int total = 0;
/*  85 */     for (int i = 0; i < this.fParticleCount; i++)
/*  86 */       total += this.fParticles[i].minEffectiveTotalRange();
/*  87 */     return total;
/*     */   }
/*     */ 
/*     */   private int minEffectiveTotalRangeChoice()
/*     */   {
/*  92 */     int min = 0;
/*  93 */     if (this.fParticleCount > 0) {
/*  94 */       min = this.fParticles[0].minEffectiveTotalRange();
/*     */     }
/*  96 */     for (int i = 1; i < this.fParticleCount; i++) {
/*  97 */       int one = this.fParticles[i].minEffectiveTotalRange();
/*  98 */       if (one < min) {
/*  99 */         min = one;
/*     */       }
/*     */     }
/* 102 */     return min;
/*     */   }
/*     */ 
/*     */   public int maxEffectiveTotalRange() {
/* 106 */     if (this.fCompositor == 101) {
/* 107 */       return maxEffectiveTotalRangeChoice();
/*     */     }
/* 109 */     return maxEffectiveTotalRangeAllSeq();
/*     */   }
/*     */ 
/*     */   private int maxEffectiveTotalRangeAllSeq()
/*     */   {
/* 115 */     int total = 0;
/* 116 */     for (int i = 0; i < this.fParticleCount; i++) {
/* 117 */       int one = this.fParticles[i].maxEffectiveTotalRange();
/* 118 */       if (one == -1)
/* 119 */         return -1;
/* 120 */       total += one;
/*     */     }
/* 122 */     return total;
/*     */   }
/*     */ 
/*     */   private int maxEffectiveTotalRangeChoice()
/*     */   {
/* 128 */     int max = 0;
/* 129 */     if (this.fParticleCount > 0) {
/* 130 */       max = this.fParticles[0].maxEffectiveTotalRange();
/* 131 */       if (max == -1) {
/* 132 */         return -1;
/*     */       }
/*     */     }
/* 135 */     for (int i = 1; i < this.fParticleCount; i++) {
/* 136 */       int one = this.fParticles[i].maxEffectiveTotalRange();
/* 137 */       if (one == -1)
/* 138 */         return -1;
/* 139 */       if (one > max)
/* 140 */         max = one;
/*     */     }
/* 142 */     return max;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 151 */     if (this.fDescription == null) {
/* 152 */       StringBuffer buffer = new StringBuffer();
/* 153 */       if (this.fCompositor == 103)
/* 154 */         buffer.append("all(");
/*     */       else
/* 156 */         buffer.append('(');
/* 157 */       if (this.fParticleCount > 0)
/* 158 */         buffer.append(this.fParticles[0].toString());
/* 159 */       for (int i = 1; i < this.fParticleCount; i++) {
/* 160 */         if (this.fCompositor == 101)
/* 161 */           buffer.append('|');
/*     */         else
/* 163 */           buffer.append(',');
/* 164 */         buffer.append(this.fParticles[i].toString());
/*     */       }
/*     */ 
/* 167 */       buffer.append(')');
/* 168 */       this.fDescription = buffer.toString();
/*     */     }
/* 170 */     return this.fDescription;
/*     */   }
/*     */ 
/*     */   public void reset() {
/* 174 */     this.fCompositor = 102;
/* 175 */     this.fParticles = null;
/* 176 */     this.fParticleCount = 0;
/* 177 */     this.fDescription = null;
/* 178 */     this.fAnnotations = null;
/*     */   }
/*     */ 
/*     */   public short getType()
/*     */   {
/* 185 */     return 7;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 193 */     return null;
/*     */   }
/*     */ 
/*     */   public String getNamespace()
/*     */   {
/* 202 */     return null;
/*     */   }
/*     */ 
/*     */   public short getCompositor()
/*     */   {
/* 210 */     if (this.fCompositor == 101)
/* 211 */       return 2;
/* 212 */     if (this.fCompositor == 102) {
/* 213 */       return 1;
/*     */     }
/* 215 */     return 3;
/*     */   }
/*     */ 
/*     */   public XSObjectList getParticles()
/*     */   {
/* 222 */     return new XSObjectListImpl(this.fParticles, this.fParticleCount);
/*     */   }
/*     */ 
/*     */   public XSAnnotation getAnnotation()
/*     */   {
/* 229 */     return this.fAnnotations != null ? (XSAnnotation)this.fAnnotations.item(0) : null;
/*     */   }
/*     */ 
/*     */   public XSObjectList getAnnotations()
/*     */   {
/* 236 */     return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
/*     */   }
/*     */ 
/*     */   public XSNamespaceItem getNamespaceItem()
/*     */   {
/* 243 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XSModelGroupImpl
 * JD-Core Version:    0.6.2
 */