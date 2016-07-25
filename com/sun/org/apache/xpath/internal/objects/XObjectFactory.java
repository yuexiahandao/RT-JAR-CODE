/*     */ package com.sun.org.apache.xpath.internal.objects;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMIterator;
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.axes.OneStepIterator;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.traversal.NodeIterator;
/*     */ 
/*     */ public class XObjectFactory
/*     */ {
/*     */   public static XObject create(Object val)
/*     */   {
/*     */     XObject result;
/*     */     XObject result;
/*  50 */     if ((val instanceof XObject))
/*     */     {
/*  52 */       result = (XObject)val;
/*     */     }
/*     */     else
/*     */     {
/*     */       XObject result;
/*  54 */       if ((val instanceof String))
/*     */       {
/*  56 */         result = new XString((String)val);
/*     */       }
/*     */       else
/*     */       {
/*     */         XObject result;
/*  58 */         if ((val instanceof Boolean))
/*     */         {
/*  60 */           result = new XBoolean((Boolean)val);
/*     */         }
/*     */         else
/*     */         {
/*     */           XObject result;
/*  62 */           if ((val instanceof Double))
/*     */           {
/*  64 */             result = new XNumber((Double)val);
/*     */           }
/*     */           else
/*     */           {
/*  68 */             result = new XObject(val);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  71 */     return result;
/*     */   }
/*     */ 
/*     */   public static XObject create(Object val, XPathContext xctxt)
/*     */   {
/*     */     XObject result;
/*     */     XObject result;
/*  89 */     if ((val instanceof XObject))
/*     */     {
/*  91 */       result = (XObject)val;
/*     */     }
/*     */     else
/*     */     {
/*     */       XObject result;
/*  93 */       if ((val instanceof String))
/*     */       {
/*  95 */         result = new XString((String)val);
/*     */       }
/*     */       else
/*     */       {
/*     */         XObject result;
/*  97 */         if ((val instanceof Boolean))
/*     */         {
/*  99 */           result = new XBoolean((Boolean)val);
/*     */         }
/*     */         else
/*     */         {
/*     */           XObject result;
/* 101 */           if ((val instanceof Number))
/*     */           {
/* 103 */             result = new XNumber((Number)val);
/*     */           }
/* 105 */           else if ((val instanceof DTM))
/*     */           {
/* 107 */             DTM dtm = (DTM)val;
/*     */             try
/*     */             {
/* 110 */               int dtmRoot = dtm.getDocument();
/* 111 */               DTMAxisIterator iter = dtm.getAxisIterator(13);
/* 112 */               iter.setStartNode(dtmRoot);
/* 113 */               DTMIterator iterator = new OneStepIterator(iter, 13);
/* 114 */               iterator.setRoot(dtmRoot, xctxt);
/* 115 */               result = new XNodeSet(iterator);
/*     */             }
/*     */             catch (Exception ex)
/*     */             {
/*     */               XObject result;
/* 119 */               throw new WrappedRuntimeException(ex);
/*     */             }
/*     */           }
/* 122 */           else if ((val instanceof DTMAxisIterator))
/*     */           {
/* 124 */             DTMAxisIterator iter = (DTMAxisIterator)val;
/*     */             try
/*     */             {
/* 127 */               DTMIterator iterator = new OneStepIterator(iter, 13);
/* 128 */               iterator.setRoot(iter.getStartNode(), xctxt);
/* 129 */               result = new XNodeSet(iterator);
/*     */             }
/*     */             catch (Exception ex)
/*     */             {
/*     */               XObject result;
/* 133 */               throw new WrappedRuntimeException(ex);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/*     */             XObject result;
/* 136 */             if ((val instanceof DTMIterator))
/*     */             {
/* 138 */               result = new XNodeSet((DTMIterator)val);
/*     */             }
/*     */             else
/*     */             {
/*     */               XObject result;
/* 142 */               if ((val instanceof Node))
/*     */               {
/* 144 */                 result = new XNodeSetForDOM((Node)val, xctxt);
/*     */               }
/*     */               else
/*     */               {
/*     */                 XObject result;
/* 148 */                 if ((val instanceof NodeList))
/*     */                 {
/* 150 */                   result = new XNodeSetForDOM((NodeList)val, xctxt);
/*     */                 }
/*     */                 else
/*     */                 {
/*     */                   XObject result;
/* 152 */                   if ((val instanceof NodeIterator))
/*     */                   {
/* 154 */                     result = new XNodeSetForDOM((NodeIterator)val, xctxt);
/*     */                   }
/*     */                   else
/*     */                   {
/* 158 */                     result = new XObject(val);
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 161 */     return result;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.objects.XObjectFactory
 * JD-Core Version:    0.6.2
 */