/*     */ package java.sql;
/*     */ 
/*     */ class DriverInfo
/*     */ {
/*     */   final Driver driver;
/*     */ 
/*     */   DriverInfo(Driver paramDriver)
/*     */   {
/* 609 */     this.driver = paramDriver;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 613 */     return ((paramObject instanceof DriverInfo)) && (this.driver == ((DriverInfo)paramObject).driver);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 618 */     return this.driver.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 622 */     return "driver[className=" + this.driver + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.DriverInfo
 * JD-Core Version:    0.6.2
 */