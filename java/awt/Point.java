/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.geom.Point2D;
/*     */ import java.beans.Transient;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class Point extends Point2D
/*     */   implements Serializable
/*     */ {
/*     */   public int x;
/*     */   public int y;
/*     */   private static final long serialVersionUID = -5276940640259749850L;
/*     */ 
/*     */   public Point()
/*     */   {
/*  72 */     this(0, 0);
/*     */   }
/*     */ 
/*     */   public Point(Point paramPoint)
/*     */   {
/*  82 */     this(paramPoint.x, paramPoint.y);
/*     */   }
/*     */ 
/*     */   public Point(int paramInt1, int paramInt2)
/*     */   {
/*  93 */     this.x = paramInt1;
/*  94 */     this.y = paramInt2;
/*     */   }
/*     */ 
/*     */   public double getX()
/*     */   {
/* 102 */     return this.x;
/*     */   }
/*     */ 
/*     */   public double getY()
/*     */   {
/* 110 */     return this.y;
/*     */   }
/*     */ 
/*     */   @Transient
/*     */   public Point getLocation()
/*     */   {
/* 125 */     return new Point(this.x, this.y);
/*     */   }
/*     */ 
/*     */   public void setLocation(Point paramPoint)
/*     */   {
/* 138 */     setLocation(paramPoint.x, paramPoint.y);
/*     */   }
/*     */ 
/*     */   public void setLocation(int paramInt1, int paramInt2)
/*     */   {
/* 155 */     move(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void setLocation(double paramDouble1, double paramDouble2)
/*     */   {
/* 171 */     this.x = ((int)Math.floor(paramDouble1 + 0.5D));
/* 172 */     this.y = ((int)Math.floor(paramDouble2 + 0.5D));
/*     */   }
/*     */ 
/*     */   public void move(int paramInt1, int paramInt2)
/*     */   {
/* 184 */     this.x = paramInt1;
/* 185 */     this.y = paramInt2;
/*     */   }
/*     */ 
/*     */   public void translate(int paramInt1, int paramInt2)
/*     */   {
/* 200 */     this.x += paramInt1;
/* 201 */     this.y += paramInt2;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 215 */     if ((paramObject instanceof Point)) {
/* 216 */       Point localPoint = (Point)paramObject;
/* 217 */       return (this.x == localPoint.x) && (this.y == localPoint.y);
/*     */     }
/* 219 */     return super.equals(paramObject);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 232 */     return getClass().getName() + "[x=" + this.x + ",y=" + this.y + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Point
 * JD-Core Version:    0.6.2
 */