/*     */ package sun.org.mozilla.javascript.internal.xmlimpl;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.NativeWith;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ import sun.org.mozilla.javascript.internal.xml.XMLObject;
/*     */ 
/*     */ final class XMLWithScope extends NativeWith
/*     */ {
/*     */   private static final long serialVersionUID = -696429282095170887L;
/*     */   private XMLLibImpl lib;
/*     */   private int _currIndex;
/*     */   private XMLList _xmlList;
/*     */   private XMLObject _dqPrototype;
/*     */ 
/*     */   XMLWithScope(XMLLibImpl paramXMLLibImpl, Scriptable paramScriptable, XMLObject paramXMLObject)
/*     */   {
/*  58 */     super(paramScriptable, paramXMLObject);
/*  59 */     this.lib = paramXMLLibImpl;
/*     */   }
/*     */ 
/*     */   void initAsDotQuery()
/*     */   {
/*  64 */     XMLObject localXMLObject = (XMLObject)getPrototype();
/*     */ 
/*  72 */     this._currIndex = 0;
/*  73 */     this._dqPrototype = localXMLObject;
/*  74 */     if ((localXMLObject instanceof XMLList)) {
/*  75 */       XMLList localXMLList = (XMLList)localXMLObject;
/*  76 */       if (localXMLList.length() > 0) {
/*  77 */         setPrototype((Scriptable)localXMLList.get(0, null));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  82 */     this._xmlList = this.lib.newXMLList();
/*     */   }
/*     */ 
/*     */   protected Object updateDotQuery(boolean paramBoolean)
/*     */   {
/*  90 */     XMLObject localXMLObject = this._dqPrototype;
/*  91 */     XMLList localXMLList1 = this._xmlList;
/*     */ 
/*  93 */     if ((localXMLObject instanceof XMLList))
/*     */     {
/*  97 */       XMLList localXMLList2 = (XMLList)localXMLObject;
/*     */ 
/*  99 */       int i = this._currIndex;
/*     */ 
/* 101 */       if (paramBoolean) {
/* 102 */         localXMLList1.addToList(localXMLList2.get(i, null));
/*     */       }
/*     */ 
/* 106 */       i++; if (i < localXMLList2.length())
/*     */       {
/* 110 */         this._currIndex = i;
/* 111 */         setPrototype((Scriptable)localXMLList2.get(i, null));
/*     */ 
/* 114 */         return null;
/*     */       }
/*     */ 
/*     */     }
/* 119 */     else if (paramBoolean) {
/* 120 */       localXMLList1.addToList(localXMLObject);
/*     */     }
/*     */ 
/* 124 */     return localXMLList1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.xmlimpl.XMLWithScope
 * JD-Core Version:    0.6.2
 */