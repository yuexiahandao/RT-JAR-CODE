/*     */ package com.sun.xml.internal.fastinfoset.stax.events;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.xml.stream.events.DTD;
/*     */ 
/*     */ public class DTDEvent extends EventBase
/*     */   implements DTD
/*     */ {
/*     */   private String _dtd;
/*     */   private List _notations;
/*     */   private List _entities;
/*     */ 
/*     */   public DTDEvent()
/*     */   {
/*  47 */     setEventType(11);
/*     */   }
/*     */ 
/*     */   public DTDEvent(String dtd) {
/*  51 */     setEventType(11);
/*  52 */     this._dtd = dtd;
/*     */   }
/*     */ 
/*     */   public String getDocumentTypeDeclaration()
/*     */   {
/*  64 */     return this._dtd;
/*     */   }
/*     */   public void setDTD(String dtd) {
/*  67 */     this._dtd = dtd;
/*     */   }
/*     */ 
/*     */   public List getEntities()
/*     */   {
/*  78 */     return this._entities;
/*     */   }
/*     */ 
/*     */   public List getNotations()
/*     */   {
/*  88 */     return this._notations;
/*     */   }
/*     */ 
/*     */   public Object getProcessedDTD()
/*     */   {
/*  97 */     return null;
/*     */   }
/*     */ 
/*     */   public void setEntities(List entites) {
/* 101 */     this._entities = entites;
/*     */   }
/*     */ 
/*     */   public void setNotations(List notations) {
/* 105 */     this._notations = notations;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 109 */     return this._dtd;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.events.DTDEvent
 * JD-Core Version:    0.6.2
 */