/*     */ package com.sun.org.apache.xerces.internal.impl.xs.models;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaException;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSConstraints;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class XSAllCM
/*     */   implements XSCMValidator
/*     */ {
/*     */   private static final short STATE_START = 0;
/*     */   private static final short STATE_VALID = 1;
/*     */   private static final short STATE_CHILD = 1;
/*     */   private XSElementDecl[] fAllElements;
/*     */   private boolean[] fIsOptionalElement;
/*  58 */   private boolean fHasOptionalContent = false;
/*  59 */   private int fNumElements = 0;
/*     */ 
/*     */   public XSAllCM(boolean hasOptionalContent, int size)
/*     */   {
/*  66 */     this.fHasOptionalContent = hasOptionalContent;
/*  67 */     this.fAllElements = new XSElementDecl[size];
/*  68 */     this.fIsOptionalElement = new boolean[size];
/*     */   }
/*     */ 
/*     */   public void addElement(XSElementDecl element, boolean isOptional) {
/*  72 */     this.fAllElements[this.fNumElements] = element;
/*  73 */     this.fIsOptionalElement[this.fNumElements] = isOptional;
/*  74 */     this.fNumElements += 1;
/*     */   }
/*     */ 
/*     */   public int[] startContentModel()
/*     */   {
/*  91 */     int[] state = new int[this.fNumElements + 1];
/*     */ 
/*  93 */     for (int i = 0; i <= this.fNumElements; i++) {
/*  94 */       state[i] = 0;
/*     */     }
/*  96 */     return state;
/*     */   }
/*     */ 
/*     */   Object findMatchingDecl(QName elementName, SubstitutionGroupHandler subGroupHandler)
/*     */   {
/* 102 */     Object matchingDecl = null;
/* 103 */     for (int i = 0; i < this.fNumElements; i++) {
/* 104 */       matchingDecl = subGroupHandler.getMatchingElemDecl(elementName, this.fAllElements[i]);
/* 105 */       if (matchingDecl != null)
/*     */         break;
/*     */     }
/* 108 */     return matchingDecl;
/*     */   }
/*     */ 
/*     */   public Object oneTransition(QName elementName, int[] currentState, SubstitutionGroupHandler subGroupHandler)
/*     */   {
/* 121 */     if (currentState[0] < 0) {
/* 122 */       currentState[0] = -2;
/* 123 */       return findMatchingDecl(elementName, subGroupHandler);
/*     */     }
/*     */ 
/* 127 */     currentState[0] = 1;
/*     */ 
/* 129 */     Object matchingDecl = null;
/*     */ 
/* 131 */     for (int i = 0; i < this.fNumElements; i++)
/*     */     {
/* 134 */       if (currentState[(i + 1)] == 0)
/*     */       {
/* 136 */         matchingDecl = subGroupHandler.getMatchingElemDecl(elementName, this.fAllElements[i]);
/* 137 */         if (matchingDecl != null)
/*     */         {
/* 139 */           currentState[(i + 1)] = 1;
/* 140 */           return matchingDecl;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 145 */     currentState[0] = -1;
/* 146 */     return findMatchingDecl(elementName, subGroupHandler);
/*     */   }
/*     */ 
/*     */   public boolean endContentModel(int[] currentState)
/*     */   {
/* 158 */     int state = currentState[0];
/*     */ 
/* 160 */     if ((state == -1) || (state == -2)) {
/* 161 */       return false;
/*     */     }
/*     */ 
/* 166 */     if ((this.fHasOptionalContent) && (state == 0)) {
/* 167 */       return true;
/*     */     }
/*     */ 
/* 170 */     for (int i = 0; i < this.fNumElements; i++)
/*     */     {
/* 172 */       if ((this.fIsOptionalElement[i] == 0) && (currentState[(i + 1)] == 0)) {
/* 173 */         return false;
/*     */       }
/*     */     }
/* 176 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean checkUniqueParticleAttribution(SubstitutionGroupHandler subGroupHandler)
/*     */     throws XMLSchemaException
/*     */   {
/* 187 */     for (int i = 0; i < this.fNumElements; i++) {
/* 188 */       for (int j = i + 1; j < this.fNumElements; j++) {
/* 189 */         if (XSConstraints.overlapUPA(this.fAllElements[i], this.fAllElements[j], subGroupHandler))
/*     */         {
/* 191 */           throw new XMLSchemaException("cos-nonambig", new Object[] { this.fAllElements[i].toString(), this.fAllElements[j].toString() });
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 197 */     return false;
/*     */   }
/*     */ 
/*     */   public Vector whatCanGoHere(int[] state)
/*     */   {
/* 210 */     Vector ret = new Vector();
/* 211 */     for (int i = 0; i < this.fNumElements; i++)
/*     */     {
/* 214 */       if (state[(i + 1)] == 0)
/* 215 */         ret.addElement(this.fAllElements[i]);
/*     */     }
/* 217 */     return ret;
/*     */   }
/*     */ 
/*     */   public ArrayList checkMinMaxBounds() {
/* 221 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.models.XSAllCM
 * JD-Core Version:    0.6.2
 */