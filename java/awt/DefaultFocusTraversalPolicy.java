/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.peer.ComponentPeer;
/*     */ 
/*     */ public class DefaultFocusTraversalPolicy extends ContainerOrderFocusTraversalPolicy
/*     */ {
/*     */   private static final long serialVersionUID = 8876966522510157497L;
/*     */ 
/*     */   protected boolean accept(Component paramComponent)
/*     */   {
/*  97 */     if ((!paramComponent.isVisible()) || (!paramComponent.isDisplayable()) || (!paramComponent.isEnabled()))
/*     */     {
/* 100 */       return false;
/*     */     }
/*     */ 
/* 106 */     if (!(paramComponent instanceof Window)) {
/* 107 */       for (Container localContainer = paramComponent.getParent(); 
/* 108 */         localContainer != null; 
/* 109 */         localContainer = localContainer.getParent())
/*     */       {
/* 111 */         if ((!localContainer.isEnabled()) && (!localContainer.isLightweight())) {
/* 112 */           return false;
/*     */         }
/* 114 */         if ((localContainer instanceof Window))
/*     */         {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/* 120 */     boolean bool = paramComponent.isFocusable();
/* 121 */     if (paramComponent.isFocusTraversableOverridden()) {
/* 122 */       return bool;
/*     */     }
/*     */ 
/* 125 */     ComponentPeer localComponentPeer = paramComponent.getPeer();
/* 126 */     return (localComponentPeer != null) && (localComponentPeer.isFocusable());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.DefaultFocusTraversalPolicy
 * JD-Core Version:    0.6.2
 */