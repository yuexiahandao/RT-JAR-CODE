/*     */ package javax.print;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.HeadlessException;
/*     */ import java.awt.Rectangle;
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.AttributeSet;
/*     */ import javax.print.attribute.PrintRequestAttributeSet;
/*     */ import javax.print.attribute.standard.Destination;
/*     */ import javax.print.attribute.standard.Fidelity;
/*     */ import sun.print.ServiceDialog;
/*     */ import sun.print.SunAlternateMedia;
/*     */ 
/*     */ public class ServiceUI
/*     */ {
/*     */   public static PrintService printDialog(GraphicsConfiguration paramGraphicsConfiguration, int paramInt1, int paramInt2, PrintService[] paramArrayOfPrintService, PrintService paramPrintService, DocFlavor paramDocFlavor, PrintRequestAttributeSet paramPrintRequestAttributeSet)
/*     */     throws HeadlessException
/*     */   {
/* 162 */     int i = -1;
/*     */ 
/* 164 */     if (GraphicsEnvironment.isHeadless())
/* 165 */       throw new HeadlessException();
/* 166 */     if ((paramArrayOfPrintService == null) || (paramArrayOfPrintService.length == 0)) {
/* 167 */       throw new IllegalArgumentException("services must be non-null and non-empty");
/*     */     }
/* 169 */     if (paramPrintRequestAttributeSet == null) {
/* 170 */       throw new IllegalArgumentException("attributes must be non-null");
/*     */     }
/*     */ 
/* 173 */     if (paramPrintService != null) {
/* 174 */       for (int j = 0; j < paramArrayOfPrintService.length; j++) {
/* 175 */         if (paramArrayOfPrintService[j].equals(paramPrintService)) {
/* 176 */           i = j;
/* 177 */           break;
/*     */         }
/*     */       }
/*     */ 
/* 181 */       if (i < 0)
/* 182 */         throw new IllegalArgumentException("services must contain defaultService");
/*     */     }
/*     */     else
/*     */     {
/* 186 */       i = 0;
/*     */     }
/*     */ 
/* 191 */     Component localComponent = null;
/*     */ 
/* 193 */     Rectangle localRectangle1 = paramGraphicsConfiguration == null ? GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds() : paramGraphicsConfiguration.getBounds();
/*     */     ServiceDialog localServiceDialog;
/* 198 */     if ((localComponent instanceof Frame)) {
/* 199 */       localServiceDialog = new ServiceDialog(paramGraphicsConfiguration, paramInt1 + localRectangle1.x, paramInt2 + localRectangle1.y, paramArrayOfPrintService, i, paramDocFlavor, paramPrintRequestAttributeSet, (Frame)localComponent);
/*     */     }
/*     */     else
/*     */     {
/* 206 */       localServiceDialog = new ServiceDialog(paramGraphicsConfiguration, paramInt1 + localRectangle1.x, paramInt2 + localRectangle1.y, paramArrayOfPrintService, i, paramDocFlavor, paramPrintRequestAttributeSet, (Dialog)localComponent);
/*     */     }
/*     */ 
/* 213 */     Rectangle localRectangle2 = localServiceDialog.getBounds();
/*     */ 
/* 216 */     GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
/* 217 */     GraphicsDevice[] arrayOfGraphicsDevice = localGraphicsEnvironment.getScreenDevices();
/* 218 */     for (int k = 0; k < arrayOfGraphicsDevice.length; k++) {
/* 219 */       localRectangle1 = localRectangle1.union(arrayOfGraphicsDevice[k].getDefaultConfiguration().getBounds());
/*     */     }
/*     */ 
/* 224 */     if (!localRectangle1.contains(localRectangle2))
/*     */     {
/* 226 */       localServiceDialog.setLocationRelativeTo(localComponent);
/*     */     }
/* 228 */     localServiceDialog.show();
/*     */ 
/* 230 */     if (localServiceDialog.getStatus() == 1) {
/* 231 */       PrintRequestAttributeSet localPrintRequestAttributeSet = localServiceDialog.getAttributes();
/* 232 */       Destination localDestination = Destination.class;
/* 233 */       SunAlternateMedia localSunAlternateMedia = SunAlternateMedia.class;
/* 234 */       Fidelity localFidelity1 = Fidelity.class;
/*     */ 
/* 236 */       if ((paramPrintRequestAttributeSet.containsKey(localDestination)) && (!localPrintRequestAttributeSet.containsKey(localDestination)))
/*     */       {
/* 239 */         paramPrintRequestAttributeSet.remove(localDestination);
/*     */       }
/*     */ 
/* 242 */       if ((paramPrintRequestAttributeSet.containsKey(localSunAlternateMedia)) && (!localPrintRequestAttributeSet.containsKey(localSunAlternateMedia)))
/*     */       {
/* 245 */         paramPrintRequestAttributeSet.remove(localSunAlternateMedia);
/*     */       }
/*     */ 
/* 248 */       paramPrintRequestAttributeSet.addAll(localPrintRequestAttributeSet);
/*     */ 
/* 250 */       Fidelity localFidelity2 = (Fidelity)paramPrintRequestAttributeSet.get(localFidelity1);
/* 251 */       if ((localFidelity2 != null) && 
/* 252 */         (localFidelity2 == Fidelity.FIDELITY_TRUE)) {
/* 253 */         removeUnsupportedAttributes(localServiceDialog.getPrintService(), paramDocFlavor, paramPrintRequestAttributeSet);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 259 */     return localServiceDialog.getPrintService();
/*     */   }
/*     */ 
/*     */   private static void removeUnsupportedAttributes(PrintService paramPrintService, DocFlavor paramDocFlavor, AttributeSet paramAttributeSet)
/*     */   {
/* 312 */     AttributeSet localAttributeSet = paramPrintService.getUnsupportedAttributes(paramDocFlavor, paramAttributeSet);
/*     */ 
/* 315 */     if (localAttributeSet != null) {
/* 316 */       Attribute[] arrayOfAttribute = localAttributeSet.toArray();
/*     */ 
/* 318 */       for (int i = 0; i < arrayOfAttribute.length; i++) {
/* 319 */         Class localClass = arrayOfAttribute[i].getCategory();
/*     */ 
/* 321 */         if (paramPrintService.isAttributeCategorySupported(localClass)) {
/* 322 */           Attribute localAttribute = (Attribute)paramPrintService.getDefaultAttributeValue(localClass);
/*     */ 
/* 325 */           if (localAttribute != null)
/* 326 */             paramAttributeSet.add(localAttribute);
/*     */           else
/* 328 */             paramAttributeSet.remove(localClass);
/*     */         }
/*     */         else {
/* 331 */           paramAttributeSet.remove(localClass);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.ServiceUI
 * JD-Core Version:    0.6.2
 */