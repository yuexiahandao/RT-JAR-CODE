/*     */ package com.sun.org.apache.xml.internal.security.c14n.implementations;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class NameSpaceSymbTable
/*     */ {
/*     */   SymbMap symb;
/*  45 */   int nameSpaces = 0;
/*     */   List level;
/*  48 */   boolean cloned = true;
/*     */   static final String XMLNS = "xmlns";
/*  50 */   static final SymbMap initialMap = new SymbMap();
/*     */ 
/*     */   public NameSpaceSymbTable()
/*     */   {
/*  60 */     this.level = new ArrayList(10);
/*     */ 
/*  62 */     this.symb = ((SymbMap)initialMap.clone());
/*     */   }
/*     */ 
/*     */   public void getUnrenderedNodes(Collection paramCollection)
/*     */   {
/*  72 */     Iterator localIterator = this.symb.entrySet().iterator();
/*  73 */     while (localIterator.hasNext()) {
/*  74 */       NameSpaceSymbEntry localNameSpaceSymbEntry = (NameSpaceSymbEntry)localIterator.next();
/*     */ 
/*  76 */       if ((!localNameSpaceSymbEntry.rendered) && (localNameSpaceSymbEntry.n != null)) {
/*  77 */         localNameSpaceSymbEntry = (NameSpaceSymbEntry)localNameSpaceSymbEntry.clone();
/*  78 */         needsClone();
/*  79 */         this.symb.put(localNameSpaceSymbEntry.prefix, localNameSpaceSymbEntry);
/*  80 */         localNameSpaceSymbEntry.lastrendered = localNameSpaceSymbEntry.uri;
/*  81 */         localNameSpaceSymbEntry.rendered = true;
/*     */ 
/*  83 */         paramCollection.add(localNameSpaceSymbEntry.n);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void outputNodePush()
/*     */   {
/*  94 */     this.nameSpaces += 1;
/*  95 */     push();
/*     */   }
/*     */ 
/*     */   public void outputNodePop()
/*     */   {
/* 102 */     this.nameSpaces -= 1;
/* 103 */     pop();
/*     */   }
/*     */ 
/*     */   public void push()
/*     */   {
/* 112 */     this.level.add(null);
/* 113 */     this.cloned = false;
/*     */   }
/*     */ 
/*     */   public void pop()
/*     */   {
/* 121 */     int i = this.level.size() - 1;
/* 122 */     Object localObject = this.level.remove(i);
/* 123 */     if (localObject != null) {
/* 124 */       this.symb = ((SymbMap)localObject);
/* 125 */       if (i == 0)
/* 126 */         this.cloned = false;
/*     */       else
/* 128 */         this.cloned = (this.level.get(i - 1) != this.symb);
/*     */     } else {
/* 130 */       this.cloned = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   final void needsClone()
/*     */   {
/* 137 */     if (!this.cloned) {
/* 138 */       this.level.set(this.level.size() - 1, this.symb);
/* 139 */       this.symb = ((SymbMap)this.symb.clone());
/* 140 */       this.cloned = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Attr getMapping(String paramString)
/*     */   {
/* 152 */     NameSpaceSymbEntry localNameSpaceSymbEntry = this.symb.get(paramString);
/* 153 */     if (localNameSpaceSymbEntry == null)
/*     */     {
/* 155 */       return null;
/*     */     }
/* 157 */     if (localNameSpaceSymbEntry.rendered)
/*     */     {
/* 159 */       return null;
/*     */     }
/*     */ 
/* 162 */     localNameSpaceSymbEntry = (NameSpaceSymbEntry)localNameSpaceSymbEntry.clone();
/* 163 */     needsClone();
/* 164 */     this.symb.put(paramString, localNameSpaceSymbEntry);
/* 165 */     localNameSpaceSymbEntry.rendered = true;
/* 166 */     localNameSpaceSymbEntry.level = this.nameSpaces;
/* 167 */     localNameSpaceSymbEntry.lastrendered = localNameSpaceSymbEntry.uri;
/*     */ 
/* 169 */     return localNameSpaceSymbEntry.n;
/*     */   }
/*     */ 
/*     */   public Attr getMappingWithoutRendered(String paramString)
/*     */   {
/* 179 */     NameSpaceSymbEntry localNameSpaceSymbEntry = this.symb.get(paramString);
/* 180 */     if (localNameSpaceSymbEntry == null) {
/* 181 */       return null;
/*     */     }
/* 183 */     if (localNameSpaceSymbEntry.rendered) {
/* 184 */       return null;
/*     */     }
/* 186 */     return localNameSpaceSymbEntry.n;
/*     */   }
/*     */ 
/*     */   public boolean addMapping(String paramString1, String paramString2, Attr paramAttr)
/*     */   {
/* 197 */     NameSpaceSymbEntry localNameSpaceSymbEntry1 = this.symb.get(paramString1);
/* 198 */     if ((localNameSpaceSymbEntry1 != null) && (paramString2.equals(localNameSpaceSymbEntry1.uri)))
/*     */     {
/* 200 */       return false;
/*     */     }
/*     */ 
/* 203 */     NameSpaceSymbEntry localNameSpaceSymbEntry2 = new NameSpaceSymbEntry(paramString2, paramAttr, false, paramString1);
/* 204 */     needsClone();
/* 205 */     this.symb.put(paramString1, localNameSpaceSymbEntry2);
/* 206 */     if (localNameSpaceSymbEntry1 != null)
/*     */     {
/* 209 */       localNameSpaceSymbEntry2.lastrendered = localNameSpaceSymbEntry1.lastrendered;
/* 210 */       if ((localNameSpaceSymbEntry1.lastrendered != null) && (localNameSpaceSymbEntry1.lastrendered.equals(paramString2)))
/*     */       {
/* 212 */         localNameSpaceSymbEntry2.rendered = true;
/*     */       }
/*     */     }
/* 215 */     return true;
/*     */   }
/*     */ 
/*     */   public Node addMappingAndRender(String paramString1, String paramString2, Attr paramAttr)
/*     */   {
/* 227 */     NameSpaceSymbEntry localNameSpaceSymbEntry1 = this.symb.get(paramString1);
/*     */ 
/* 229 */     if ((localNameSpaceSymbEntry1 != null) && (paramString2.equals(localNameSpaceSymbEntry1.uri))) {
/* 230 */       if (!localNameSpaceSymbEntry1.rendered) {
/* 231 */         localNameSpaceSymbEntry1 = (NameSpaceSymbEntry)localNameSpaceSymbEntry1.clone();
/* 232 */         needsClone();
/* 233 */         this.symb.put(paramString1, localNameSpaceSymbEntry1);
/* 234 */         localNameSpaceSymbEntry1.lastrendered = paramString2;
/* 235 */         localNameSpaceSymbEntry1.rendered = true;
/* 236 */         return localNameSpaceSymbEntry1.n;
/*     */       }
/* 238 */       return null;
/*     */     }
/*     */ 
/* 241 */     NameSpaceSymbEntry localNameSpaceSymbEntry2 = new NameSpaceSymbEntry(paramString2, paramAttr, true, paramString1);
/* 242 */     localNameSpaceSymbEntry2.lastrendered = paramString2;
/* 243 */     needsClone();
/* 244 */     this.symb.put(paramString1, localNameSpaceSymbEntry2);
/* 245 */     if (localNameSpaceSymbEntry1 != null)
/*     */     {
/* 247 */       if ((localNameSpaceSymbEntry1.lastrendered != null) && (localNameSpaceSymbEntry1.lastrendered.equals(paramString2))) {
/* 248 */         localNameSpaceSymbEntry2.rendered = true;
/* 249 */         return null;
/*     */       }
/*     */     }
/* 252 */     return localNameSpaceSymbEntry2.n;
/*     */   }
/*     */ 
/*     */   public int getLevel()
/*     */   {
/* 257 */     return this.level.size();
/*     */   }
/*     */ 
/*     */   public void removeMapping(String paramString) {
/* 261 */     NameSpaceSymbEntry localNameSpaceSymbEntry = this.symb.get(paramString);
/*     */ 
/* 263 */     if (localNameSpaceSymbEntry != null) {
/* 264 */       needsClone();
/* 265 */       this.symb.put(paramString, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeMappingIfNotRender(String paramString) {
/* 270 */     NameSpaceSymbEntry localNameSpaceSymbEntry = this.symb.get(paramString);
/*     */ 
/* 272 */     if ((localNameSpaceSymbEntry != null) && (!localNameSpaceSymbEntry.rendered)) {
/* 273 */       needsClone();
/* 274 */       this.symb.put(paramString, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean removeMappingIfRender(String paramString) {
/* 279 */     NameSpaceSymbEntry localNameSpaceSymbEntry = this.symb.get(paramString);
/*     */ 
/* 281 */     if ((localNameSpaceSymbEntry != null) && (localNameSpaceSymbEntry.rendered)) {
/* 282 */       needsClone();
/* 283 */       this.symb.put(paramString, null);
/*     */     }
/* 285 */     return false;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  52 */     NameSpaceSymbEntry localNameSpaceSymbEntry = new NameSpaceSymbEntry("", null, true, "xmlns");
/*  53 */     localNameSpaceSymbEntry.lastrendered = "";
/*  54 */     initialMap.put("xmlns", localNameSpaceSymbEntry);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.c14n.implementations.NameSpaceSymbTable
 * JD-Core Version:    0.6.2
 */