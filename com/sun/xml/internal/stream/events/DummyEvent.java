/*     */ package com.sun.xml.internal.stream.events;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Characters;
/*     */ import javax.xml.stream.events.EndElement;
/*     */ import javax.xml.stream.events.StartElement;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ 
/*     */ public abstract class DummyEvent
/*     */   implements XMLEvent
/*     */ {
/*  49 */   private static DummyLocation nowhere = new DummyLocation();
/*     */   private int fEventType;
/*  53 */   protected Location fLocation = nowhere;
/*     */ 
/*     */   public DummyEvent() {
/*     */   }
/*     */ 
/*     */   public DummyEvent(int i) {
/*  59 */     this.fEventType = i;
/*     */   }
/*     */ 
/*     */   public int getEventType() {
/*  63 */     return this.fEventType;
/*     */   }
/*     */ 
/*     */   protected void setEventType(int eventType) {
/*  67 */     this.fEventType = eventType;
/*     */   }
/*     */ 
/*     */   public boolean isStartElement()
/*     */   {
/*  72 */     return this.fEventType == 1;
/*     */   }
/*     */ 
/*     */   public boolean isEndElement() {
/*  76 */     return this.fEventType == 2;
/*     */   }
/*     */ 
/*     */   public boolean isEntityReference() {
/*  80 */     return this.fEventType == 9;
/*     */   }
/*     */ 
/*     */   public boolean isProcessingInstruction() {
/*  84 */     return this.fEventType == 3;
/*     */   }
/*     */ 
/*     */   public boolean isCharacterData() {
/*  88 */     return this.fEventType == 4;
/*     */   }
/*     */ 
/*     */   public boolean isStartDocument() {
/*  92 */     return this.fEventType == 7;
/*     */   }
/*     */ 
/*     */   public boolean isEndDocument() {
/*  96 */     return this.fEventType == 8;
/*     */   }
/*     */ 
/*     */   public Location getLocation() {
/* 100 */     return this.fLocation;
/*     */   }
/*     */ 
/*     */   void setLocation(Location loc) {
/* 104 */     if (loc == null)
/* 105 */       this.fLocation = nowhere;
/*     */     else
/* 107 */       this.fLocation = loc;
/*     */   }
/*     */ 
/*     */   public Characters asCharacters()
/*     */   {
/* 115 */     return (Characters)this;
/*     */   }
/*     */ 
/*     */   public EndElement asEndElement()
/*     */   {
/* 122 */     return (EndElement)this;
/*     */   }
/*     */ 
/*     */   public StartElement asStartElement()
/*     */   {
/* 129 */     return (StartElement)this;
/*     */   }
/*     */ 
/*     */   public QName getSchemaType()
/*     */   {
/* 139 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isAttribute()
/*     */   {
/* 146 */     return this.fEventType == 10;
/*     */   }
/*     */ 
/*     */   public boolean isCharacters()
/*     */   {
/* 153 */     return this.fEventType == 4;
/*     */   }
/*     */ 
/*     */   public boolean isNamespace()
/*     */   {
/* 160 */     return this.fEventType == 13;
/*     */   }
/*     */ 
/*     */   public void writeAsEncodedUnicode(Writer writer)
/*     */     throws XMLStreamException
/*     */   {
/*     */     try
/*     */     {
/* 180 */       writeAsEncodedUnicodeEx(writer);
/*     */     } catch (IOException e) {
/* 182 */       throw new XMLStreamException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract void writeAsEncodedUnicodeEx(Writer paramWriter)
/*     */     throws IOException, XMLStreamException;
/*     */ 
/*     */   protected void charEncode(Writer writer, String data)
/*     */     throws IOException
/*     */   {
/* 199 */     if ((data == null) || (data == "")) return;
/* 200 */     int i = 0; int start = 0;
/* 201 */     int len = data.length();
/*     */ 
/* 204 */     for (; i < len; i++) {
/* 205 */       switch (data.charAt(i)) {
/*     */       case '<':
/* 207 */         writer.write(data, start, i - start);
/* 208 */         writer.write("&lt;");
/* 209 */         start = i + 1;
/* 210 */         break;
/*     */       case '&':
/* 213 */         writer.write(data, start, i - start);
/* 214 */         writer.write("&amp;");
/* 215 */         start = i + 1;
/* 216 */         break;
/*     */       case '>':
/* 219 */         writer.write(data, start, i - start);
/* 220 */         writer.write("&gt;");
/* 221 */         start = i + 1;
/* 222 */         break;
/*     */       case '"':
/* 224 */         writer.write(data, start, i - start);
/* 225 */         writer.write("&quot;");
/* 226 */         start = i + 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 231 */     writer.write(data, start, len - start);
/*     */   }
/*     */ 
/*     */   static class DummyLocation
/*     */     implements Location
/*     */   {
/*     */     public int getCharacterOffset()
/*     */     {
/* 239 */       return -1;
/*     */     }
/*     */ 
/*     */     public int getColumnNumber() {
/* 243 */       return -1;
/*     */     }
/*     */ 
/*     */     public int getLineNumber() {
/* 247 */       return -1;
/*     */     }
/*     */ 
/*     */     public String getPublicId() {
/* 251 */       return null;
/*     */     }
/*     */ 
/*     */     public String getSystemId() {
/* 255 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.events.DummyEvent
 * JD-Core Version:    0.6.2
 */