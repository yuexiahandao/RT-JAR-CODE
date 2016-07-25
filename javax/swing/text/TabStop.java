/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class TabStop
/*     */   implements Serializable
/*     */ {
/*     */   public static final int ALIGN_LEFT = 0;
/*     */   public static final int ALIGN_RIGHT = 1;
/*     */   public static final int ALIGN_CENTER = 2;
/*     */   public static final int ALIGN_DECIMAL = 4;
/*     */   public static final int ALIGN_BAR = 5;
/*     */   public static final int LEAD_NONE = 0;
/*     */   public static final int LEAD_DOTS = 1;
/*     */   public static final int LEAD_HYPHENS = 2;
/*     */   public static final int LEAD_UNDERLINE = 3;
/*     */   public static final int LEAD_THICKLINE = 4;
/*     */   public static final int LEAD_EQUALS = 5;
/*     */   private int alignment;
/*     */   private float position;
/*     */   private int leader;
/*     */ 
/*     */   public TabStop(float paramFloat)
/*     */   {
/*  86 */     this(paramFloat, 0, 0);
/*     */   }
/*     */ 
/*     */   public TabStop(float paramFloat, int paramInt1, int paramInt2)
/*     */   {
/*  94 */     this.alignment = paramInt1;
/*  95 */     this.leader = paramInt2;
/*  96 */     this.position = paramFloat;
/*     */   }
/*     */ 
/*     */   public float getPosition()
/*     */   {
/* 104 */     return this.position;
/*     */   }
/*     */ 
/*     */   public int getAlignment()
/*     */   {
/* 112 */     return this.alignment;
/*     */   }
/*     */ 
/*     */   public int getLeader()
/*     */   {
/* 120 */     return this.leader;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 129 */     if (paramObject == this) {
/* 130 */       return true;
/*     */     }
/* 132 */     if ((paramObject instanceof TabStop)) {
/* 133 */       TabStop localTabStop = (TabStop)paramObject;
/* 134 */       return (this.alignment == localTabStop.alignment) && (this.leader == localTabStop.leader) && (this.position == localTabStop.position);
/*     */     }
/*     */ 
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 148 */     return this.alignment ^ this.leader ^ Math.round(this.position);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 154 */     switch (this.alignment) { case 0:
/*     */     case 3:
/*     */     default:
/* 157 */       str = "";
/* 158 */       break;
/*     */     case 1:
/* 160 */       str = "right ";
/* 161 */       break;
/*     */     case 2:
/* 163 */       str = "center ";
/* 164 */       break;
/*     */     case 4:
/* 166 */       str = "decimal ";
/* 167 */       break;
/*     */     case 5:
/* 169 */       str = "bar ";
/*     */     }
/*     */ 
/* 172 */     String str = str + "tab @" + String.valueOf(this.position);
/* 173 */     if (this.leader != 0)
/* 174 */       str = str + " (w/leaders)";
/* 175 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.TabStop
 * JD-Core Version:    0.6.2
 */