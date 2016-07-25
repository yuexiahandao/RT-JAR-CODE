/*    */ package org.omg.CosNaming;
/*    */ 
/*    */ import java.util.Dictionary;
/*    */ import java.util.Hashtable;
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.BAD_OPERATION;
/*    */ import org.omg.CORBA.CompletionStatus;
/*    */ import org.omg.CORBA.DynamicImplementation;
/*    */ import org.omg.CORBA.NVList;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.ServerRequest;
/*    */ import org.omg.CORBA.TCKind;
/*    */ import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
/*    */ import org.omg.CosNaming.NamingContextPackage.AlreadyBoundHelper;
/*    */ import org.omg.CosNaming.NamingContextPackage.CannotProceed;
/*    */ import org.omg.CosNaming.NamingContextPackage.CannotProceedHelper;
/*    */ import org.omg.CosNaming.NamingContextPackage.InvalidName;
/*    */ import org.omg.CosNaming.NamingContextPackage.InvalidNameHelper;
/*    */ import org.omg.CosNaming.NamingContextPackage.NotEmpty;
/*    */ import org.omg.CosNaming.NamingContextPackage.NotEmptyHelper;
/*    */ import org.omg.CosNaming.NamingContextPackage.NotFound;
/*    */ import org.omg.CosNaming.NamingContextPackage.NotFoundHelper;
/*    */ 
/*    */ public abstract class _NamingContextImplBase extends DynamicImplementation
/*    */   implements NamingContext
/*    */ {
/* 40 */   private static final String[] _type_ids = { "IDL:omg.org/CosNaming/NamingContext:1.0" };
/*    */ 
/* 46 */   private static Dictionary _methods = new Hashtable();
/*    */ 
/*    */   public String[] _ids()
/*    */   {
/* 44 */     return (String[])_type_ids.clone();
/*    */   }
/*    */ 
/*    */   public void invoke(ServerRequest paramServerRequest)
/*    */   {
/*    */     NVList localNVList;
/*    */     java.lang.Object localObject1;
/*    */     java.lang.Object localObject2;
/*    */     java.lang.Object localObject3;
/*    */     java.lang.Object localObject5;
/*    */     java.lang.Object localObject7;
/*    */     java.lang.Object localObject6;
/*    */     Any localAny2;
/*    */     java.lang.Object localObject4;
/* 61 */     switch (((Integer)_methods.get(paramServerRequest.op_name())).intValue())
/*    */     {
/*    */     case 0:
/* 64 */       localNVList = _orb().create_list(0);
/* 65 */       localObject1 = _orb().create_any();
/* 66 */       ((Any)localObject1).type(NameHelper.type());
/* 67 */       localNVList.add_value("n", (Any)localObject1, 1);
/* 68 */       localObject2 = _orb().create_any();
/* 69 */       ((Any)localObject2).type(ORB.init().get_primitive_tc(TCKind.tk_objref));
/* 70 */       localNVList.add_value("obj", (Any)localObject2, 1);
/* 71 */       paramServerRequest.params(localNVList);
/*    */ 
/* 73 */       localObject3 = NameHelper.extract((Any)localObject1);
/*    */ 
/* 75 */       localObject5 = ((Any)localObject2).extract_Object();
/*    */       try {
/* 77 */         bind((NameComponent[])localObject3, (org.omg.CORBA.Object)localObject5);
/*    */       }
/*    */       catch (NotFound localNotFound4) {
/* 80 */         localObject7 = _orb().create_any();
/* 81 */         NotFoundHelper.insert((Any)localObject7, localNotFound4);
/* 82 */         paramServerRequest.except((Any)localObject7);
/* 83 */         return;
/*    */       }
/*    */       catch (CannotProceed localCannotProceed4) {
/* 86 */         localObject7 = _orb().create_any();
/* 87 */         CannotProceedHelper.insert((Any)localObject7, localCannotProceed4);
/* 88 */         paramServerRequest.except((Any)localObject7);
/* 89 */         return;
/*    */       }
/*    */       catch (InvalidName localInvalidName4) {
/* 92 */         localObject7 = _orb().create_any();
/* 93 */         InvalidNameHelper.insert((Any)localObject7, localInvalidName4);
/* 94 */         paramServerRequest.except((Any)localObject7);
/* 95 */         return;
/*    */       }
/*    */       catch (AlreadyBound localAlreadyBound2) {
/* 98 */         localObject7 = _orb().create_any();
/* 99 */         AlreadyBoundHelper.insert((Any)localObject7, localAlreadyBound2);
/* 100 */         paramServerRequest.except((Any)localObject7);
/* 101 */         return;
/*    */       }
/* 103 */       Any localAny4 = _orb().create_any();
/* 104 */       localAny4.type(_orb().get_primitive_tc(TCKind.tk_void));
/* 105 */       paramServerRequest.result(localAny4);
/*    */ 
/* 107 */       break;
/*    */     case 1:
/* 110 */       localNVList = _orb().create_list(0);
/* 111 */       localObject1 = _orb().create_any();
/* 112 */       ((Any)localObject1).type(NameHelper.type());
/* 113 */       localNVList.add_value("n", (Any)localObject1, 1);
/* 114 */       localObject2 = _orb().create_any();
/* 115 */       ((Any)localObject2).type(NamingContextHelper.type());
/* 116 */       localNVList.add_value("nc", (Any)localObject2, 1);
/* 117 */       paramServerRequest.params(localNVList);
/*    */ 
/* 119 */       localObject3 = NameHelper.extract((Any)localObject1);
/*    */ 
/* 121 */       localObject5 = NamingContextHelper.extract((Any)localObject2);
/*    */       try {
/* 123 */         bind_context((NameComponent[])localObject3, (NamingContext)localObject5);
/*    */       }
/*    */       catch (NotFound localNotFound5) {
/* 126 */         localObject7 = _orb().create_any();
/* 127 */         NotFoundHelper.insert((Any)localObject7, localNotFound5);
/* 128 */         paramServerRequest.except((Any)localObject7);
/* 129 */         return;
/*    */       }
/*    */       catch (CannotProceed localCannotProceed5) {
/* 132 */         localObject7 = _orb().create_any();
/* 133 */         CannotProceedHelper.insert((Any)localObject7, localCannotProceed5);
/* 134 */         paramServerRequest.except((Any)localObject7);
/* 135 */         return;
/*    */       }
/*    */       catch (InvalidName localInvalidName5) {
/* 138 */         localObject7 = _orb().create_any();
/* 139 */         InvalidNameHelper.insert((Any)localObject7, localInvalidName5);
/* 140 */         paramServerRequest.except((Any)localObject7);
/* 141 */         return;
/*    */       }
/*    */       catch (AlreadyBound localAlreadyBound3) {
/* 144 */         localObject7 = _orb().create_any();
/* 145 */         AlreadyBoundHelper.insert((Any)localObject7, localAlreadyBound3);
/* 146 */         paramServerRequest.except((Any)localObject7);
/* 147 */         return;
/*    */       }
/* 149 */       Any localAny5 = _orb().create_any();
/* 150 */       localAny5.type(_orb().get_primitive_tc(TCKind.tk_void));
/* 151 */       paramServerRequest.result(localAny5);
/*    */ 
/* 153 */       break;
/*    */     case 2:
/* 156 */       localNVList = _orb().create_list(0);
/* 157 */       localObject1 = _orb().create_any();
/* 158 */       ((Any)localObject1).type(NameHelper.type());
/* 159 */       localNVList.add_value("n", (Any)localObject1, 1);
/* 160 */       localObject2 = _orb().create_any();
/* 161 */       ((Any)localObject2).type(ORB.init().get_primitive_tc(TCKind.tk_objref));
/* 162 */       localNVList.add_value("obj", (Any)localObject2, 1);
/* 163 */       paramServerRequest.params(localNVList);
/*    */ 
/* 165 */       localObject3 = NameHelper.extract((Any)localObject1);
/*    */ 
/* 167 */       localObject5 = ((Any)localObject2).extract_Object();
/*    */       try {
/* 169 */         rebind((NameComponent[])localObject3, (org.omg.CORBA.Object)localObject5);
/*    */       }
/*    */       catch (NotFound localNotFound6) {
/* 172 */         localObject7 = _orb().create_any();
/* 173 */         NotFoundHelper.insert((Any)localObject7, localNotFound6);
/* 174 */         paramServerRequest.except((Any)localObject7);
/* 175 */         return;
/*    */       }
/*    */       catch (CannotProceed localCannotProceed6) {
/* 178 */         localObject7 = _orb().create_any();
/* 179 */         CannotProceedHelper.insert((Any)localObject7, localCannotProceed6);
/* 180 */         paramServerRequest.except((Any)localObject7);
/* 181 */         return;
/*    */       }
/*    */       catch (InvalidName localInvalidName6) {
/* 184 */         localObject7 = _orb().create_any();
/* 185 */         InvalidNameHelper.insert((Any)localObject7, localInvalidName6);
/* 186 */         paramServerRequest.except((Any)localObject7);
/* 187 */         return;
/*    */       }
/* 189 */       Any localAny6 = _orb().create_any();
/* 190 */       localAny6.type(_orb().get_primitive_tc(TCKind.tk_void));
/* 191 */       paramServerRequest.result(localAny6);
/*    */ 
/* 193 */       break;
/*    */     case 3:
/* 196 */       localNVList = _orb().create_list(0);
/* 197 */       localObject1 = _orb().create_any();
/* 198 */       ((Any)localObject1).type(NameHelper.type());
/* 199 */       localNVList.add_value("n", (Any)localObject1, 1);
/* 200 */       localObject2 = _orb().create_any();
/* 201 */       ((Any)localObject2).type(NamingContextHelper.type());
/* 202 */       localNVList.add_value("nc", (Any)localObject2, 1);
/* 203 */       paramServerRequest.params(localNVList);
/*    */ 
/* 205 */       localObject3 = NameHelper.extract((Any)localObject1);
/*    */ 
/* 207 */       localObject5 = NamingContextHelper.extract((Any)localObject2);
/*    */       try {
/* 209 */         rebind_context((NameComponent[])localObject3, (NamingContext)localObject5);
/*    */       }
/*    */       catch (NotFound localNotFound7) {
/* 212 */         localObject7 = _orb().create_any();
/* 213 */         NotFoundHelper.insert((Any)localObject7, localNotFound7);
/* 214 */         paramServerRequest.except((Any)localObject7);
/* 215 */         return;
/*    */       }
/*    */       catch (CannotProceed localCannotProceed7) {
/* 218 */         localObject7 = _orb().create_any();
/* 219 */         CannotProceedHelper.insert((Any)localObject7, localCannotProceed7);
/* 220 */         paramServerRequest.except((Any)localObject7);
/* 221 */         return;
/*    */       }
/*    */       catch (InvalidName localInvalidName7) {
/* 224 */         localObject7 = _orb().create_any();
/* 225 */         InvalidNameHelper.insert((Any)localObject7, localInvalidName7);
/* 226 */         paramServerRequest.except((Any)localObject7);
/* 227 */         return;
/*    */       }
/* 229 */       localObject6 = _orb().create_any();
/* 230 */       ((Any)localObject6).type(_orb().get_primitive_tc(TCKind.tk_void));
/* 231 */       paramServerRequest.result((Any)localObject6);
/*    */ 
/* 233 */       break;
/*    */     case 4:
/* 236 */       localNVList = _orb().create_list(0);
/* 237 */       localObject1 = _orb().create_any();
/* 238 */       ((Any)localObject1).type(NameHelper.type());
/* 239 */       localNVList.add_value("n", (Any)localObject1, 1);
/* 240 */       paramServerRequest.params(localNVList);
/*    */ 
/* 242 */       localObject2 = NameHelper.extract((Any)localObject1);
/*    */       try
/*    */       {
/* 245 */         localObject3 = resolve((NameComponent[])localObject2);
/*    */       }
/*    */       catch (NotFound localNotFound2) {
/* 248 */         localObject6 = _orb().create_any();
/* 249 */         NotFoundHelper.insert((Any)localObject6, localNotFound2);
/* 250 */         paramServerRequest.except((Any)localObject6);
/* 251 */         return;
/*    */       }
/*    */       catch (CannotProceed localCannotProceed2) {
/* 254 */         localObject6 = _orb().create_any();
/* 255 */         CannotProceedHelper.insert((Any)localObject6, localCannotProceed2);
/* 256 */         paramServerRequest.except((Any)localObject6);
/* 257 */         return;
/*    */       }
/*    */       catch (InvalidName localInvalidName2) {
/* 260 */         localObject6 = _orb().create_any();
/* 261 */         InvalidNameHelper.insert((Any)localObject6, localInvalidName2);
/* 262 */         paramServerRequest.except((Any)localObject6);
/* 263 */         return;
/*    */       }
/* 265 */       localAny2 = _orb().create_any();
/* 266 */       localAny2.insert_Object((org.omg.CORBA.Object)localObject3);
/* 267 */       paramServerRequest.result(localAny2);
/*    */ 
/* 269 */       break;
/*    */     case 5:
/* 272 */       localNVList = _orb().create_list(0);
/* 273 */       localObject1 = _orb().create_any();
/* 274 */       ((Any)localObject1).type(NameHelper.type());
/* 275 */       localNVList.add_value("n", (Any)localObject1, 1);
/* 276 */       paramServerRequest.params(localNVList);
/*    */ 
/* 278 */       localObject2 = NameHelper.extract((Any)localObject1);
/*    */       try {
/* 280 */         unbind((NameComponent[])localObject2);
/*    */       }
/*    */       catch (NotFound localNotFound1) {
/* 283 */         localAny2 = _orb().create_any();
/* 284 */         NotFoundHelper.insert(localAny2, localNotFound1);
/* 285 */         paramServerRequest.except(localAny2);
/* 286 */         return;
/*    */       }
/*    */       catch (CannotProceed localCannotProceed1) {
/* 289 */         localAny2 = _orb().create_any();
/* 290 */         CannotProceedHelper.insert(localAny2, localCannotProceed1);
/* 291 */         paramServerRequest.except(localAny2);
/* 292 */         return;
/*    */       }
/*    */       catch (InvalidName localInvalidName1) {
/* 295 */         localAny2 = _orb().create_any();
/* 296 */         InvalidNameHelper.insert(localAny2, localInvalidName1);
/* 297 */         paramServerRequest.except(localAny2);
/* 298 */         return;
/*    */       }
/* 300 */       localObject4 = _orb().create_any();
/* 301 */       ((Any)localObject4).type(_orb().get_primitive_tc(TCKind.tk_void));
/* 302 */       paramServerRequest.result((Any)localObject4);
/*    */ 
/* 304 */       break;
/*    */     case 6:
/* 307 */       localNVList = _orb().create_list(0);
/* 308 */       localObject1 = _orb().create_any();
/* 309 */       ((Any)localObject1).type(ORB.init().get_primitive_tc(TCKind.tk_ulong));
/* 310 */       localNVList.add_value("how_many", (Any)localObject1, 1);
/* 311 */       localObject2 = _orb().create_any();
/* 312 */       ((Any)localObject2).type(BindingListHelper.type());
/* 313 */       localNVList.add_value("bl", (Any)localObject2, 2);
/* 314 */       localObject4 = _orb().create_any();
/* 315 */       ((Any)localObject4).type(BindingIteratorHelper.type());
/* 316 */       localNVList.add_value("bi", (Any)localObject4, 2);
/* 317 */       paramServerRequest.params(localNVList);
/*    */ 
/* 319 */       int i = ((Any)localObject1).extract_ulong();
/*    */ 
/* 321 */       localObject6 = new BindingListHolder();
/*    */ 
/* 323 */       localObject7 = new BindingIteratorHolder();
/* 324 */       list(i, (BindingListHolder)localObject6, (BindingIteratorHolder)localObject7);
/* 325 */       BindingListHelper.insert((Any)localObject2, ((BindingListHolder)localObject6).value);
/* 326 */       BindingIteratorHelper.insert((Any)localObject4, ((BindingIteratorHolder)localObject7).value);
/* 327 */       Any localAny7 = _orb().create_any();
/* 328 */       localAny7.type(_orb().get_primitive_tc(TCKind.tk_void));
/* 329 */       paramServerRequest.result(localAny7);
/*    */ 
/* 331 */       break;
/*    */     case 7:
/* 334 */       localNVList = _orb().create_list(0);
/* 335 */       paramServerRequest.params(localNVList);
/*    */ 
/* 337 */       localObject1 = new_context();
/* 338 */       localObject2 = _orb().create_any();
/* 339 */       NamingContextHelper.insert((Any)localObject2, (NamingContext)localObject1);
/* 340 */       paramServerRequest.result((Any)localObject2);
/*    */ 
/* 342 */       break;
/*    */     case 8:
/* 345 */       localNVList = _orb().create_list(0);
/* 346 */       localObject1 = _orb().create_any();
/* 347 */       ((Any)localObject1).type(NameHelper.type());
/* 348 */       localNVList.add_value("n", (Any)localObject1, 1);
/* 349 */       paramServerRequest.params(localNVList);
/*    */ 
/* 351 */       localObject2 = NameHelper.extract((Any)localObject1);
/*    */       try
/*    */       {
/* 354 */         localObject4 = bind_new_context((NameComponent[])localObject2);
/*    */       }
/*    */       catch (NotFound localNotFound3) {
/* 357 */         localObject6 = _orb().create_any();
/* 358 */         NotFoundHelper.insert((Any)localObject6, localNotFound3);
/* 359 */         paramServerRequest.except((Any)localObject6);
/* 360 */         return;
/*    */       }
/*    */       catch (AlreadyBound localAlreadyBound1) {
/* 363 */         localObject6 = _orb().create_any();
/* 364 */         AlreadyBoundHelper.insert((Any)localObject6, localAlreadyBound1);
/* 365 */         paramServerRequest.except((Any)localObject6);
/* 366 */         return;
/*    */       }
/*    */       catch (CannotProceed localCannotProceed3) {
/* 369 */         localObject6 = _orb().create_any();
/* 370 */         CannotProceedHelper.insert((Any)localObject6, localCannotProceed3);
/* 371 */         paramServerRequest.except((Any)localObject6);
/* 372 */         return;
/*    */       }
/*    */       catch (InvalidName localInvalidName3) {
/* 375 */         localObject6 = _orb().create_any();
/* 376 */         InvalidNameHelper.insert((Any)localObject6, localInvalidName3);
/* 377 */         paramServerRequest.except((Any)localObject6);
/* 378 */         return;
/*    */       }
/* 380 */       Any localAny3 = _orb().create_any();
/* 381 */       NamingContextHelper.insert(localAny3, (NamingContext)localObject4);
/* 382 */       paramServerRequest.result(localAny3);
/*    */ 
/* 384 */       break;
/*    */     case 9:
/* 387 */       localNVList = _orb().create_list(0);
/* 388 */       paramServerRequest.params(localNVList);
/*    */       try {
/* 390 */         destroy();
/*    */       }
/*    */       catch (NotEmpty localNotEmpty) {
/* 393 */         localObject2 = _orb().create_any();
/* 394 */         NotEmptyHelper.insert((Any)localObject2, localNotEmpty);
/* 395 */         paramServerRequest.except((Any)localObject2);
/* 396 */         return;
/*    */       }
/* 398 */       Any localAny1 = _orb().create_any();
/* 399 */       localAny1.type(_orb().get_primitive_tc(TCKind.tk_void));
/* 400 */       paramServerRequest.result(localAny1);
/*    */ 
/* 402 */       break;
/*    */     default:
/* 404 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*    */     }
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 48 */     _methods.put("bind", new Integer(0));
/* 49 */     _methods.put("bind_context", new Integer(1));
/* 50 */     _methods.put("rebind", new Integer(2));
/* 51 */     _methods.put("rebind_context", new Integer(3));
/* 52 */     _methods.put("resolve", new Integer(4));
/* 53 */     _methods.put("unbind", new Integer(5));
/* 54 */     _methods.put("list", new Integer(6));
/* 55 */     _methods.put("new_context", new Integer(7));
/* 56 */     _methods.put("bind_new_context", new Integer(8));
/* 57 */     _methods.put("destroy", new Integer(9));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming._NamingContextImplBase
 * JD-Core Version:    0.6.2
 */