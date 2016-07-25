/*     */ package com.sun.xml.internal.stream.events;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import javax.xml.stream.events.EntityDeclaration;
/*     */ 
/*     */ public class EntityDeclarationImpl extends DummyEvent
/*     */   implements EntityDeclaration
/*     */ {
/*     */   private XMLResourceIdentifier fXMLResourceIdentifier;
/*     */   private String fEntityName;
/*     */   private String fReplacementText;
/*     */   private String fNotationName;
/*     */ 
/*     */   public EntityDeclarationImpl()
/*     */   {
/*  48 */     init();
/*     */   }
/*     */ 
/*     */   public EntityDeclarationImpl(String entityName, String replacementText) {
/*  52 */     this(entityName, replacementText, null);
/*     */   }
/*     */ 
/*     */   public EntityDeclarationImpl(String entityName, String replacementText, XMLResourceIdentifier resourceIdentifier)
/*     */   {
/*  57 */     init();
/*  58 */     this.fEntityName = entityName;
/*  59 */     this.fReplacementText = replacementText;
/*  60 */     this.fXMLResourceIdentifier = resourceIdentifier;
/*     */   }
/*     */ 
/*     */   public void setEntityName(String entityName) {
/*  64 */     this.fEntityName = entityName;
/*     */   }
/*     */ 
/*     */   public String getEntityName() {
/*  68 */     return this.fEntityName;
/*     */   }
/*     */ 
/*     */   public void setEntityReplacementText(String replacementText) {
/*  72 */     this.fReplacementText = replacementText;
/*     */   }
/*     */ 
/*     */   public void setXMLResourceIdentifier(XMLResourceIdentifier resourceIdentifier) {
/*  76 */     this.fXMLResourceIdentifier = resourceIdentifier;
/*     */   }
/*     */ 
/*     */   public XMLResourceIdentifier getXMLResourceIdentifier() {
/*  80 */     return this.fXMLResourceIdentifier;
/*     */   }
/*     */ 
/*     */   public String getSystemId() {
/*  84 */     if (this.fXMLResourceIdentifier != null)
/*  85 */       return this.fXMLResourceIdentifier.getLiteralSystemId();
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   public String getPublicId() {
/*  90 */     if (this.fXMLResourceIdentifier != null) {
/*  91 */       return this.fXMLResourceIdentifier.getPublicId();
/*     */     }
/*  93 */     return null;
/*     */   }
/*     */ 
/*     */   public String getBaseURI() {
/*  97 */     if (this.fXMLResourceIdentifier != null)
/*  98 */       return this.fXMLResourceIdentifier.getBaseSystemId();
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 103 */     return this.fEntityName;
/*     */   }
/*     */ 
/*     */   public String getNotationName() {
/* 107 */     return this.fNotationName;
/*     */   }
/*     */ 
/*     */   public void setNotationName(String notationName) {
/* 111 */     this.fNotationName = notationName;
/*     */   }
/*     */ 
/*     */   public String getReplacementText() {
/* 115 */     return this.fReplacementText;
/*     */   }
/*     */ 
/*     */   protected void init() {
/* 119 */     setEventType(15);
/*     */   }
/*     */ 
/*     */   protected void writeAsEncodedUnicodeEx(Writer writer)
/*     */     throws IOException
/*     */   {
/* 125 */     writer.write("<!ENTITY ");
/* 126 */     writer.write(this.fEntityName);
/* 127 */     if (this.fReplacementText != null)
/*     */     {
/* 130 */       writer.write(" \"");
/* 131 */       charEncode(writer, this.fReplacementText);
/*     */     }
/*     */     else {
/* 134 */       String pubId = getPublicId();
/* 135 */       if (pubId != null) {
/* 136 */         writer.write(" PUBLIC \"");
/* 137 */         writer.write(pubId);
/*     */       } else {
/* 139 */         writer.write(" SYSTEM \"");
/* 140 */         writer.write(getSystemId());
/*     */       }
/*     */     }
/* 143 */     writer.write("\"");
/* 144 */     if (this.fNotationName != null) {
/* 145 */       writer.write(" NDATA ");
/* 146 */       writer.write(this.fNotationName);
/*     */     }
/* 148 */     writer.write(">");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.events.EntityDeclarationImpl
 * JD-Core Version:    0.6.2
 */