/*     */ package com.sun.org.apache.xml.internal.serialize;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ 
/*     */ /** @deprecated */
/*     */ public class XHTMLSerializer extends HTMLSerializer
/*     */ {
/*     */   public XHTMLSerializer()
/*     */   {
/*  53 */     super(true, new OutputFormat("xhtml", null, false));
/*     */   }
/*     */ 
/*     */   public XHTMLSerializer(OutputFormat format)
/*     */   {
/*  64 */     super(true, format != null ? format : new OutputFormat("xhtml", null, false));
/*     */   }
/*     */ 
/*     */   public XHTMLSerializer(Writer writer, OutputFormat format)
/*     */   {
/*  78 */     super(true, format != null ? format : new OutputFormat("xhtml", null, false));
/*  79 */     setOutputCharStream(writer);
/*     */   }
/*     */ 
/*     */   public XHTMLSerializer(OutputStream output, OutputFormat format)
/*     */   {
/*  93 */     super(true, format != null ? format : new OutputFormat("xhtml", null, false));
/*  94 */     setOutputByteStream(output);
/*     */   }
/*     */ 
/*     */   public void setOutputFormat(OutputFormat format)
/*     */   {
/* 100 */     super.setOutputFormat(format != null ? format : new OutputFormat("xhtml", null, false));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serialize.XHTMLSerializer
 * JD-Core Version:    0.6.2
 */