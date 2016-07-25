/*     */ package javax.sound.sampled;
/*     */ 
/*     */ import java.util.EventObject;
/*     */ 
/*     */ public class LineEvent extends EventObject
/*     */ {
/*     */   private final Type type;
/*     */   private final long position;
/*     */ 
/*     */   public LineEvent(Line paramLine, Type paramType, long paramLong)
/*     */   {
/*  85 */     super(paramLine);
/*  86 */     this.type = paramType;
/*  87 */     this.position = paramLong;
/*     */   }
/*     */ 
/*     */   public final Line getLine()
/*     */   {
/*  96 */     return (Line)getSource();
/*     */   }
/*     */ 
/*     */   public final Type getType()
/*     */   {
/* 107 */     return this.type;
/*     */   }
/*     */ 
/*     */   public final long getFramePosition()
/*     */   {
/* 130 */     return this.position;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 139 */     String str1 = "";
/* 140 */     if (this.type != null) str1 = this.type.toString() + " ";
/*     */     String str2;
/* 142 */     if (getLine() == null)
/* 143 */       str2 = "null";
/*     */     else {
/* 145 */       str2 = getLine().toString();
/*     */     }
/* 147 */     return new String(str1 + "event from line " + str2);
/*     */   }
/*     */ 
/*     */   public static class Type
/*     */   {
/*     */     private String name;
/* 212 */     public static final Type OPEN = new Type("Open");
/*     */ 
/* 221 */     public static final Type CLOSE = new Type("Close");
/*     */ 
/* 231 */     public static final Type START = new Type("Start");
/*     */ 
/* 241 */     public static final Type STOP = new Type("Stop");
/*     */ 
/*     */     protected Type(String paramString)
/*     */     {
/* 171 */       this.name = paramString;
/*     */     }
/*     */ 
/*     */     public final boolean equals(Object paramObject)
/*     */     {
/* 184 */       return super.equals(paramObject);
/*     */     }
/*     */ 
/*     */     public final int hashCode()
/*     */     {
/* 192 */       return super.hashCode();
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 200 */       return this.name;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.LineEvent
 * JD-Core Version:    0.6.2
 */