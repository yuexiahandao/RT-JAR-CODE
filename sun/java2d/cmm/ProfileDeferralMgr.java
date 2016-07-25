/*     */ package sun.java2d.cmm;
/*     */ 
/*     */ import java.awt.color.ProfileDataException;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class ProfileDeferralMgr
/*     */ {
/*  42 */   public static boolean deferring = true;
/*     */   private static Vector<ProfileActivator> aVector;
/*     */ 
/*     */   public static void registerDeferral(ProfileActivator paramProfileActivator)
/*     */   {
/*  51 */     if (!deferring) {
/*  52 */       return;
/*     */     }
/*  54 */     if (aVector == null) {
/*  55 */       aVector = new Vector(3, 3);
/*     */     }
/*  57 */     aVector.addElement(paramProfileActivator);
/*     */   }
/*     */ 
/*     */   public static void unregisterDeferral(ProfileActivator paramProfileActivator)
/*     */   {
/*  69 */     if (!deferring) {
/*  70 */       return;
/*     */     }
/*  72 */     if (aVector == null) {
/*  73 */       return;
/*     */     }
/*  75 */     aVector.removeElement(paramProfileActivator);
/*     */   }
/*     */ 
/*     */   public static void activateProfiles()
/*     */   {
/*  88 */     deferring = false;
/*  89 */     if (aVector == null) {
/*  90 */       return;
/*     */     }
/*  92 */     int i = aVector.size();
/*  93 */     for (ProfileActivator localProfileActivator : aVector) {
/*     */       try {
/*  95 */         localProfileActivator.activate();
/*     */       }
/*     */       catch (ProfileDataException localProfileDataException)
/*     */       {
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 114 */     aVector.removeAllElements();
/* 115 */     aVector = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.cmm.ProfileDeferralMgr
 * JD-Core Version:    0.6.2
 */