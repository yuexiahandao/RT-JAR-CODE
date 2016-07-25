/*     */ package com.sun.xml.internal.stream.events;
/*     */ 
/*     */ import com.sun.xml.internal.stream.dtd.nonvalidating.XMLNotationDecl;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import javax.xml.stream.events.NotationDeclaration;
/*     */ 
/*     */ public class NotationDeclarationImpl extends DummyEvent
/*     */   implements NotationDeclaration
/*     */ {
/*  39 */   String fName = null;
/*  40 */   String fPublicId = null;
/*  41 */   String fSystemId = null;
/*     */ 
/*     */   public NotationDeclarationImpl()
/*     */   {
/*  45 */     setEventType(14);
/*     */   }
/*     */ 
/*     */   public NotationDeclarationImpl(String name, String publicId, String systemId) {
/*  49 */     this.fName = name;
/*  50 */     this.fPublicId = publicId;
/*  51 */     this.fSystemId = systemId;
/*  52 */     setEventType(14);
/*     */   }
/*     */ 
/*     */   public NotationDeclarationImpl(XMLNotationDecl notation) {
/*  56 */     this.fName = notation.name;
/*  57 */     this.fPublicId = notation.publicId;
/*  58 */     this.fSystemId = notation.systemId;
/*  59 */     setEventType(14);
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  63 */     return this.fName;
/*     */   }
/*     */ 
/*     */   public String getPublicId() {
/*  67 */     return this.fPublicId;
/*     */   }
/*     */ 
/*     */   public String getSystemId() {
/*  71 */     return this.fSystemId;
/*     */   }
/*     */ 
/*     */   void setPublicId(String publicId) {
/*  75 */     this.fPublicId = publicId;
/*     */   }
/*     */ 
/*     */   void setSystemId(String systemId) {
/*  79 */     this.fSystemId = systemId;
/*     */   }
/*     */ 
/*     */   void setName(String name) {
/*  83 */     this.fName = name;
/*     */   }
/*     */ 
/*     */   protected void writeAsEncodedUnicodeEx(Writer writer)
/*     */     throws IOException
/*     */   {
/*  89 */     writer.write("<!NOTATION ");
/*  90 */     writer.write(getName());
/*  91 */     if (this.fPublicId != null) {
/*  92 */       writer.write(" PUBLIC \"");
/*  93 */       writer.write(this.fPublicId);
/*  94 */       writer.write("\"");
/*  95 */     } else if (this.fSystemId != null) {
/*  96 */       writer.write(" SYSTEM");
/*  97 */       writer.write(" \"");
/*  98 */       writer.write(this.fSystemId);
/*  99 */       writer.write("\"");
/*     */     }
/* 101 */     writer.write(62);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.events.NotationDeclarationImpl
 * JD-Core Version:    0.6.2
 */