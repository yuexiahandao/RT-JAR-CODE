/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.Translet;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ 
/*     */ public abstract class SingleNodeCounter extends NodeCounter
/*     */ {
/*  37 */   private static final int[] EmptyArray = new int[0];
/*  38 */   DTMAxisIterator _countSiblings = null;
/*     */ 
/*     */   public SingleNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator)
/*     */   {
/*  43 */     super(translet, document, iterator);
/*     */   }
/*     */ 
/*     */   public SingleNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator, boolean hasFrom)
/*     */   {
/*  50 */     super(translet, document, iterator, hasFrom);
/*     */   }
/*     */ 
/*     */   public NodeCounter setStartNode(int node) {
/*  54 */     this._node = node;
/*  55 */     this._nodeType = this._document.getExpandedTypeID(node);
/*  56 */     this._countSiblings = this._document.getAxisIterator(12);
/*  57 */     return this;
/*     */   }
/*     */ 
/*     */   public String getCounter()
/*     */   {
/*     */     int result;
/*     */     int result;
/*  62 */     if (this._value != -2147483648.0D)
/*     */     {
/*  64 */       if (this._value == 0.0D) return "0";
/*  65 */       if (Double.isNaN(this._value)) return "NaN";
/*  66 */       if ((this._value < 0.0D) && (Double.isInfinite(this._value))) return "-Infinity";
/*  67 */       if (Double.isInfinite(this._value)) return "Infinity";
/*  68 */       result = (int)this._value;
/*     */     }
/*     */     else {
/*  71 */       int next = this._node;
/*  72 */       result = 0;
/*  73 */       boolean matchesCount = matchesCount(next);
/*     */ 
/*  75 */       if (!matchesCount) {
/*  76 */         while (((next = this._document.getParent(next)) > -1) && 
/*  77 */           (!matchesCount(next)))
/*     */         {
/*  80 */           if (matchesFrom(next)) {
/*  81 */             next = -1;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*  87 */       if (next != -1) {
/*  88 */         int from = next;
/*     */ 
/*  90 */         if ((!matchesCount) && (this._hasFrom))
/*     */         {
/*  92 */           while ((from = this._document.getParent(from)) > -1) {
/*  93 */             if (matchesFrom(from)) {
/*  94 */               break;
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 100 */         if (from != -1) {
/* 101 */           this._countSiblings.setStartNode(next);
/*     */           do
/* 103 */             if (matchesCount(next)) result++;
/* 104 */           while ((next = this._countSiblings.next()) != -1);
/*     */ 
/* 106 */           return formatNumbers(result);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 111 */       return formatNumbers(EmptyArray);
/*     */     }
/* 113 */     return formatNumbers(result);
/*     */   }
/*     */ 
/*     */   public static NodeCounter getDefaultNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator)
/*     */   {
/* 119 */     return new DefaultSingleNodeCounter(translet, document, iterator);
/*     */   }
/*     */ 
/*     */   static class DefaultSingleNodeCounter extends SingleNodeCounter
/*     */   {
/*     */     public DefaultSingleNodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
/* 125 */       super(document, iterator);
/*     */     }
/*     */ 
/*     */     public NodeCounter setStartNode(int node) {
/* 129 */       this._node = node;
/* 130 */       this._nodeType = this._document.getExpandedTypeID(node);
/* 131 */       this._countSiblings = this._document.getTypedAxisIterator(12, this._document.getExpandedTypeID(node));
/*     */ 
/* 134 */       return this;
/*     */     }
/*     */ 
/*     */     public String getCounter()
/*     */     {
/*     */       int result;
/*     */       int result;
/* 139 */       if (this._value != -2147483648.0D)
/*     */       {
/* 141 */         if (this._value == 0.0D) return "0";
/* 142 */         if (Double.isNaN(this._value)) return "NaN";
/* 143 */         if ((this._value < 0.0D) && (Double.isInfinite(this._value))) return "-Infinity";
/* 144 */         if (Double.isInfinite(this._value)) return "Infinity";
/* 145 */         result = (int)this._value;
/*     */       }
/*     */       else
/*     */       {
/* 149 */         result = 1;
/* 150 */         this._countSiblings.setStartNode(this._node);
/*     */         int next;
/* 151 */         while ((next = this._countSiblings.next()) != -1) {
/* 152 */           result++;
/*     */         }
/*     */       }
/* 155 */       return formatNumbers(result);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.SingleNodeCounter
 * JD-Core Version:    0.6.2
 */