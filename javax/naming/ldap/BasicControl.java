/*     */ package javax.naming.ldap;
/*     */ 
/*     */ public class BasicControl
/*     */   implements Control
/*     */ {
/*     */   protected String id;
/*  50 */   protected boolean criticality = false;
/*     */ 
/*  57 */   protected byte[] value = null;
/*     */   private static final long serialVersionUID = -4233907508771791687L;
/*     */ 
/*     */   public BasicControl(String paramString)
/*     */   {
/*  68 */     this.id = paramString;
/*     */   }
/*     */ 
/*     */   public BasicControl(String paramString, boolean paramBoolean, byte[] paramArrayOfByte)
/*     */   {
/*  82 */     this.id = paramString;
/*  83 */     this.criticality = paramBoolean;
/*  84 */     this.value = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public String getID()
/*     */   {
/*  93 */     return this.id;
/*     */   }
/*     */ 
/*     */   public boolean isCritical()
/*     */   {
/* 102 */     return this.criticality;
/*     */   }
/*     */ 
/*     */   public byte[] getEncodedValue()
/*     */   {
/* 115 */     return this.value;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ldap.BasicControl
 * JD-Core Version:    0.6.2
 */