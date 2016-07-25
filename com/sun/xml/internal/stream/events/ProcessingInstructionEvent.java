/*    */ package com.sun.xml.internal.stream.events;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import javax.xml.stream.Location;
/*    */ import javax.xml.stream.events.ProcessingInstruction;
/*    */ 
/*    */ public class ProcessingInstructionEvent extends DummyEvent
/*    */   implements ProcessingInstruction
/*    */ {
/*    */   private String fName;
/*    */   private String fContent;
/*    */ 
/*    */   public ProcessingInstructionEvent()
/*    */   {
/* 50 */     init();
/*    */   }
/*    */ 
/*    */   public ProcessingInstructionEvent(String targetName, String data) {
/* 54 */     this(targetName, data, null);
/*    */   }
/*    */ 
/*    */   public ProcessingInstructionEvent(String targetName, String data, Location loc) {
/* 58 */     init();
/* 59 */     this.fName = targetName;
/* 60 */     this.fContent = data;
/* 61 */     setLocation(loc);
/*    */   }
/*    */ 
/*    */   protected void init() {
/* 65 */     setEventType(3);
/*    */   }
/*    */ 
/*    */   public String getTarget() {
/* 69 */     return this.fName;
/*    */   }
/*    */ 
/*    */   public void setTarget(String targetName) {
/* 73 */     this.fName = targetName;
/*    */   }
/*    */ 
/*    */   public void setData(String data) {
/* 77 */     this.fContent = data;
/*    */   }
/*    */ 
/*    */   public String getData() {
/* 81 */     return this.fContent;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 85 */     if ((this.fContent != null) && (this.fName != null))
/* 86 */       return "<?" + this.fName + " " + this.fContent + "?>";
/* 87 */     if (this.fName != null)
/* 88 */       return "<?" + this.fName + "?>";
/* 89 */     if (this.fContent != null) {
/* 90 */       return "<?" + this.fContent + "?>";
/*    */     }
/* 92 */     return "<??>";
/*    */   }
/*    */ 
/*    */   protected void writeAsEncodedUnicodeEx(Writer writer)
/*    */     throws IOException
/*    */   {
/* 98 */     writer.write(toString());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.events.ProcessingInstructionEvent
 * JD-Core Version:    0.6.2
 */