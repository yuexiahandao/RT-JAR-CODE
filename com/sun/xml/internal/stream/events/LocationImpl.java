/*    */ package com.sun.xml.internal.stream.events;
/*    */ 
/*    */ import javax.xml.stream.Location;
/*    */ 
/*    */ public class LocationImpl
/*    */   implements Location
/*    */ {
/*    */   String systemId;
/*    */   String publicId;
/*    */   int colNo;
/*    */   int lineNo;
/*    */   int charOffset;
/*    */ 
/*    */   LocationImpl(Location loc)
/*    */   {
/* 42 */     this.systemId = loc.getSystemId();
/* 43 */     this.publicId = loc.getPublicId();
/* 44 */     this.lineNo = loc.getLineNumber();
/* 45 */     this.colNo = loc.getColumnNumber();
/* 46 */     this.charOffset = loc.getCharacterOffset();
/*    */   }
/*    */ 
/*    */   public int getCharacterOffset() {
/* 50 */     return this.charOffset;
/*    */   }
/*    */ 
/*    */   public int getColumnNumber() {
/* 54 */     return this.colNo;
/*    */   }
/*    */ 
/*    */   public int getLineNumber() {
/* 58 */     return this.lineNo;
/*    */   }
/*    */ 
/*    */   public String getPublicId() {
/* 62 */     return this.publicId;
/*    */   }
/*    */ 
/*    */   public String getSystemId() {
/* 66 */     return this.systemId;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 70 */     StringBuffer sbuffer = new StringBuffer();
/* 71 */     sbuffer.append("Line number = " + getLineNumber());
/* 72 */     sbuffer.append("\n");
/* 73 */     sbuffer.append("Column number = " + getColumnNumber());
/* 74 */     sbuffer.append("\n");
/* 75 */     sbuffer.append("System Id = " + getSystemId());
/* 76 */     sbuffer.append("\n");
/* 77 */     sbuffer.append("Public Id = " + getPublicId());
/* 78 */     sbuffer.append("\n");
/* 79 */     sbuffer.append("CharacterOffset = " + getCharacterOffset());
/* 80 */     sbuffer.append("\n");
/* 81 */     return sbuffer.toString();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.events.LocationImpl
 * JD-Core Version:    0.6.2
 */