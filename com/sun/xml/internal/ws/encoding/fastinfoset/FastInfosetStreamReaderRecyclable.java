/*    */ package com.sun.xml.internal.ws.encoding.fastinfoset;
/*    */ 
/*    */ import com.sun.xml.internal.fastinfoset.stax.StAXDocumentParser;
/*    */ import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.RecycleAware;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public final class FastInfosetStreamReaderRecyclable extends StAXDocumentParser
/*    */   implements XMLStreamReaderFactory.RecycleAware
/*    */ {
/* 36 */   private static final FastInfosetStreamReaderFactory READER_FACTORY = FastInfosetStreamReaderFactory.getInstance();
/*    */ 
/*    */   public FastInfosetStreamReaderRecyclable()
/*    */   {
/*    */   }
/*    */ 
/*    */   public FastInfosetStreamReaderRecyclable(InputStream in) {
/* 43 */     super(in);
/*    */   }
/*    */ 
/*    */   public void onRecycled() {
/* 47 */     READER_FACTORY.doRecycle(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.fastinfoset.FastInfosetStreamReaderRecyclable
 * JD-Core Version:    0.6.2
 */