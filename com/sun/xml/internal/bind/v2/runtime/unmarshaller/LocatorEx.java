/*     */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import java.net.URL;
/*     */ import javax.xml.bind.ValidationEventLocator;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public abstract interface LocatorEx extends Locator
/*     */ {
/*     */   public abstract ValidationEventLocator getLocation();
/*     */ 
/*     */   public static final class Snapshot
/*     */     implements LocatorEx, ValidationEventLocator
/*     */   {
/*     */     private final int columnNumber;
/*     */     private final int lineNumber;
/*     */     private final int offset;
/*     */     private final String systemId;
/*     */     private final String publicId;
/*     */     private final URL url;
/*     */     private final Object object;
/*     */     private final Node node;
/*     */ 
/*     */     public Snapshot(LocatorEx loc)
/*     */     {
/*  57 */       this.columnNumber = loc.getColumnNumber();
/*  58 */       this.lineNumber = loc.getLineNumber();
/*  59 */       this.systemId = loc.getSystemId();
/*  60 */       this.publicId = loc.getPublicId();
/*     */ 
/*  62 */       ValidationEventLocator vel = loc.getLocation();
/*  63 */       this.offset = vel.getOffset();
/*  64 */       this.url = vel.getURL();
/*  65 */       this.object = vel.getObject();
/*  66 */       this.node = vel.getNode();
/*     */     }
/*     */ 
/*     */     public Object getObject() {
/*  70 */       return this.object;
/*     */     }
/*     */ 
/*     */     public Node getNode() {
/*  74 */       return this.node;
/*     */     }
/*     */ 
/*     */     public int getOffset() {
/*  78 */       return this.offset;
/*     */     }
/*     */ 
/*     */     public URL getURL() {
/*  82 */       return this.url;
/*     */     }
/*     */ 
/*     */     public int getColumnNumber() {
/*  86 */       return this.columnNumber;
/*     */     }
/*     */ 
/*     */     public int getLineNumber() {
/*  90 */       return this.lineNumber;
/*     */     }
/*     */ 
/*     */     public String getSystemId() {
/*  94 */       return this.systemId;
/*     */     }
/*     */ 
/*     */     public String getPublicId() {
/*  98 */       return this.publicId;
/*     */     }
/*     */ 
/*     */     public ValidationEventLocator getLocation() {
/* 102 */       return this;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx
 * JD-Core Version:    0.6.2
 */