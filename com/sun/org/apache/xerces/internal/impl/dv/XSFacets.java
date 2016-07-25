/*     */ package com.sun.org.apache.xerces.internal.impl.dv;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class XSFacets
/*     */ {
/*     */   public int length;
/*     */   public int minLength;
/*     */   public int maxLength;
/*     */   public short whiteSpace;
/*     */   public int totalDigits;
/*     */   public int fractionDigits;
/*     */   public String pattern;
/*     */   public Vector enumeration;
/*     */   public Vector enumNSDecls;
/*     */   public String maxInclusive;
/*     */   public String maxExclusive;
/*     */   public String minInclusive;
/*     */   public String minExclusive;
/*     */   public XSAnnotation lengthAnnotation;
/*     */   public XSAnnotation minLengthAnnotation;
/*     */   public XSAnnotation maxLengthAnnotation;
/*     */   public XSAnnotation whiteSpaceAnnotation;
/*     */   public XSAnnotation totalDigitsAnnotation;
/*     */   public XSAnnotation fractionDigitsAnnotation;
/*     */   public XSObjectListImpl patternAnnotations;
/*     */   public XSObjectList enumAnnotations;
/*     */   public XSAnnotation maxInclusiveAnnotation;
/*     */   public XSAnnotation maxExclusiveAnnotation;
/*     */   public XSAnnotation minInclusiveAnnotation;
/*     */   public XSAnnotation minExclusiveAnnotation;
/*     */ 
/*     */   public void reset()
/*     */   {
/* 123 */     this.lengthAnnotation = null;
/* 124 */     this.minLengthAnnotation = null;
/* 125 */     this.maxLengthAnnotation = null;
/* 126 */     this.whiteSpaceAnnotation = null;
/* 127 */     this.totalDigitsAnnotation = null;
/* 128 */     this.fractionDigitsAnnotation = null;
/* 129 */     this.patternAnnotations = null;
/* 130 */     this.enumAnnotations = null;
/* 131 */     this.maxInclusiveAnnotation = null;
/* 132 */     this.maxExclusiveAnnotation = null;
/* 133 */     this.minInclusiveAnnotation = null;
/* 134 */     this.minExclusiveAnnotation = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.XSFacets
 * JD-Core Version:    0.6.2
 */