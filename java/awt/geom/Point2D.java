/*     */ package java.awt.geom;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class Point2D
/*     */   implements Cloneable
/*     */ {
/*     */   public abstract double getX();
/*     */ 
/*     */   public abstract double getY();
/*     */ 
/*     */   public abstract void setLocation(double paramDouble1, double paramDouble2);
/*     */ 
/*     */   public void setLocation(Point2D paramPoint2D)
/*     */   {
/* 274 */     setLocation(paramPoint2D.getX(), paramPoint2D.getY());
/*     */   }
/*     */ 
/*     */   public static double distanceSq(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 291 */     paramDouble1 -= paramDouble3;
/* 292 */     paramDouble2 -= paramDouble4;
/* 293 */     return paramDouble1 * paramDouble1 + paramDouble2 * paramDouble2;
/*     */   }
/*     */ 
/*     */   public static double distance(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 310 */     paramDouble1 -= paramDouble3;
/* 311 */     paramDouble2 -= paramDouble4;
/* 312 */     return Math.sqrt(paramDouble1 * paramDouble1 + paramDouble2 * paramDouble2);
/*     */   }
/*     */ 
/*     */   public double distanceSq(double paramDouble1, double paramDouble2)
/*     */   {
/* 328 */     paramDouble1 -= getX();
/* 329 */     paramDouble2 -= getY();
/* 330 */     return paramDouble1 * paramDouble1 + paramDouble2 * paramDouble2;
/*     */   }
/*     */ 
/*     */   public double distanceSq(Point2D paramPoint2D)
/*     */   {
/* 344 */     double d1 = paramPoint2D.getX() - getX();
/* 345 */     double d2 = paramPoint2D.getY() - getY();
/* 346 */     return d1 * d1 + d2 * d2;
/*     */   }
/*     */ 
/*     */   public double distance(double paramDouble1, double paramDouble2)
/*     */   {
/* 362 */     paramDouble1 -= getX();
/* 363 */     paramDouble2 -= getY();
/* 364 */     return Math.sqrt(paramDouble1 * paramDouble1 + paramDouble2 * paramDouble2);
/*     */   }
/*     */ 
/*     */   public double distance(Point2D paramPoint2D)
/*     */   {
/* 378 */     double d1 = paramPoint2D.getX() - getX();
/* 379 */     double d2 = paramPoint2D.getY() - getY();
/* 380 */     return Math.sqrt(d1 * d1 + d2 * d2);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 393 */       return super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 396 */     throw new InternalError();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 405 */     long l = Double.doubleToLongBits(getX());
/* 406 */     l ^= Double.doubleToLongBits(getY()) * 31L;
/* 407 */     return (int)l ^ (int)(l >> 32);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 422 */     if ((paramObject instanceof Point2D)) {
/* 423 */       Point2D localPoint2D = (Point2D)paramObject;
/* 424 */       return (getX() == localPoint2D.getX()) && (getY() == localPoint2D.getY());
/*     */     }
/* 426 */     return super.equals(paramObject);
/*     */   }
/*     */ 
/*     */   public static class Double extends Point2D
/*     */     implements Serializable
/*     */   {
/*     */     public double x;
/*     */     public double y;
/*     */     private static final long serialVersionUID = 6150783262733311327L;
/*     */ 
/*     */     public Double()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Double(double paramDouble1, double paramDouble2)
/*     */     {
/* 180 */       this.x = paramDouble1;
/* 181 */       this.y = paramDouble2;
/*     */     }
/*     */ 
/*     */     public double getX()
/*     */     {
/* 189 */       return this.x;
/*     */     }
/*     */ 
/*     */     public double getY()
/*     */     {
/* 197 */       return this.y;
/*     */     }
/*     */ 
/*     */     public void setLocation(double paramDouble1, double paramDouble2)
/*     */     {
/* 205 */       this.x = paramDouble1;
/* 206 */       this.y = paramDouble2;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 216 */       return "Point2D.Double[" + this.x + ", " + this.y + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Float extends Point2D
/*     */     implements Serializable
/*     */   {
/*     */     public float x;
/*     */     public float y;
/*     */     private static final long serialVersionUID = -2870572449815403710L;
/*     */ 
/*     */     public Float()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Float(float paramFloat1, float paramFloat2)
/*     */     {
/*  83 */       this.x = paramFloat1;
/*  84 */       this.y = paramFloat2;
/*     */     }
/*     */ 
/*     */     public double getX()
/*     */     {
/*  92 */       return this.x;
/*     */     }
/*     */ 
/*     */     public double getY()
/*     */     {
/* 100 */       return this.y;
/*     */     }
/*     */ 
/*     */     public void setLocation(double paramDouble1, double paramDouble2)
/*     */     {
/* 108 */       this.x = ((float)paramDouble1);
/* 109 */       this.y = ((float)paramDouble2);
/*     */     }
/*     */ 
/*     */     public void setLocation(float paramFloat1, float paramFloat2)
/*     */     {
/* 121 */       this.x = paramFloat1;
/* 122 */       this.y = paramFloat2;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 132 */       return "Point2D.Float[" + this.x + ", " + this.y + "]";
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.geom.Point2D
 * JD-Core Version:    0.6.2
 */