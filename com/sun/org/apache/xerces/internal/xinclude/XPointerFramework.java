/*     */ package com.sun.org.apache.xerces.internal.xinclude;
/*     */ 
/*     */ import java.util.Stack;
/*     */ 
/*     */ public class XPointerFramework
/*     */ {
/*     */   XPointerSchema[] fXPointerSchema;
/*     */   String[] fSchemaPointerName;
/*     */   String[] fSchemaPointerURI;
/*     */   String fSchemaPointer;
/*     */   String fCurrentSchemaPointer;
/*     */   Stack fSchemaNotAvailable;
/*  79 */   int fCountSchemaName = 0;
/*  80 */   int schemaLength = 0;
/*     */   XPointerSchema fDefaultXPointerSchema;
/*     */ 
/*     */   public XPointerFramework()
/*     */   {
/*  84 */     this(null);
/*     */   }
/*     */ 
/*     */   public XPointerFramework(XPointerSchema[] xpointerschema) {
/*  88 */     this.fXPointerSchema = xpointerschema;
/*  89 */     this.fSchemaNotAvailable = new Stack();
/*     */   }
/*     */ 
/*     */   public void reset() {
/*  93 */     this.fXPointerSchema = null;
/*  94 */     this.fXPointerSchema = null;
/*  95 */     this.fCountSchemaName = 0;
/*  96 */     this.schemaLength = 0;
/*  97 */     this.fSchemaPointerName = null;
/*  98 */     this.fSchemaPointerURI = null;
/*  99 */     this.fDefaultXPointerSchema = null;
/* 100 */     this.fCurrentSchemaPointer = null;
/*     */   }
/*     */ 
/*     */   public void setXPointerSchema(XPointerSchema[] xpointerschema) {
/* 104 */     this.fXPointerSchema = xpointerschema;
/*     */   }
/*     */ 
/*     */   public void setSchemaPointer(String schemaPointer) {
/* 108 */     this.fSchemaPointer = schemaPointer;
/*     */   }
/*     */ 
/*     */   public XPointerSchema getNextXPointerSchema() {
/* 112 */     int i = this.fCountSchemaName;
/* 113 */     if (this.fSchemaPointerName == null) {
/* 114 */       getSchemaNames();
/*     */     }
/* 116 */     if (this.fDefaultXPointerSchema == null) {
/* 117 */       getDefaultSchema();
/*     */     }
/* 119 */     if (this.fDefaultXPointerSchema.getXpointerSchemaName().equalsIgnoreCase(this.fSchemaPointerName[i])) {
/* 120 */       this.fDefaultXPointerSchema.reset();
/* 121 */       this.fDefaultXPointerSchema.setXPointerSchemaPointer(this.fSchemaPointerURI[i]);
/* 122 */       this.fCountSchemaName = (++i);
/* 123 */       return getDefaultSchema();
/*     */     }
/* 125 */     if (this.fXPointerSchema == null) {
/* 126 */       this.fCountSchemaName = (++i);
/* 127 */       return null;
/*     */     }
/*     */ 
/* 130 */     int fschemalength = this.fXPointerSchema.length;
/*     */ 
/* 132 */     for (; this.fSchemaPointerName[i] != null; i++) {
/* 133 */       for (int j = 0; j < fschemalength; j++) {
/* 134 */         if (this.fSchemaPointerName[i].equalsIgnoreCase(this.fXPointerSchema[j].getXpointerSchemaName())) {
/* 135 */           this.fXPointerSchema[j].setXPointerSchemaPointer(this.fSchemaPointerURI[i]);
/* 136 */           this.fCountSchemaName = (++i);
/* 137 */           return this.fXPointerSchema[j];
/*     */         }
/*     */       }
/*     */ 
/* 141 */       if (this.fSchemaNotAvailable == null) {
/* 142 */         this.fSchemaNotAvailable = new Stack();
/*     */       }
/* 144 */       this.fSchemaNotAvailable.push(this.fSchemaPointerName[i]);
/*     */     }
/* 146 */     return null;
/*     */   }
/*     */ 
/*     */   public XPointerSchema getDefaultSchema() {
/* 150 */     if (this.fDefaultXPointerSchema == null)
/* 151 */       this.fDefaultXPointerSchema = new XPointerElementHandler();
/* 152 */     return this.fDefaultXPointerSchema;
/*     */   }
/*     */ 
/*     */   public void getSchemaNames() {
/* 156 */     int count = 0;
/* 157 */     int index = 0; int lastindex = 0;
/* 158 */     int schemapointerindex = 0; int schemapointerURIindex = 0;
/*     */ 
/* 160 */     int length = this.fSchemaPointer.length();
/* 161 */     this.fSchemaPointerName = new String[5];
/* 162 */     this.fSchemaPointerURI = new String[5];
/*     */ 
/* 164 */     index = this.fSchemaPointer.indexOf('(');
/* 165 */     if (index <= 0) {
/* 166 */       return;
/*     */     }
/* 168 */     this.fSchemaPointerName[(schemapointerindex++)] = this.fSchemaPointer.substring(0, index++).trim();
/* 169 */     lastindex = index;
/* 170 */     String tempURI = null;
/* 171 */     count++;
/*     */ 
/* 173 */     while (index < length) {
/* 174 */       char c = this.fSchemaPointer.charAt(index);
/* 175 */       if (c == '(')
/* 176 */         count++;
/* 177 */       if (c == ')')
/* 178 */         count--;
/* 179 */       if (count == 0) {
/* 180 */         tempURI = this.fSchemaPointer.substring(lastindex, index).trim();
/* 181 */         this.fSchemaPointerURI[(schemapointerURIindex++)] = getEscapedURI(tempURI);
/* 182 */         lastindex = index;
/* 183 */         if ((index = this.fSchemaPointer.indexOf('(', lastindex)) != -1) {
/* 184 */           this.fSchemaPointerName[(schemapointerindex++)] = this.fSchemaPointer.substring(lastindex + 1, index).trim();
/* 185 */           count++;
/* 186 */           lastindex = index + 1;
/*     */         }
/*     */         else {
/* 189 */           index = lastindex;
/*     */         }
/*     */       }
/* 192 */       index++;
/*     */     }
/* 194 */     this.schemaLength = (schemapointerURIindex - 1);
/*     */   }
/*     */ 
/*     */   public String getEscapedURI(String URI) {
/* 198 */     return URI;
/*     */   }
/*     */ 
/*     */   public int getSchemaCount() {
/* 202 */     return this.schemaLength;
/*     */   }
/*     */ 
/*     */   public int getCurrentPointer() {
/* 206 */     return this.fCountSchemaName;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xinclude.XPointerFramework
 * JD-Core Version:    0.6.2
 */