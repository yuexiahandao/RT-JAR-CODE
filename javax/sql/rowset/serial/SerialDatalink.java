/*     */ package javax.sql.rowset.serial;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ 
/*     */ public class SerialDatalink
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private URL url;
/*     */   private int baseType;
/*     */   private String baseTypeName;
/*     */   static final long serialVersionUID = 2826907821828733626L;
/*     */ 
/*     */   public SerialDatalink(URL paramURL)
/*     */     throws SerialException
/*     */   {
/*  77 */     if (paramURL == null) {
/*  78 */       throw new SerialException("Cannot serialize empty URL instance");
/*     */     }
/*  80 */     this.url = paramURL;
/*     */   }
/*     */ 
/*     */   public URL getDatalink()
/*     */     throws SerialException
/*     */   {
/*  93 */     URL localURL = null;
/*     */     try
/*     */     {
/*  96 */       localURL = new URL(this.url.toString());
/*     */     } catch (MalformedURLException localMalformedURLException) {
/*  98 */       throw new SerialException("MalformedURLException: " + localMalformedURLException.getMessage());
/*     */     }
/* 100 */     return localURL;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.serial.SerialDatalink
 * JD-Core Version:    0.6.2
 */