/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class SubstitutionGroupHandler
/*     */ {
/*  42 */   private static final XSElementDecl[] EMPTY_GROUP = new XSElementDecl[0];
/*     */   XSGrammarBucket fGrammarBucket;
/* 179 */   Hashtable fSubGroupsB = new Hashtable();
/* 180 */   private static final OneSubGroup[] EMPTY_VECTOR = new OneSubGroup[0];
/*     */ 
/* 182 */   Hashtable fSubGroups = new Hashtable();
/*     */ 
/*     */   public SubstitutionGroupHandler(XSGrammarBucket grammarBucket)
/*     */   {
/*  51 */     this.fGrammarBucket = grammarBucket;
/*     */   }
/*     */ 
/*     */   public XSElementDecl getMatchingElemDecl(QName element, XSElementDecl exemplar)
/*     */   {
/*  57 */     if ((element.localpart == exemplar.fName) && (element.uri == exemplar.fTargetNamespace))
/*     */     {
/*  59 */       return exemplar;
/*     */     }
/*     */ 
/*  64 */     if (exemplar.fScope != 1) {
/*  65 */       return null;
/*     */     }
/*     */ 
/*  68 */     if ((exemplar.fBlock & 0x4) != 0) {
/*  69 */       return null;
/*     */     }
/*     */ 
/*  72 */     SchemaGrammar sGrammar = this.fGrammarBucket.getGrammar(element.uri);
/*  73 */     if (sGrammar == null) {
/*  74 */       return null;
/*     */     }
/*     */ 
/*  77 */     XSElementDecl eDecl = sGrammar.getGlobalElementDecl(element.localpart);
/*  78 */     if (eDecl == null) {
/*  79 */       return null;
/*     */     }
/*     */ 
/*  82 */     if (substitutionGroupOK(eDecl, exemplar, exemplar.fBlock)) {
/*  83 */       return eDecl;
/*     */     }
/*  85 */     return null;
/*     */   }
/*     */ 
/*     */   protected boolean substitutionGroupOK(XSElementDecl element, XSElementDecl exemplar, short blockingConstraint)
/*     */   {
/*  93 */     if (element == exemplar) {
/*  94 */       return true;
/*     */     }
/*     */ 
/*  98 */     if ((blockingConstraint & 0x4) != 0) {
/*  99 */       return false;
/*     */     }
/*     */ 
/* 102 */     XSElementDecl subGroup = element.fSubGroup;
/* 103 */     while ((subGroup != null) && (subGroup != exemplar)) {
/* 104 */       subGroup = subGroup.fSubGroup;
/*     */     }
/*     */ 
/* 107 */     if (subGroup == null) {
/* 108 */       return false;
/*     */     }
/*     */ 
/* 113 */     return typeDerivationOK(element.fType, exemplar.fType, blockingConstraint);
/*     */   }
/*     */ 
/*     */   private boolean typeDerivationOK(XSTypeDefinition derived, XSTypeDefinition base, short blockingConstraint) {
/* 117 */     short devMethod = 0; short blockConstraint = blockingConstraint;
/*     */ 
/* 122 */     XSTypeDefinition type = derived;
/* 123 */     while ((type != base) && (type != SchemaGrammar.fAnyType)) {
/* 124 */       if (type.getTypeCategory() == 15) {
/* 125 */         devMethod = (short)(devMethod | ((XSComplexTypeDecl)type).fDerivedBy);
/*     */       }
/*     */       else {
/* 128 */         devMethod = (short)(devMethod | 0x2);
/*     */       }
/* 130 */       type = type.getBaseType();
/*     */ 
/* 133 */       if (type == null) {
/* 134 */         type = SchemaGrammar.fAnyType;
/*     */       }
/* 136 */       if (type.getTypeCategory() == 15) {
/* 137 */         blockConstraint = (short)(blockConstraint | ((XSComplexTypeDecl)type).fBlock);
/*     */       }
/*     */     }
/* 140 */     if (type != base)
/*     */     {
/* 142 */       if (base.getTypeCategory() == 16) {
/* 143 */         XSSimpleTypeDefinition st = (XSSimpleTypeDefinition)base;
/* 144 */         if (st.getVariety() == 3) {
/* 145 */           XSObjectList memberTypes = st.getMemberTypes();
/* 146 */           int length = memberTypes.getLength();
/* 147 */           for (int i = 0; i < length; i++) {
/* 148 */             if (typeDerivationOK(derived, (XSTypeDefinition)memberTypes.item(i), blockingConstraint)) {
/* 149 */               return true;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 154 */       return false;
/*     */     }
/* 156 */     if ((devMethod & blockConstraint) != 0) {
/* 157 */       return false;
/*     */     }
/* 159 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean inSubstitutionGroup(XSElementDecl element, XSElementDecl exemplar)
/*     */   {
/* 171 */     return substitutionGroupOK(element, exemplar, exemplar.fBlock);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 188 */     this.fSubGroupsB.clear();
/* 189 */     this.fSubGroups.clear();
/*     */   }
/*     */ 
/*     */   public void addSubstitutionGroup(XSElementDecl[] elements)
/*     */   {
/* 199 */     for (int i = elements.length - 1; i >= 0; i--) {
/* 200 */       XSElementDecl element = elements[i];
/* 201 */       XSElementDecl subHead = element.fSubGroup;
/*     */ 
/* 203 */       Vector subGroup = (Vector)this.fSubGroupsB.get(subHead);
/* 204 */       if (subGroup == null)
/*     */       {
/* 206 */         subGroup = new Vector();
/* 207 */         this.fSubGroupsB.put(subHead, subGroup);
/*     */       }
/*     */ 
/* 210 */       subGroup.addElement(element);
/*     */     }
/*     */   }
/*     */ 
/*     */   public XSElementDecl[] getSubstitutionGroup(XSElementDecl element)
/*     */   {
/* 224 */     Object subGroup = this.fSubGroups.get(element);
/* 225 */     if (subGroup != null) {
/* 226 */       return (XSElementDecl[])subGroup;
/*     */     }
/* 228 */     if ((element.fBlock & 0x4) != 0) {
/* 229 */       this.fSubGroups.put(element, EMPTY_GROUP);
/* 230 */       return EMPTY_GROUP;
/*     */     }
/*     */ 
/* 235 */     OneSubGroup[] groupB = getSubGroupB(element, new OneSubGroup());
/* 236 */     int len = groupB.length; int rlen = 0;
/* 237 */     XSElementDecl[] ret = new XSElementDecl[len];
/*     */ 
/* 240 */     for (int i = 0; i < len; i++) {
/* 241 */       if ((element.fBlock & groupB[i].dMethod) == 0) {
/* 242 */         ret[(rlen++)] = groupB[i].sub;
/*     */       }
/*     */     }
/* 245 */     if (rlen < len) {
/* 246 */       XSElementDecl[] ret1 = new XSElementDecl[rlen];
/* 247 */       System.arraycopy(ret, 0, ret1, 0, rlen);
/* 248 */       ret = ret1;
/*     */     }
/*     */ 
/* 251 */     this.fSubGroups.put(element, ret);
/*     */ 
/* 253 */     return ret;
/*     */   }
/*     */ 
/*     */   private OneSubGroup[] getSubGroupB(XSElementDecl element, OneSubGroup methods)
/*     */   {
/* 258 */     Object subGroup = this.fSubGroupsB.get(element);
/*     */ 
/* 261 */     if (subGroup == null) {
/* 262 */       this.fSubGroupsB.put(element, EMPTY_VECTOR);
/* 263 */       return EMPTY_VECTOR;
/*     */     }
/*     */ 
/* 267 */     if ((subGroup instanceof OneSubGroup[])) {
/* 268 */       return (OneSubGroup[])subGroup;
/*     */     }
/*     */ 
/* 271 */     Vector group = (Vector)subGroup; Vector newGroup = new Vector();
/*     */ 
/* 276 */     for (int i = group.size() - 1; i >= 0; i--)
/*     */     {
/* 278 */       XSElementDecl sub = (XSElementDecl)group.elementAt(i);
/* 279 */       if (getDBMethods(sub.fType, element.fType, methods))
/*     */       {
/* 282 */         short dMethod = methods.dMethod;
/* 283 */         short bMethod = methods.bMethod;
/*     */ 
/* 285 */         newGroup.addElement(new OneSubGroup(sub, methods.dMethod, methods.bMethod));
/*     */ 
/* 287 */         OneSubGroup[] group1 = getSubGroupB(sub, methods);
/* 288 */         for (int j = group1.length - 1; j >= 0; j--)
/*     */         {
/* 290 */           short dSubMethod = (short)(dMethod | group1[j].dMethod);
/* 291 */           short bSubMethod = (short)(bMethod | group1[j].bMethod);
/*     */ 
/* 293 */           if ((dSubMethod & bSubMethod) == 0)
/*     */           {
/* 295 */             newGroup.addElement(new OneSubGroup(group1[j].sub, dSubMethod, bSubMethod));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 299 */     OneSubGroup[] ret = new OneSubGroup[newGroup.size()];
/* 300 */     for (int i = newGroup.size() - 1; i >= 0; i--) {
/* 301 */       ret[i] = ((OneSubGroup)newGroup.elementAt(i));
/*     */     }
/*     */ 
/* 304 */     this.fSubGroupsB.put(element, ret);
/*     */ 
/* 306 */     return ret;
/*     */   }
/*     */ 
/*     */   private boolean getDBMethods(XSTypeDefinition typed, XSTypeDefinition typeb, OneSubGroup methods)
/*     */   {
/* 311 */     short dMethod = 0; short bMethod = 0;
/* 312 */     while ((typed != typeb) && (typed != SchemaGrammar.fAnyType)) {
/* 313 */       if (typed.getTypeCategory() == 15)
/* 314 */         dMethod = (short)(dMethod | ((XSComplexTypeDecl)typed).fDerivedBy);
/*     */       else
/* 316 */         dMethod = (short)(dMethod | 0x2);
/* 317 */       typed = typed.getBaseType();
/*     */ 
/* 320 */       if (typed == null)
/* 321 */         typed = SchemaGrammar.fAnyType;
/* 322 */       if (typed.getTypeCategory() == 15) {
/* 323 */         bMethod = (short)(bMethod | ((XSComplexTypeDecl)typed).fBlock);
/*     */       }
/*     */     }
/* 326 */     if ((typed != typeb) || ((dMethod & bMethod) != 0)) {
/* 327 */       return false;
/*     */     }
/*     */ 
/* 330 */     methods.dMethod = dMethod;
/* 331 */     methods.bMethod = bMethod;
/* 332 */     return true; } 
/*     */   private static final class OneSubGroup { XSElementDecl sub;
/*     */     short dMethod;
/*     */     short bMethod;
/*     */ 
/*     */     OneSubGroup() {  } 
/* 339 */     OneSubGroup(XSElementDecl sub, short dMethod, short bMethod) { this.sub = sub;
/* 340 */       this.dMethod = dMethod;
/* 341 */       this.bMethod = bMethod;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler
 * JD-Core Version:    0.6.2
 */