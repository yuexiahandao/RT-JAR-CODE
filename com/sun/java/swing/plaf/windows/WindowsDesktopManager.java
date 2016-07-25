/*    */ package com.sun.java.swing.plaf.windows;
/*    */ 
/*    */ import java.beans.PropertyVetoException;
/*    */ import java.io.Serializable;
/*    */ import java.lang.ref.WeakReference;
/*    */ import javax.swing.DefaultDesktopManager;
/*    */ import javax.swing.JInternalFrame;
/*    */ import javax.swing.plaf.UIResource;
/*    */ 
/*    */ public class WindowsDesktopManager extends DefaultDesktopManager
/*    */   implements Serializable, UIResource
/*    */ {
/*    */   private WeakReference<JInternalFrame> currentFrameRef;
/*    */ 
/*    */   public void activateFrame(JInternalFrame paramJInternalFrame)
/*    */   {
/* 63 */     Object localObject = this.currentFrameRef != null ? (JInternalFrame)this.currentFrameRef.get() : null;
/*    */     try
/*    */     {
/* 66 */       super.activateFrame(paramJInternalFrame);
/* 67 */       if ((localObject != null) && (paramJInternalFrame != localObject))
/*    */       {
/* 70 */         if ((localObject.isMaximum()) && (paramJInternalFrame.getClientProperty("JInternalFrame.frameType") != "optionDialog"))
/*    */         {
/* 76 */           if (!localObject.isIcon()) {
/* 77 */             localObject.setMaximum(false);
/* 78 */             if (paramJInternalFrame.isMaximizable()) {
/* 79 */               if (!paramJInternalFrame.isMaximum())
/* 80 */                 paramJInternalFrame.setMaximum(true);
/* 81 */               else if ((paramJInternalFrame.isMaximum()) && (paramJInternalFrame.isIcon()))
/* 82 */                 paramJInternalFrame.setIcon(false);
/*    */               else {
/* 84 */                 paramJInternalFrame.setMaximum(false);
/*    */               }
/*    */             }
/*    */           }
/*    */         }
/* 89 */         if (localObject.isSelected()) {
/* 90 */           localObject.setSelected(false);
/*    */         }
/*    */       }
/*    */ 
/* 94 */       if (!paramJInternalFrame.isSelected())
/* 95 */         paramJInternalFrame.setSelected(true);
/*    */     } catch (PropertyVetoException localPropertyVetoException) {
/*    */     }
/* 98 */     if (paramJInternalFrame != localObject)
/* 99 */       this.currentFrameRef = new WeakReference(paramJInternalFrame);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsDesktopManager
 * JD-Core Version:    0.6.2
 */