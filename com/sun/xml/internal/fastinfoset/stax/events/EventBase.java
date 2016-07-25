/*     */ package com.sun.xml.internal.fastinfoset.stax.events;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import java.io.Writer;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Characters;
/*     */ import javax.xml.stream.events.EndElement;
/*     */ import javax.xml.stream.events.StartElement;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ 
/*     */ public abstract class EventBase
/*     */   implements XMLEvent
/*     */ {
/*     */   protected int _eventType;
/*  44 */   protected Location _location = null;
/*     */ 
/*     */   public EventBase()
/*     */   {
/*     */   }
/*     */ 
/*     */   public EventBase(int eventType) {
/*  51 */     this._eventType = eventType;
/*     */   }
/*     */ 
/*     */   public int getEventType()
/*     */   {
/*  58 */     return this._eventType;
/*     */   }
/*     */ 
/*     */   protected void setEventType(int eventType) {
/*  62 */     this._eventType = eventType;
/*     */   }
/*     */ 
/*     */   public boolean isStartElement()
/*     */   {
/*  67 */     return this._eventType == 1;
/*     */   }
/*     */ 
/*     */   public boolean isEndElement() {
/*  71 */     return this._eventType == 2;
/*     */   }
/*     */ 
/*     */   public boolean isEntityReference() {
/*  75 */     return this._eventType == 9;
/*     */   }
/*     */ 
/*     */   public boolean isProcessingInstruction() {
/*  79 */     return this._eventType == 3;
/*     */   }
/*     */ 
/*     */   public boolean isStartDocument() {
/*  83 */     return this._eventType == 7;
/*     */   }
/*     */ 
/*     */   public boolean isEndDocument() {
/*  87 */     return this._eventType == 8;
/*     */   }
/*     */ 
/*     */   public Location getLocation()
/*     */   {
/*  97 */     return this._location;
/*     */   }
/*     */ 
/*     */   public void setLocation(Location loc) {
/* 101 */     this._location = loc;
/*     */   }
/*     */   public String getSystemId() {
/* 104 */     if (this._location == null) {
/* 105 */       return "";
/*     */     }
/* 107 */     return this._location.getSystemId();
/*     */   }
/*     */ 
/*     */   public Characters asCharacters()
/*     */   {
/* 114 */     if (isCharacters()) {
/* 115 */       return (Characters)this;
/*     */     }
/* 117 */     throw new ClassCastException(CommonResourceBundle.getInstance().getString("message.charactersCast", new Object[] { getEventTypeString() }));
/*     */   }
/*     */ 
/*     */   public EndElement asEndElement()
/*     */   {
/* 124 */     if (isEndElement()) {
/* 125 */       return (EndElement)this;
/*     */     }
/* 127 */     throw new ClassCastException(CommonResourceBundle.getInstance().getString("message.endElementCase", new Object[] { getEventTypeString() }));
/*     */   }
/*     */ 
/*     */   public StartElement asStartElement()
/*     */   {
/* 135 */     if (isStartElement()) {
/* 136 */       return (StartElement)this;
/*     */     }
/* 138 */     throw new ClassCastException(CommonResourceBundle.getInstance().getString("message.startElementCase", new Object[] { getEventTypeString() }));
/*     */   }
/*     */ 
/*     */   public QName getSchemaType()
/*     */   {
/* 148 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isAttribute()
/*     */   {
/* 155 */     return this._eventType == 10;
/*     */   }
/*     */ 
/*     */   public boolean isCharacters()
/*     */   {
/* 162 */     return this._eventType == 4;
/*     */   }
/*     */ 
/*     */   public boolean isNamespace()
/*     */   {
/* 169 */     return this._eventType == 13;
/*     */   }
/*     */ 
/*     */   public void writeAsEncodedUnicode(Writer writer)
/*     */     throws XMLStreamException
/*     */   {
/*     */   }
/*     */ 
/*     */   private String getEventTypeString()
/*     */   {
/* 193 */     switch (this._eventType) {
/*     */     case 1:
/* 195 */       return "StartElementEvent";
/*     */     case 2:
/* 197 */       return "EndElementEvent";
/*     */     case 3:
/* 199 */       return "ProcessingInstructionEvent";
/*     */     case 4:
/* 201 */       return "CharacterEvent";
/*     */     case 5:
/* 203 */       return "CommentEvent";
/*     */     case 7:
/* 205 */       return "StartDocumentEvent";
/*     */     case 8:
/* 207 */       return "EndDocumentEvent";
/*     */     case 9:
/* 209 */       return "EntityReferenceEvent";
/*     */     case 10:
/* 211 */       return "AttributeBase";
/*     */     case 11:
/* 213 */       return "DTDEvent";
/*     */     case 12:
/* 215 */       return "CDATA";
/*     */     case 6:
/* 217 */     }return "UNKNOWN_EVENT_TYPE";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.events.EventBase
 * JD-Core Version:    0.6.2
 */