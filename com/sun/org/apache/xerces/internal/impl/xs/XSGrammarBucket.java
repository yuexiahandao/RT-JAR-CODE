/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class XSGrammarBucket
/*     */ {
/*  43 */   Map<String, SchemaGrammar> fGrammarRegistry = new HashMap();
/*  44 */   SchemaGrammar fNoNSGrammar = null;
/*     */ 
/*     */   public SchemaGrammar getGrammar(String namespace)
/*     */   {
/*  53 */     if (namespace == null)
/*  54 */       return this.fNoNSGrammar;
/*  55 */     return (SchemaGrammar)this.fGrammarRegistry.get(namespace);
/*     */   }
/*     */ 
/*     */   public void putGrammar(SchemaGrammar grammar)
/*     */   {
/*  66 */     if (grammar.getTargetNamespace() == null)
/*  67 */       this.fNoNSGrammar = grammar;
/*     */     else
/*  69 */       this.fGrammarRegistry.put(grammar.getTargetNamespace(), grammar);
/*     */   }
/*     */ 
/*     */   public boolean putGrammar(SchemaGrammar grammar, boolean deep)
/*     */   {
/*  84 */     SchemaGrammar sg = getGrammar(grammar.fTargetNamespace);
/*  85 */     if (sg != null)
/*     */     {
/*  87 */       return sg == grammar;
/*     */     }
/*     */ 
/*  90 */     if (!deep) {
/*  91 */       putGrammar(grammar);
/*  92 */       return true;
/*     */     }
/*     */ 
/*  98 */     Vector currGrammars = grammar.getImportedGrammars();
/*  99 */     if (currGrammars == null) {
/* 100 */       putGrammar(grammar);
/* 101 */       return true;
/*     */     }
/*     */ 
/* 104 */     Vector grammars = (Vector)currGrammars.clone();
/*     */ 
/* 108 */     for (int i = 0; i < grammars.size(); i++)
/*     */     {
/* 110 */       SchemaGrammar sg1 = (SchemaGrammar)grammars.elementAt(i);
/*     */ 
/* 112 */       SchemaGrammar sg2 = getGrammar(sg1.fTargetNamespace);
/* 113 */       if (sg2 == null)
/*     */       {
/* 115 */         Vector gs = sg1.getImportedGrammars();
/*     */ 
/* 118 */         if (gs != null) {
/* 119 */           for (int j = gs.size() - 1; j >= 0; j--) {
/* 120 */             sg2 = (SchemaGrammar)gs.elementAt(j);
/* 121 */             if (!grammars.contains(sg2)) {
/* 122 */               grammars.addElement(sg2);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 127 */       else if (sg2 != sg1) {
/* 128 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 133 */     putGrammar(grammar);
/* 134 */     for (int i = grammars.size() - 1; i >= 0; i--) {
/* 135 */       putGrammar((SchemaGrammar)grammars.elementAt(i));
/*     */     }
/* 137 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean putGrammar(SchemaGrammar grammar, boolean deep, boolean ignoreConflict)
/*     */   {
/* 153 */     if (!ignoreConflict) {
/* 154 */       return putGrammar(grammar, deep);
/*     */     }
/*     */ 
/* 158 */     SchemaGrammar sg = getGrammar(grammar.fTargetNamespace);
/* 159 */     if (sg == null) {
/* 160 */       putGrammar(grammar);
/*     */     }
/*     */ 
/* 164 */     if (!deep) {
/* 165 */       return true;
/*     */     }
/*     */ 
/* 171 */     Vector currGrammars = grammar.getImportedGrammars();
/* 172 */     if (currGrammars == null) {
/* 173 */       return true;
/*     */     }
/*     */ 
/* 176 */     Vector grammars = (Vector)currGrammars.clone();
/*     */ 
/* 180 */     for (int i = 0; i < grammars.size(); i++)
/*     */     {
/* 182 */       SchemaGrammar sg1 = (SchemaGrammar)grammars.elementAt(i);
/*     */ 
/* 184 */       SchemaGrammar sg2 = getGrammar(sg1.fTargetNamespace);
/* 185 */       if (sg2 == null)
/*     */       {
/* 187 */         Vector gs = sg1.getImportedGrammars();
/*     */ 
/* 190 */         if (gs != null)
/* 191 */           for (int j = gs.size() - 1; j >= 0; j--) {
/* 192 */             sg2 = (SchemaGrammar)gs.elementAt(j);
/* 193 */             if (!grammars.contains(sg2))
/* 194 */               grammars.addElement(sg2);
/*     */           }
/*     */       }
/*     */       else
/*     */       {
/* 199 */         grammars.remove(sg1);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 204 */     for (int i = grammars.size() - 1; i >= 0; i--) {
/* 205 */       putGrammar((SchemaGrammar)grammars.elementAt(i));
/*     */     }
/*     */ 
/* 208 */     return true;
/*     */   }
/*     */ 
/*     */   public SchemaGrammar[] getGrammars()
/*     */   {
/* 218 */     int count = this.fGrammarRegistry.size() + (this.fNoNSGrammar == null ? 0 : 1);
/* 219 */     SchemaGrammar[] grammars = new SchemaGrammar[count];
/*     */ 
/* 221 */     int i = 0;
/* 222 */     for (Map.Entry entry : this.fGrammarRegistry.entrySet()) {
/* 223 */       grammars[(i++)] = ((SchemaGrammar)entry.getValue());
/*     */     }
/*     */ 
/* 227 */     if (this.fNoNSGrammar != null)
/* 228 */       grammars[(count - 1)] = this.fNoNSGrammar;
/* 229 */     return grammars;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 237 */     this.fNoNSGrammar = null;
/* 238 */     this.fGrammarRegistry.clear();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XSGrammarBucket
 * JD-Core Version:    0.6.2
 */