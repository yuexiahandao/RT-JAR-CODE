/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSParticle;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTerm;
/*     */ 
/*     */ public class XSParticleDecl
/*     */   implements XSParticle
/*     */ {
/*     */   public static final short PARTICLE_EMPTY = 0;
/*     */   public static final short PARTICLE_ELEMENT = 1;
/*     */   public static final short PARTICLE_WILDCARD = 2;
/*     */   public static final short PARTICLE_MODELGROUP = 3;
/*     */   public static final short PARTICLE_ZERO_OR_MORE = 4;
/*     */   public static final short PARTICLE_ZERO_OR_ONE = 5;
/*     */   public static final short PARTICLE_ONE_OR_MORE = 6;
/*  51 */   public short fType = 0;
/*     */ 
/*  57 */   public XSTerm fValue = null;
/*     */ 
/*  60 */   public int fMinOccurs = 1;
/*     */ 
/*  62 */   public int fMaxOccurs = 1;
/*     */ 
/*  64 */   public XSObjectList fAnnotations = null;
/*     */ 
/* 131 */   private String fDescription = null;
/*     */ 
/*     */   public XSParticleDecl makeClone()
/*     */   {
/*  68 */     XSParticleDecl particle = new XSParticleDecl();
/*  69 */     particle.fType = this.fType;
/*  70 */     particle.fMinOccurs = this.fMinOccurs;
/*  71 */     particle.fMaxOccurs = this.fMaxOccurs;
/*  72 */     particle.fDescription = this.fDescription;
/*  73 */     particle.fValue = this.fValue;
/*  74 */     particle.fAnnotations = this.fAnnotations;
/*  75 */     return particle;
/*     */   }
/*     */ 
/*     */   public boolean emptiable()
/*     */   {
/*  83 */     return minEffectiveTotalRange() == 0;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  88 */     if (this.fType == 0)
/*  89 */       return true;
/*  90 */     if ((this.fType == 1) || (this.fType == 2)) {
/*  91 */       return false;
/*     */     }
/*  93 */     return ((XSModelGroupImpl)this.fValue).isEmpty();
/*     */   }
/*     */ 
/*     */   public int minEffectiveTotalRange()
/*     */   {
/* 104 */     if (this.fType == 0) {
/* 105 */       return 0;
/*     */     }
/* 107 */     if (this.fType == 3) {
/* 108 */       return ((XSModelGroupImpl)this.fValue).minEffectiveTotalRange() * this.fMinOccurs;
/*     */     }
/* 110 */     return this.fMinOccurs;
/*     */   }
/*     */ 
/*     */   public int maxEffectiveTotalRange() {
/* 114 */     if (this.fType == 0) {
/* 115 */       return 0;
/*     */     }
/* 117 */     if (this.fType == 3) {
/* 118 */       int max = ((XSModelGroupImpl)this.fValue).maxEffectiveTotalRange();
/* 119 */       if (max == -1)
/* 120 */         return -1;
/* 121 */       if ((max != 0) && (this.fMaxOccurs == -1))
/* 122 */         return -1;
/* 123 */       return max * this.fMaxOccurs;
/*     */     }
/* 125 */     return this.fMaxOccurs;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 133 */     if (this.fDescription == null) {
/* 134 */       StringBuffer buffer = new StringBuffer();
/* 135 */       appendParticle(buffer);
/* 136 */       if (((this.fMinOccurs != 0) || (this.fMaxOccurs != 0)) && ((this.fMinOccurs != 1) || (this.fMaxOccurs != 1)))
/*     */       {
/* 138 */         buffer.append('{').append(this.fMinOccurs);
/* 139 */         if (this.fMaxOccurs == -1)
/* 140 */           buffer.append("-UNBOUNDED");
/* 141 */         else if (this.fMinOccurs != this.fMaxOccurs)
/* 142 */           buffer.append('-').append(this.fMaxOccurs);
/* 143 */         buffer.append('}');
/*     */       }
/* 145 */       this.fDescription = buffer.toString();
/*     */     }
/* 147 */     return this.fDescription;
/*     */   }
/*     */ 
/*     */   void appendParticle(StringBuffer buffer)
/*     */   {
/* 155 */     switch (this.fType) {
/*     */     case 0:
/* 157 */       buffer.append("EMPTY");
/* 158 */       break;
/*     */     case 1:
/* 160 */       buffer.append(this.fValue.toString());
/* 161 */       break;
/*     */     case 2:
/* 163 */       buffer.append('(');
/* 164 */       buffer.append(this.fValue.toString());
/* 165 */       buffer.append(')');
/* 166 */       break;
/*     */     case 3:
/* 168 */       buffer.append(this.fValue.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 174 */     this.fType = 0;
/* 175 */     this.fValue = null;
/* 176 */     this.fMinOccurs = 1;
/* 177 */     this.fMaxOccurs = 1;
/* 178 */     this.fDescription = null;
/* 179 */     this.fAnnotations = null;
/*     */   }
/*     */ 
/*     */   public short getType()
/*     */   {
/* 186 */     return 8;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 194 */     return null;
/*     */   }
/*     */ 
/*     */   public String getNamespace()
/*     */   {
/* 203 */     return null;
/*     */   }
/*     */ 
/*     */   public int getMinOccurs()
/*     */   {
/* 210 */     return this.fMinOccurs;
/*     */   }
/*     */ 
/*     */   public boolean getMaxOccursUnbounded()
/*     */   {
/* 217 */     return this.fMaxOccurs == -1;
/*     */   }
/*     */ 
/*     */   public int getMaxOccurs()
/*     */   {
/* 224 */     return this.fMaxOccurs;
/*     */   }
/*     */ 
/*     */   public XSTerm getTerm()
/*     */   {
/* 231 */     return this.fValue;
/*     */   }
/*     */ 
/*     */   public XSNamespaceItem getNamespaceItem()
/*     */   {
/* 238 */     return null;
/*     */   }
/*     */ 
/*     */   public XSObjectList getAnnotations()
/*     */   {
/* 245 */     return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl
 * JD-Core Version:    0.6.2
 */