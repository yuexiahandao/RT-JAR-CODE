/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.Translet;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ 
/*     */ public abstract class AnyNodeCounter extends NodeCounter
/*     */ {
/*     */   public AnyNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator)
/*     */   {
/*  37 */     super(translet, document, iterator);
/*     */   }
/*     */ 
/*     */   public AnyNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator, boolean hasFrom)
/*     */   {
/*  44 */     super(translet, document, iterator, hasFrom);
/*     */   }
/*     */ 
/*     */   public NodeCounter setStartNode(int node) {
/*  48 */     this._node = node;
/*  49 */     this._nodeType = this._document.getExpandedTypeID(node);
/*  50 */     return this;
/*     */   }
/*     */ 
/*     */   public String getCounter()
/*     */   {
/*  55 */     if (this._value != -2147483648.0D)
/*     */     {
/*  57 */       if (this._value == 0.0D) return "0";
/*  58 */       if (Double.isNaN(this._value)) return "NaN";
/*  59 */       if ((this._value < 0.0D) && (Double.isInfinite(this._value))) return "-Infinity";
/*  60 */       if (Double.isInfinite(this._value)) return "Infinity";
/*  61 */       return formatNumbers((int)this._value);
/*     */     }
/*     */ 
/*  64 */     int next = this._node;
/*  65 */     int root = this._document.getDocument();
/*  66 */     int result = 0;
/*  67 */     while ((next >= root) && (!matchesFrom(next))) {
/*  68 */       if (matchesCount(next)) {
/*  69 */         result++;
/*     */       }
/*  71 */       next--;
/*     */     }
/*     */ 
/*  84 */     return formatNumbers(result);
/*     */   }
/*     */ 
/*     */   public static NodeCounter getDefaultNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator)
/*     */   {
/*  90 */     return new DefaultAnyNodeCounter(translet, document, iterator);
/*     */   }
/*     */ 
/*     */   static class DefaultAnyNodeCounter extends AnyNodeCounter
/*     */   {
/*     */     public DefaultAnyNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
/*  96 */       super(document, iterator);
/*     */     }
/*     */ 
/*     */     public String getCounter()
/*     */     {
/*     */       int result;
/*     */       int result;
/* 101 */       if (this._value != -2147483648.0D)
/*     */       {
/* 103 */         if (this._value == 0.0D) return "0";
/* 104 */         if (Double.isNaN(this._value)) return "NaN";
/* 105 */         if ((this._value < 0.0D) && (Double.isInfinite(this._value))) return "-Infinity";
/* 106 */         if (Double.isInfinite(this._value)) return "Infinity";
/* 107 */         result = (int)this._value;
/*     */       }
/*     */       else {
/* 110 */         int next = this._node;
/* 111 */         result = 0;
/* 112 */         int ntype = this._document.getExpandedTypeID(this._node);
/* 113 */         int root = this._document.getDocument();
/* 114 */         while (next >= 0) {
/* 115 */           if (ntype == this._document.getExpandedTypeID(next)) {
/* 116 */             result++;
/*     */           }
/*     */ 
/* 120 */           if (next == root)
/*     */           {
/*     */             break;
/*     */           }
/* 124 */           next--;
/*     */         }
/*     */       }
/*     */ 
/* 128 */       return formatNumbers(result);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.AnyNodeCounter
 * JD-Core Version:    0.6.2
 */