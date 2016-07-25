/*    */ package com.sun.xml.internal.bind.v2.runtime.output;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Base64Data;
/*    */ import com.sun.xml.internal.org.jvnet.staxex.XMLStreamWriterEx;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ 
/*    */ public final class StAXExStreamWriterOutput extends XMLStreamWriterOutput
/*    */ {
/*    */   private final XMLStreamWriterEx out;
/*    */ 
/*    */   public StAXExStreamWriterOutput(XMLStreamWriterEx out)
/*    */   {
/* 43 */     super(out);
/* 44 */     this.out = out;
/*    */   }
/*    */ 
/*    */   public void text(Pcdata value, boolean needsSeparatingWhitespace) throws XMLStreamException {
/* 48 */     if (needsSeparatingWhitespace) {
/* 49 */       this.out.writeCharacters(" ");
/*    */     }
/*    */ 
/* 52 */     if (!(value instanceof Base64Data)) {
/* 53 */       this.out.writeCharacters(value.toString());
/*    */     } else {
/* 55 */       Base64Data v = (Base64Data)value;
/* 56 */       this.out.writeBinary(v.getDataHandler());
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput
 * JD-Core Version:    0.6.2
 */