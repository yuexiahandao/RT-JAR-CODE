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
/*    */ 
/*    */ public abstract class _BindingIteratorImplBase extends DynamicImplementation
/*    */   implements BindingIterator
/*    */ {
/* 40 */   private static final String[] _type_ids = { "IDL:omg.org/CosNaming/BindingIterator:1.0" };
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
/*    */     Any localAny1;
/*    */     Object localObject1;
/*    */     Object localObject2;
/* 54 */     switch (((Integer)_methods.get(paramServerRequest.op_name())).intValue())
/*    */     {
/*    */     case 0:
/* 57 */       localNVList = _orb().create_list(0);
/* 58 */       localAny1 = _orb().create_any();
/* 59 */       localAny1.type(BindingHelper.type());
/* 60 */       localNVList.add_value("b", localAny1, 2);
/* 61 */       paramServerRequest.params(localNVList);
/*    */ 
/* 63 */       localObject1 = new BindingHolder();
/*    */ 
/* 65 */       boolean bool1 = next_one((BindingHolder)localObject1);
/* 66 */       BindingHelper.insert(localAny1, ((BindingHolder)localObject1).value);
/* 67 */       localObject2 = _orb().create_any();
/* 68 */       ((Any)localObject2).insert_boolean(bool1);
/* 69 */       paramServerRequest.result((Any)localObject2);
/*    */ 
/* 71 */       break;
/*    */     case 1:
/* 74 */       localNVList = _orb().create_list(0);
/* 75 */       localAny1 = _orb().create_any();
/* 76 */       localAny1.type(ORB.init().get_primitive_tc(TCKind.tk_ulong));
/* 77 */       localNVList.add_value("how_many", localAny1, 1);
/* 78 */       localObject1 = _orb().create_any();
/* 79 */       ((Any)localObject1).type(BindingListHelper.type());
/* 80 */       localNVList.add_value("bl", (Any)localObject1, 2);
/* 81 */       paramServerRequest.params(localNVList);
/*    */ 
/* 83 */       int i = localAny1.extract_ulong();
/*    */ 
/* 85 */       localObject2 = new BindingListHolder();
/*    */ 
/* 87 */       boolean bool2 = next_n(i, (BindingListHolder)localObject2);
/* 88 */       BindingListHelper.insert((Any)localObject1, ((BindingListHolder)localObject2).value);
/* 89 */       Any localAny2 = _orb().create_any();
/* 90 */       localAny2.insert_boolean(bool2);
/* 91 */       paramServerRequest.result(localAny2);
/*    */ 
/* 93 */       break;
/*    */     case 2:
/* 96 */       localNVList = _orb().create_list(0);
/* 97 */       paramServerRequest.params(localNVList);
/* 98 */       destroy();
/* 99 */       localAny1 = _orb().create_any();
/* 100 */       localAny1.type(_orb().get_primitive_tc(TCKind.tk_void));
/* 101 */       paramServerRequest.result(localAny1);
/*    */ 
/* 103 */       break;
/*    */     default:
/* 105 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*    */     }
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 48 */     _methods.put("next_one", new Integer(0));
/* 49 */     _methods.put("next_n", new Integer(1));
/* 50 */     _methods.put("destroy", new Integer(2));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming._BindingIteratorImplBase
 * JD-Core Version:    0.6.2
 */