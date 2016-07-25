/*    */ package com.sun.xml.internal.ws.streaming;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.istack.internal.Nullable;
/*    */ import com.sun.xml.internal.ws.util.xml.XMLStreamReaderFilter;
/*    */ import java.io.Closeable;
/*    */ import java.io.IOException;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ import javax.xml.ws.WebServiceException;
/*    */ 
/*    */ public class TidyXMLStreamReader extends XMLStreamReaderFilter
/*    */ {
/*    */   private final Closeable closeableSource;
/*    */ 
/*    */   public TidyXMLStreamReader(@NotNull XMLStreamReader reader, @Nullable Closeable closeableSource)
/*    */   {
/* 48 */     super(reader);
/* 49 */     this.closeableSource = closeableSource;
/*    */   }
/*    */ 
/*    */   public void close() throws XMLStreamException {
/* 53 */     super.close();
/*    */     try {
/* 55 */       if (this.closeableSource != null)
/* 56 */         this.closeableSource.close();
/*    */     } catch (IOException e) {
/* 58 */       throw new WebServiceException(e);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.streaming.TidyXMLStreamReader
 * JD-Core Version:    0.6.2
 */