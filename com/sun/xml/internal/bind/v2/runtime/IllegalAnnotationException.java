/*     */ package com.sun.xml.internal.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBException;
/*     */ 
/*     */ public class IllegalAnnotationException extends JAXBException
/*     */ {
/*     */   private final List<List<Location>> pos;
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */   public IllegalAnnotationException(String message, Locatable src)
/*     */   {
/*  54 */     super(message);
/*  55 */     this.pos = build(new Locatable[] { src });
/*     */   }
/*     */ 
/*     */   public IllegalAnnotationException(String message, Annotation src) {
/*  59 */     this(message, cast(src));
/*     */   }
/*     */ 
/*     */   public IllegalAnnotationException(String message, Locatable src1, Locatable src2) {
/*  63 */     super(message);
/*  64 */     this.pos = build(new Locatable[] { src1, src2 });
/*     */   }
/*     */ 
/*     */   public IllegalAnnotationException(String message, Annotation src1, Annotation src2) {
/*  68 */     this(message, cast(src1), cast(src2));
/*     */   }
/*     */ 
/*     */   public IllegalAnnotationException(String message, Annotation src1, Locatable src2) {
/*  72 */     this(message, cast(src1), src2);
/*     */   }
/*     */ 
/*     */   public IllegalAnnotationException(String message, Throwable cause, Locatable src) {
/*  76 */     super(message, cause);
/*  77 */     this.pos = build(new Locatable[] { src });
/*     */   }
/*     */ 
/*     */   private static Locatable cast(Annotation a) {
/*  81 */     if ((a instanceof Locatable)) {
/*  82 */       return (Locatable)a;
/*     */     }
/*  84 */     return null;
/*     */   }
/*     */ 
/*     */   private List<List<Location>> build(Locatable[] srcs) {
/*  88 */     List r = new ArrayList();
/*  89 */     for (Locatable l : srcs) {
/*  90 */       if (l != null) {
/*  91 */         List ll = convert(l);
/*  92 */         if ((ll != null) && (!ll.isEmpty()))
/*  93 */           r.add(ll);
/*     */       }
/*     */     }
/*  96 */     return Collections.unmodifiableList(r);
/*     */   }
/*     */ 
/*     */   private List<Location> convert(Locatable src)
/*     */   {
/* 103 */     if (src == null) return null;
/*     */ 
/* 105 */     List r = new ArrayList();
/* 106 */     for (; src != null; src = src.getUpstream())
/* 107 */       r.add(src.getLocation());
/* 108 */     return Collections.unmodifiableList(r);
/*     */   }
/*     */ 
/*     */   public List<List<Location>> getSourcePos()
/*     */   {
/* 164 */     return this.pos;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 177 */     StringBuilder sb = new StringBuilder(getMessage());
/*     */ 
/* 179 */     for (List locs : this.pos) {
/* 180 */       sb.append("\n\tthis problem is related to the following location:");
/* 181 */       for (Location loc : locs) {
/* 182 */         sb.append("\n\t\tat ").append(loc.toString());
/*     */       }
/*     */     }
/* 185 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException
 * JD-Core Version:    0.6.2
 */