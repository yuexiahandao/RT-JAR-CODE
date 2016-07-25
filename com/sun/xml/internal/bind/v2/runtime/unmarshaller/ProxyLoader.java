/*    */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*    */ 
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public abstract class ProxyLoader extends Loader
/*    */ {
/*    */   public ProxyLoader()
/*    */   {
/* 38 */     super(false);
/*    */   }
/*    */ 
/*    */   public final void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException
/*    */   {
/* 43 */     Loader loader = selectLoader(state, ea);
/* 44 */     state.setLoader(loader);
/* 45 */     loader.startElement(state, ea);
/*    */   }
/*    */ 
/*    */   protected abstract Loader selectLoader(UnmarshallingContext.State paramState, TagName paramTagName)
/*    */     throws SAXException;
/*    */ 
/*    */   public final void leaveElement(UnmarshallingContext.State state, TagName ea)
/*    */   {
/* 59 */     throw new IllegalStateException();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.ProxyLoader
 * JD-Core Version:    0.6.2
 */