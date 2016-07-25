/*     */ package javax.xml.crypto.dsig.dom;
/*     */ 
/*     */ import java.security.Key;
/*     */ import javax.xml.crypto.KeySelector;
/*     */ import javax.xml.crypto.dom.DOMCryptoContext;
/*     */ import javax.xml.crypto.dsig.XMLSignContext;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class DOMSignContext extends DOMCryptoContext
/*     */   implements XMLSignContext
/*     */ {
/*     */   private Node parent;
/*     */   private Node nextSibling;
/*     */ 
/*     */   public DOMSignContext(Key paramKey, Node paramNode)
/*     */   {
/*  73 */     if (paramKey == null) {
/*  74 */       throw new NullPointerException("signingKey cannot be null");
/*     */     }
/*  76 */     if (paramNode == null) {
/*  77 */       throw new NullPointerException("parent cannot be null");
/*     */     }
/*  79 */     setKeySelector(KeySelector.singletonKeySelector(paramKey));
/*  80 */     this.parent = paramNode;
/*     */   }
/*     */ 
/*     */   public DOMSignContext(Key paramKey, Node paramNode1, Node paramNode2)
/*     */   {
/*  99 */     if (paramKey == null) {
/* 100 */       throw new NullPointerException("signingKey cannot be null");
/*     */     }
/* 102 */     if (paramNode1 == null) {
/* 103 */       throw new NullPointerException("parent cannot be null");
/*     */     }
/* 105 */     if (paramNode2 == null) {
/* 106 */       throw new NullPointerException("nextSibling cannot be null");
/*     */     }
/* 108 */     setKeySelector(KeySelector.singletonKeySelector(paramKey));
/* 109 */     this.parent = paramNode1;
/* 110 */     this.nextSibling = paramNode2;
/*     */   }
/*     */ 
/*     */   public DOMSignContext(KeySelector paramKeySelector, Node paramNode)
/*     */   {
/* 126 */     if (paramKeySelector == null) {
/* 127 */       throw new NullPointerException("key selector cannot be null");
/*     */     }
/* 129 */     if (paramNode == null) {
/* 130 */       throw new NullPointerException("parent cannot be null");
/*     */     }
/* 132 */     setKeySelector(paramKeySelector);
/* 133 */     this.parent = paramNode;
/*     */   }
/*     */ 
/*     */   public DOMSignContext(KeySelector paramKeySelector, Node paramNode1, Node paramNode2)
/*     */   {
/* 149 */     if (paramKeySelector == null) {
/* 150 */       throw new NullPointerException("key selector cannot be null");
/*     */     }
/* 152 */     if (paramNode1 == null) {
/* 153 */       throw new NullPointerException("parent cannot be null");
/*     */     }
/* 155 */     if (paramNode2 == null) {
/* 156 */       throw new NullPointerException("nextSibling cannot be null");
/*     */     }
/* 158 */     setKeySelector(paramKeySelector);
/* 159 */     this.parent = paramNode1;
/* 160 */     this.nextSibling = paramNode2;
/*     */   }
/*     */ 
/*     */   public void setParent(Node paramNode)
/*     */   {
/* 172 */     if (paramNode == null) {
/* 173 */       throw new NullPointerException("parent is null");
/*     */     }
/* 175 */     this.parent = paramNode;
/*     */   }
/*     */ 
/*     */   public void setNextSibling(Node paramNode)
/*     */   {
/* 187 */     this.nextSibling = paramNode;
/*     */   }
/*     */ 
/*     */   public Node getParent()
/*     */   {
/* 197 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public Node getNextSibling()
/*     */   {
/* 207 */     return this.nextSibling;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.dsig.dom.DOMSignContext
 * JD-Core Version:    0.6.2
 */