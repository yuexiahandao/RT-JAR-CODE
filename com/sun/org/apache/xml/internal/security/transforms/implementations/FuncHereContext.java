/*     */ package com.sun.org.apache.xml.internal.security.transforms.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*     */ import com.sun.org.apache.xml.internal.security.utils.I18n;
/*     */ import com.sun.org.apache.xpath.internal.CachedXPathAPI;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class FuncHereContext extends XPathContext
/*     */ {
/*     */   private FuncHereContext()
/*     */   {
/*     */   }
/*     */ 
/*     */   public FuncHereContext(Node paramNode)
/*     */   {
/*  84 */     super(paramNode);
/*     */   }
/*     */ 
/*     */   public FuncHereContext(Node paramNode, XPathContext paramXPathContext)
/*     */   {
/*  95 */     super(paramNode);
/*     */     try
/*     */     {
/*  98 */       this.m_dtmManager = paramXPathContext.getDTMManager();
/*     */     } catch (IllegalAccessError localIllegalAccessError) {
/* 100 */       throw new IllegalAccessError(I18n.translate("endorsed.jdk1.4.0") + " Original message was \"" + localIllegalAccessError.getMessage() + "\"");
/*     */     }
/*     */   }
/*     */ 
/*     */   public FuncHereContext(Node paramNode, CachedXPathAPI paramCachedXPathAPI)
/*     */   {
/* 114 */     super(paramNode);
/*     */     try
/*     */     {
/* 117 */       this.m_dtmManager = paramCachedXPathAPI.getXPathContext().getDTMManager();
/*     */     } catch (IllegalAccessError localIllegalAccessError) {
/* 119 */       throw new IllegalAccessError(I18n.translate("endorsed.jdk1.4.0") + " Original message was \"" + localIllegalAccessError.getMessage() + "\"");
/*     */     }
/*     */   }
/*     */ 
/*     */   public FuncHereContext(Node paramNode, DTMManager paramDTMManager)
/*     */   {
/* 133 */     super(paramNode);
/*     */     try
/*     */     {
/* 136 */       this.m_dtmManager = paramDTMManager;
/*     */     } catch (IllegalAccessError localIllegalAccessError) {
/* 138 */       throw new IllegalAccessError(I18n.translate("endorsed.jdk1.4.0") + " Original message was \"" + localIllegalAccessError.getMessage() + "\"");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.transforms.implementations.FuncHereContext
 * JD-Core Version:    0.6.2
 */