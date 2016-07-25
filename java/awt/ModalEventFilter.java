/*     */ package java.awt;
/*     */ 
/*     */ import sun.awt.AppContext;
/*     */ import sun.awt.ModalExclude;
/*     */ 
/*     */ abstract class ModalEventFilter
/*     */   implements EventFilter
/*     */ {
/*     */   protected Dialog modalDialog;
/*     */   protected boolean disabled;
/*     */ 
/*     */   protected ModalEventFilter(Dialog paramDialog)
/*     */   {
/*  37 */     this.modalDialog = paramDialog;
/*  38 */     this.disabled = false;
/*     */   }
/*     */ 
/*     */   Dialog getModalDialog() {
/*  42 */     return this.modalDialog;
/*     */   }
/*     */ 
/*     */   public EventFilter.FilterAction acceptEvent(AWTEvent paramAWTEvent) {
/*  46 */     if ((this.disabled) || (!this.modalDialog.isVisible())) {
/*  47 */       return EventFilter.FilterAction.ACCEPT;
/*     */     }
/*  49 */     int i = paramAWTEvent.getID();
/*  50 */     if (((i >= 500) && (i <= 507)) || ((i >= 1001) && (i <= 1001)) || (i == 201))
/*     */     {
/*  56 */       Object localObject1 = paramAWTEvent.getSource();
/*  57 */       if (!(localObject1 instanceof ModalExclude))
/*     */       {
/*  60 */         if ((localObject1 instanceof Component)) {
/*  61 */           Object localObject2 = (Component)localObject1;
/*  62 */           while ((localObject2 != null) && (!(localObject2 instanceof Window))) {
/*  63 */             localObject2 = ((Component)localObject2).getParent_NoClientCode();
/*     */           }
/*  65 */           if (localObject2 != null)
/*  66 */             return acceptWindow((Window)localObject2);
/*     */         }
/*     */       }
/*     */     }
/*  70 */     return EventFilter.FilterAction.ACCEPT;
/*     */   }
/*     */ 
/*     */   protected abstract EventFilter.FilterAction acceptWindow(Window paramWindow);
/*     */ 
/*     */   void disable()
/*     */   {
/*  81 */     this.disabled = true;
/*     */   }
/*     */ 
/*     */   int compareTo(ModalEventFilter paramModalEventFilter) {
/*  85 */     Dialog localDialog1 = paramModalEventFilter.getModalDialog();
/*     */ 
/*  88 */     Object localObject = this.modalDialog;
/*  89 */     while (localObject != null) {
/*  90 */       if (localObject == localDialog1) {
/*  91 */         return 1;
/*     */       }
/*  93 */       localObject = ((Component)localObject).getParent_NoClientCode();
/*     */     }
/*  95 */     localObject = localDialog1;
/*  96 */     while (localObject != null) {
/*  97 */       if (localObject == this.modalDialog) {
/*  98 */         return -1;
/*     */       }
/* 100 */       localObject = ((Component)localObject).getParent_NoClientCode();
/*     */     }
/*     */ 
/* 103 */     Dialog localDialog2 = this.modalDialog.getModalBlocker();
/* 104 */     while (localDialog2 != null) {
/* 105 */       if (localDialog2 == localDialog1) {
/* 106 */         return -1;
/*     */       }
/* 108 */       localDialog2 = localDialog2.getModalBlocker();
/*     */     }
/* 110 */     localDialog2 = localDialog1.getModalBlocker();
/* 111 */     while (localDialog2 != null) {
/* 112 */       if (localDialog2 == this.modalDialog) {
/* 113 */         return 1;
/*     */       }
/* 115 */       localDialog2 = localDialog2.getModalBlocker();
/*     */     }
/*     */ 
/* 118 */     return this.modalDialog.getModalityType().compareTo(localDialog1.getModalityType());
/*     */   }
/*     */ 
/*     */   static ModalEventFilter createFilterForDialog(Dialog paramDialog) {
/* 122 */     switch (1.$SwitchMap$java$awt$Dialog$ModalityType[paramDialog.getModalityType().ordinal()]) { case 1:
/* 123 */       return new DocumentModalEventFilter(paramDialog);
/*     */     case 2:
/* 124 */       return new ApplicationModalEventFilter(paramDialog);
/*     */     case 3:
/* 125 */       return new ToolkitModalEventFilter(paramDialog);
/*     */     }
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */   private static class ApplicationModalEventFilter extends ModalEventFilter
/*     */   {
/*     */     private AppContext appContext;
/*     */ 
/*     */     ApplicationModalEventFilter(Dialog paramDialog)
/*     */     {
/* 161 */       super();
/* 162 */       this.appContext = paramDialog.appContext;
/*     */     }
/*     */ 
/*     */     protected EventFilter.FilterAction acceptWindow(Window paramWindow) {
/* 166 */       if (paramWindow.isModalExcluded(Dialog.ModalExclusionType.APPLICATION_EXCLUDE)) {
/* 167 */         return EventFilter.FilterAction.ACCEPT;
/*     */       }
/* 169 */       if (paramWindow.appContext == this.appContext) {
/* 170 */         while (paramWindow != null) {
/* 171 */           if (paramWindow == this.modalDialog) {
/* 172 */             return EventFilter.FilterAction.ACCEPT_IMMEDIATELY;
/*     */           }
/* 174 */           paramWindow = paramWindow.getOwner();
/*     */         }
/* 176 */         return EventFilter.FilterAction.REJECT;
/*     */       }
/* 178 */       return EventFilter.FilterAction.ACCEPT;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class DocumentModalEventFilter extends ModalEventFilter
/*     */   {
/*     */     private Window documentRoot;
/*     */ 
/*     */     DocumentModalEventFilter(Dialog paramDialog) {
/* 187 */       super();
/* 188 */       this.documentRoot = paramDialog.getDocumentRoot();
/*     */     }
/*     */ 
/*     */     protected EventFilter.FilterAction acceptWindow(Window paramWindow)
/*     */     {
/* 194 */       if (paramWindow.isModalExcluded(Dialog.ModalExclusionType.APPLICATION_EXCLUDE)) {
/* 195 */         Window localWindow = this.modalDialog.getOwner();
/* 196 */         while (localWindow != null) {
/* 197 */           if (localWindow == paramWindow) {
/* 198 */             return EventFilter.FilterAction.REJECT;
/*     */           }
/* 200 */           localWindow = localWindow.getOwner();
/*     */         }
/* 202 */         return EventFilter.FilterAction.ACCEPT;
/*     */       }
/* 204 */       while (paramWindow != null) {
/* 205 */         if (paramWindow == this.modalDialog) {
/* 206 */           return EventFilter.FilterAction.ACCEPT_IMMEDIATELY;
/*     */         }
/* 208 */         if (paramWindow == this.documentRoot) {
/* 209 */           return EventFilter.FilterAction.REJECT;
/*     */         }
/* 211 */         paramWindow = paramWindow.getOwner();
/*     */       }
/* 213 */       return EventFilter.FilterAction.ACCEPT;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ToolkitModalEventFilter extends ModalEventFilter
/*     */   {
/*     */     private AppContext appContext;
/*     */ 
/*     */     ToolkitModalEventFilter(Dialog paramDialog)
/*     */     {
/* 135 */       super();
/* 136 */       this.appContext = paramDialog.appContext;
/*     */     }
/*     */ 
/*     */     protected EventFilter.FilterAction acceptWindow(Window paramWindow) {
/* 140 */       if (paramWindow.isModalExcluded(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE)) {
/* 141 */         return EventFilter.FilterAction.ACCEPT;
/*     */       }
/* 143 */       if (paramWindow.appContext != this.appContext) {
/* 144 */         return EventFilter.FilterAction.REJECT;
/*     */       }
/* 146 */       while (paramWindow != null) {
/* 147 */         if (paramWindow == this.modalDialog) {
/* 148 */           return EventFilter.FilterAction.ACCEPT_IMMEDIATELY;
/*     */         }
/* 150 */         paramWindow = paramWindow.getOwner();
/*     */       }
/* 152 */       return EventFilter.FilterAction.REJECT;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.ModalEventFilter
 * JD-Core Version:    0.6.2
 */