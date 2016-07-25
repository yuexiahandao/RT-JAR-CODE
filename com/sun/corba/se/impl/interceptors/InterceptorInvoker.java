/*     */ package com.sun.corba.se.impl.interceptors;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.oa.ObjectAdapter;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.PortableInterceptor.ClientRequestInterceptor;
/*     */ import org.omg.PortableInterceptor.ForwardRequest;
/*     */ import org.omg.PortableInterceptor.IORInterceptor;
/*     */ import org.omg.PortableInterceptor.IORInterceptor_3_0;
/*     */ import org.omg.PortableInterceptor.ObjectReferenceTemplate;
/*     */ import org.omg.PortableInterceptor.ServerRequestInterceptor;
/*     */ 
/*     */ public class InterceptorInvoker
/*     */ {
/*     */   private ORB orb;
/*     */   private InterceptorList interceptorList;
/*  68 */   private boolean enabled = false;
/*     */   private PICurrent current;
/*     */ 
/*     */   InterceptorInvoker(ORB paramORB, InterceptorList paramInterceptorList, PICurrent paramPICurrent)
/*     */   {
/*  85 */     this.orb = paramORB;
/*  86 */     this.interceptorList = paramInterceptorList;
/*  87 */     this.enabled = false;
/*  88 */     this.current = paramPICurrent;
/*     */   }
/*     */ 
/*     */   void setEnabled(boolean paramBoolean)
/*     */   {
/*  95 */     this.enabled = paramBoolean;
/*     */   }
/*     */ 
/*     */   void objectAdapterCreated(ObjectAdapter paramObjectAdapter)
/*     */   {
/* 110 */     if (this.enabled)
/*     */     {
/* 112 */       IORInfoImpl localIORInfoImpl = new IORInfoImpl(paramObjectAdapter);
/*     */ 
/* 115 */       IORInterceptor[] arrayOfIORInterceptor = (IORInterceptor[])this.interceptorList.getInterceptors(2);
/*     */ 
/* 118 */       int i = arrayOfIORInterceptor.length;
/*     */       IORInterceptor localIORInterceptor;
/* 127 */       for (int j = i - 1; j >= 0; j--) {
/* 128 */         localIORInterceptor = arrayOfIORInterceptor[j];
/*     */         try {
/* 130 */           localIORInterceptor.establish_components(localIORInfoImpl);
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 139 */       localIORInfoImpl.makeStateEstablished();
/*     */ 
/* 141 */       for (j = i - 1; j >= 0; j--) {
/* 142 */         localIORInterceptor = arrayOfIORInterceptor[j];
/* 143 */         if ((localIORInterceptor instanceof IORInterceptor_3_0)) {
/* 144 */           IORInterceptor_3_0 localIORInterceptor_3_0 = (IORInterceptor_3_0)localIORInterceptor;
/*     */ 
/* 147 */           localIORInterceptor_3_0.components_established(localIORInfoImpl);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 155 */       localIORInfoImpl.makeStateDone();
/*     */     }
/*     */   }
/*     */ 
/*     */   void adapterManagerStateChanged(int paramInt, short paramShort)
/*     */   {
/* 161 */     if (this.enabled) {
/* 162 */       IORInterceptor[] arrayOfIORInterceptor = (IORInterceptor[])this.interceptorList.getInterceptors(2);
/*     */ 
/* 165 */       int i = arrayOfIORInterceptor.length;
/*     */ 
/* 167 */       for (int j = i - 1; j >= 0; j--)
/*     */         try {
/* 169 */           IORInterceptor localIORInterceptor = arrayOfIORInterceptor[j];
/* 170 */           if ((localIORInterceptor instanceof IORInterceptor_3_0)) {
/* 171 */             IORInterceptor_3_0 localIORInterceptor_3_0 = (IORInterceptor_3_0)localIORInterceptor;
/* 172 */             localIORInterceptor_3_0.adapter_manager_state_changed(paramInt, paramShort);
/*     */           }
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   void adapterStateChanged(ObjectReferenceTemplate[] paramArrayOfObjectReferenceTemplate, short paramShort)
/*     */   {
/* 185 */     if (this.enabled) {
/* 186 */       IORInterceptor[] arrayOfIORInterceptor = (IORInterceptor[])this.interceptorList.getInterceptors(2);
/*     */ 
/* 189 */       int i = arrayOfIORInterceptor.length;
/*     */ 
/* 191 */       for (int j = i - 1; j >= 0; j--)
/*     */         try {
/* 193 */           IORInterceptor localIORInterceptor = arrayOfIORInterceptor[j];
/* 194 */           if ((localIORInterceptor instanceof IORInterceptor_3_0)) {
/* 195 */             IORInterceptor_3_0 localIORInterceptor_3_0 = (IORInterceptor_3_0)localIORInterceptor;
/* 196 */             localIORInterceptor_3_0.adapter_state_changed(paramArrayOfObjectReferenceTemplate, paramShort);
/*     */           }
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   void invokeClientInterceptorStartingPoint(ClientRequestInfoImpl paramClientRequestInfoImpl)
/*     */   {
/* 216 */     if (this.enabled)
/*     */     {
/*     */       try
/*     */       {
/* 221 */         this.current.pushSlotTable();
/* 222 */         paramClientRequestInfoImpl.setPICurrentPushed(true);
/* 223 */         paramClientRequestInfoImpl.setCurrentExecutionPoint(0);
/*     */ 
/* 226 */         ClientRequestInterceptor[] arrayOfClientRequestInterceptor = (ClientRequestInterceptor[])this.interceptorList.getInterceptors(0);
/*     */ 
/* 229 */         int i = arrayOfClientRequestInterceptor.length;
/*     */ 
/* 234 */         int j = i;
/* 235 */         int k = 1;
/*     */ 
/* 243 */         for (int m = 0; (k != 0) && (m < i); m++) {
/*     */           try {
/* 245 */             arrayOfClientRequestInterceptor[m].send_request(paramClientRequestInfoImpl);
/*     */           }
/*     */           catch (ForwardRequest localForwardRequest)
/*     */           {
/* 266 */             j = m;
/* 267 */             paramClientRequestInfoImpl.setForwardRequest(localForwardRequest);
/* 268 */             paramClientRequestInfoImpl.setEndingPointCall(2);
/*     */ 
/* 270 */             paramClientRequestInfoImpl.setReplyStatus((short)3);
/*     */ 
/* 272 */             updateClientRequestDispatcherForward(paramClientRequestInfoImpl);
/*     */ 
/* 279 */             k = 0;
/*     */           }
/*     */           catch (SystemException localSystemException)
/*     */           {
/* 286 */             j = m;
/* 287 */             paramClientRequestInfoImpl.setEndingPointCall(1);
/*     */ 
/* 289 */             paramClientRequestInfoImpl.setReplyStatus((short)1);
/* 290 */             paramClientRequestInfoImpl.setException(localSystemException);
/*     */ 
/* 297 */             k = 0;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 302 */         paramClientRequestInfoImpl.setFlowStackIndex(j);
/*     */       }
/*     */       finally
/*     */       {
/* 306 */         this.current.resetSlotTable();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void invokeClientInterceptorEndingPoint(ClientRequestInfoImpl paramClientRequestInfoImpl)
/*     */   {
/* 317 */     if (this.enabled)
/*     */     {
/*     */       try
/*     */       {
/* 322 */         paramClientRequestInfoImpl.setCurrentExecutionPoint(2);
/*     */ 
/* 325 */         ClientRequestInterceptor[] arrayOfClientRequestInterceptor = (ClientRequestInterceptor[])this.interceptorList.getInterceptors(0);
/*     */ 
/* 328 */         int i = paramClientRequestInfoImpl.getFlowStackIndex();
/*     */ 
/* 332 */         int j = paramClientRequestInfoImpl.getEndingPointCall();
/*     */ 
/* 336 */         if ((j == 0) && (paramClientRequestInfoImpl.getIsOneWay()))
/*     */         {
/* 340 */           j = 2;
/* 341 */           paramClientRequestInfoImpl.setEndingPointCall(j);
/*     */         }
/*     */ 
/* 349 */         for (int k = i - 1; k >= 0; k--) {
/*     */           try
/*     */           {
/* 352 */             switch (j) {
/*     */             case 0:
/* 354 */               arrayOfClientRequestInterceptor[k].receive_reply(paramClientRequestInfoImpl);
/* 355 */               break;
/*     */             case 1:
/* 357 */               arrayOfClientRequestInterceptor[k].receive_exception(paramClientRequestInfoImpl);
/* 358 */               break;
/*     */             case 2:
/* 360 */               arrayOfClientRequestInterceptor[k].receive_other(paramClientRequestInfoImpl);
/*     */             }
/*     */ 
/*     */           }
/*     */           catch (ForwardRequest localForwardRequest)
/*     */           {
/* 369 */             j = 2;
/*     */ 
/* 371 */             paramClientRequestInfoImpl.setEndingPointCall(j);
/* 372 */             paramClientRequestInfoImpl.setReplyStatus((short)3);
/* 373 */             paramClientRequestInfoImpl.setForwardRequest(localForwardRequest);
/* 374 */             updateClientRequestDispatcherForward(paramClientRequestInfoImpl);
/*     */           }
/*     */           catch (SystemException localSystemException)
/*     */           {
/* 381 */             j = 1;
/*     */ 
/* 383 */             paramClientRequestInfoImpl.setEndingPointCall(j);
/* 384 */             paramClientRequestInfoImpl.setReplyStatus((short)1);
/* 385 */             paramClientRequestInfoImpl.setException(localSystemException);
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/* 392 */         if ((paramClientRequestInfoImpl != null) && (paramClientRequestInfoImpl.isPICurrentPushed()))
/* 393 */           this.current.popSlotTable();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void invokeServerInterceptorStartingPoint(ServerRequestInfoImpl paramServerRequestInfoImpl)
/*     */   {
/* 411 */     if (this.enabled)
/*     */       try
/*     */       {
/* 414 */         this.current.pushSlotTable();
/* 415 */         paramServerRequestInfoImpl.setSlotTable(this.current.getSlotTable());
/*     */ 
/* 419 */         this.current.pushSlotTable();
/*     */ 
/* 421 */         paramServerRequestInfoImpl.setCurrentExecutionPoint(0);
/*     */ 
/* 424 */         ServerRequestInterceptor[] arrayOfServerRequestInterceptor = (ServerRequestInterceptor[])this.interceptorList.getInterceptors(1);
/*     */ 
/* 427 */         int i = arrayOfServerRequestInterceptor.length;
/*     */ 
/* 432 */         int j = i;
/* 433 */         int k = 1;
/*     */ 
/* 437 */         for (int m = 0; (k != 0) && (m < i); m++) {
/*     */           try
/*     */           {
/* 440 */             arrayOfServerRequestInterceptor[m].receive_request_service_contexts(paramServerRequestInfoImpl);
/*     */           }
/*     */           catch (ForwardRequest localForwardRequest)
/*     */           {
/* 448 */             j = m;
/* 449 */             paramServerRequestInfoImpl.setForwardRequest(localForwardRequest);
/* 450 */             paramServerRequestInfoImpl.setIntermediatePointCall(1);
/*     */ 
/* 452 */             paramServerRequestInfoImpl.setEndingPointCall(2);
/*     */ 
/* 454 */             paramServerRequestInfoImpl.setReplyStatus((short)3);
/*     */ 
/* 461 */             k = 0;
/*     */           }
/*     */           catch (SystemException localSystemException)
/*     */           {
/* 469 */             j = m;
/* 470 */             paramServerRequestInfoImpl.setException(localSystemException);
/* 471 */             paramServerRequestInfoImpl.setIntermediatePointCall(1);
/*     */ 
/* 473 */             paramServerRequestInfoImpl.setEndingPointCall(1);
/*     */ 
/* 475 */             paramServerRequestInfoImpl.setReplyStatus((short)1);
/*     */ 
/* 482 */             k = 0;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 488 */         paramServerRequestInfoImpl.setFlowStackIndex(j);
/*     */       }
/*     */       finally
/*     */       {
/* 493 */         this.current.popSlotTable();
/*     */       }
/*     */   }
/*     */ 
/*     */   void invokeServerInterceptorIntermediatePoint(ServerRequestInfoImpl paramServerRequestInfoImpl)
/*     */   {
/* 505 */     int i = paramServerRequestInfoImpl.getIntermediatePointCall();
/*     */ 
/* 507 */     if ((this.enabled) && (i != 1))
/*     */     {
/* 513 */       paramServerRequestInfoImpl.setCurrentExecutionPoint(1);
/*     */ 
/* 516 */       ServerRequestInterceptor[] arrayOfServerRequestInterceptor = (ServerRequestInterceptor[])this.interceptorList.getInterceptors(1);
/*     */ 
/* 520 */       int j = arrayOfServerRequestInterceptor.length;
/*     */ 
/* 524 */       for (int k = 0; k < j; k++)
/*     */         try
/*     */         {
/* 527 */           arrayOfServerRequestInterceptor[k].receive_request(paramServerRequestInfoImpl);
/*     */         }
/*     */         catch (ForwardRequest localForwardRequest)
/*     */         {
/* 535 */           paramServerRequestInfoImpl.setForwardRequest(localForwardRequest);
/* 536 */           paramServerRequestInfoImpl.setEndingPointCall(2);
/*     */ 
/* 538 */           paramServerRequestInfoImpl.setReplyStatus((short)3);
/* 539 */           break;
/*     */         }
/*     */         catch (SystemException localSystemException)
/*     */         {
/* 547 */           paramServerRequestInfoImpl.setException(localSystemException);
/* 548 */           paramServerRequestInfoImpl.setEndingPointCall(1);
/*     */ 
/* 550 */           paramServerRequestInfoImpl.setReplyStatus((short)1);
/* 551 */           break;
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   void invokeServerInterceptorEndingPoint(ServerRequestInfoImpl paramServerRequestInfoImpl)
/*     */   {
/* 563 */     if (this.enabled)
/*     */     {
/*     */       try
/*     */       {
/* 576 */         ServerRequestInterceptor[] arrayOfServerRequestInterceptor = (ServerRequestInterceptor[])this.interceptorList.getInterceptors(1);
/*     */ 
/* 579 */         int i = paramServerRequestInfoImpl.getFlowStackIndex();
/*     */ 
/* 583 */         int j = paramServerRequestInfoImpl.getEndingPointCall();
/*     */ 
/* 587 */         for (int k = i - 1; k >= 0; k--) {
/*     */           try {
/* 589 */             switch (j) {
/*     */             case 0:
/* 591 */               arrayOfServerRequestInterceptor[k].send_reply(paramServerRequestInfoImpl);
/* 592 */               break;
/*     */             case 1:
/* 594 */               arrayOfServerRequestInterceptor[k].send_exception(paramServerRequestInfoImpl);
/* 595 */               break;
/*     */             case 2:
/* 597 */               arrayOfServerRequestInterceptor[k].send_other(paramServerRequestInfoImpl);
/*     */             }
/*     */ 
/*     */           }
/*     */           catch (ForwardRequest localForwardRequest)
/*     */           {
/* 605 */             j = 2;
/*     */ 
/* 607 */             paramServerRequestInfoImpl.setEndingPointCall(j);
/* 608 */             paramServerRequestInfoImpl.setForwardRequest(localForwardRequest);
/* 609 */             paramServerRequestInfoImpl.setReplyStatus((short)3);
/* 610 */             paramServerRequestInfoImpl.setForwardRequestRaisedInEnding();
/*     */           }
/*     */           catch (SystemException localSystemException)
/*     */           {
/* 616 */             j = 1;
/*     */ 
/* 618 */             paramServerRequestInfoImpl.setEndingPointCall(j);
/* 619 */             paramServerRequestInfoImpl.setException(localSystemException);
/* 620 */             paramServerRequestInfoImpl.setReplyStatus((short)1);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 626 */         paramServerRequestInfoImpl.setAlreadyExecuted(true);
/*     */       }
/*     */       finally
/*     */       {
/* 630 */         this.current.popSlotTable();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void updateClientRequestDispatcherForward(ClientRequestInfoImpl paramClientRequestInfoImpl)
/*     */   {
/* 647 */     ForwardRequest localForwardRequest = paramClientRequestInfoImpl.getForwardRequestException();
/*     */ 
/* 653 */     if (localForwardRequest != null) {
/* 654 */       org.omg.CORBA.Object localObject = localForwardRequest.forward;
/*     */ 
/* 657 */       IOR localIOR = ORBUtility.getIOR(localObject);
/* 658 */       paramClientRequestInfoImpl.setLocatedIOR(localIOR);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.interceptors.InterceptorInvoker
 * JD-Core Version:    0.6.2
 */