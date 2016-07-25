/*      */ package com.sun.java_cup.internal.runtime;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.util.Stack;
/*      */ 
/*      */ public abstract class lr_parser
/*      */ {
/*      */   protected static final int _error_sync_size = 3;
/*  246 */   protected boolean _done_parsing = false;
/*      */   protected int tos;
/*      */   protected Symbol cur_token;
/*  275 */   protected Stack stack = new Stack();
/*      */   protected short[][] production_tab;
/*      */   protected short[][] action_tab;
/*      */   protected short[][] reduce_tab;
/*      */   private Scanner _scanner;
/*      */   protected Symbol[] lookahead;
/*      */   protected int lookahead_pos;
/*      */ 
/*      */   public lr_parser()
/*      */   {
/*      */   }
/*      */ 
/*      */   public lr_parser(Scanner s)
/*      */   {
/*  153 */     this();
/*  154 */     setScanner(s);
/*      */   }
/*      */ 
/*      */   protected int error_sync_size()
/*      */   {
/*  171 */     return 3;
/*      */   }
/*      */ 
/*      */   public abstract short[][] production_table();
/*      */ 
/*      */   public abstract short[][] action_table();
/*      */ 
/*      */   public abstract short[][] reduce_table();
/*      */ 
/*      */   public abstract int start_state();
/*      */ 
/*      */   public abstract int start_production();
/*      */ 
/*      */   public abstract int EOF_sym();
/*      */ 
/*      */   public abstract int error_sym();
/*      */ 
/*      */   public void done_parsing()
/*      */   {
/*  256 */     this._done_parsing = true;
/*      */   }
/*      */ 
/*      */   public void setScanner(Scanner s)
/*      */   {
/*  302 */     this._scanner = s;
/*      */   }
/*      */ 
/*      */   public Scanner getScanner()
/*      */   {
/*  307 */     return this._scanner;
/*      */   }
/*      */ 
/*      */   public abstract Symbol do_action(int paramInt1, lr_parser paramlr_parser, Stack paramStack, int paramInt2)
/*      */     throws Exception;
/*      */ 
/*      */   public void user_init()
/*      */     throws Exception
/*      */   {
/*      */   }
/*      */ 
/*      */   protected abstract void init_actions()
/*      */     throws Exception;
/*      */ 
/*      */   public Symbol scan()
/*      */     throws Exception
/*      */   {
/*  359 */     return getScanner().next_token();
/*      */   }
/*      */ 
/*      */   public void report_fatal_error(String message, Object info)
/*      */     throws Exception
/*      */   {
/*  378 */     done_parsing();
/*      */ 
/*  381 */     report_error(message, info);
/*      */ 
/*  384 */     throw new Exception("Can't recover from previous error(s)");
/*      */   }
/*      */ 
/*      */   public void report_error(String message, Object info)
/*      */   {
/*  400 */     System.err.print(message);
/*  401 */     if ((info instanceof Symbol)) {
/*  402 */       if (((Symbol)info).left != -1)
/*  403 */         System.err.println(" at character " + ((Symbol)info).left + " of input");
/*      */       else
/*  405 */         System.err.println("");
/*      */     } else System.err.println("");
/*      */   }
/*      */ 
/*      */   public void syntax_error(Symbol cur_token)
/*      */   {
/*  419 */     report_error("Syntax error", cur_token);
/*      */   }
/*      */ 
/*      */   public void unrecovered_syntax_error(Symbol cur_token)
/*      */     throws Exception
/*      */   {
/*  432 */     report_fatal_error("Couldn't repair and continue parse", cur_token);
/*      */   }
/*      */ 
/*      */   protected final short get_action(int state, int sym)
/*      */   {
/*  451 */     short[] row = this.action_tab[state];
/*      */ 
/*  454 */     if (row.length < 20) {
/*  455 */       for (int probe = 0; probe < row.length; probe++)
/*      */       {
/*  458 */         short tag = row[(probe++)];
/*  459 */         if ((tag == sym) || (tag == -1))
/*      */         {
/*  462 */           return row[probe];
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  468 */     int first = 0;
/*  469 */     int last = (row.length - 1) / 2 - 1;
/*  470 */     while (first <= last)
/*      */     {
/*  472 */       int probe = (first + last) / 2;
/*  473 */       if (sym == row[(probe * 2)])
/*  474 */         return row[(probe * 2 + 1)];
/*  475 */       if (sym > row[(probe * 2)])
/*  476 */         first = probe + 1;
/*      */       else {
/*  478 */         last = probe - 1;
/*      */       }
/*      */     }
/*      */ 
/*  482 */     return row[(row.length - 1)];
/*      */     int probe;
/*  487 */     return 0;
/*      */   }
/*      */ 
/*      */   protected final short get_reduce(int state, int sym)
/*      */   {
/*  505 */     short[] row = this.reduce_tab[state];
/*      */ 
/*  508 */     if (row == null) {
/*  509 */       return -1;
/*      */     }
/*  511 */     for (int probe = 0; probe < row.length; probe++)
/*      */     {
/*  514 */       short tag = row[(probe++)];
/*  515 */       if ((tag == sym) || (tag == -1))
/*      */       {
/*  518 */         return row[probe];
/*      */       }
/*      */     }
/*      */ 
/*  522 */     return -1;
/*      */   }
/*      */ 
/*      */   public Symbol parse()
/*      */     throws Exception
/*      */   {
/*  539 */     Symbol lhs_sym = null;
/*      */ 
/*  546 */     this.production_tab = production_table();
/*  547 */     this.action_tab = action_table();
/*  548 */     this.reduce_tab = reduce_table();
/*      */ 
/*  551 */     init_actions();
/*      */ 
/*  554 */     user_init();
/*      */ 
/*  557 */     this.cur_token = scan();
/*      */ 
/*  560 */     this.stack.removeAllElements();
/*  561 */     this.stack.push(new Symbol(0, start_state()));
/*  562 */     this.tos = 0;
/*      */ 
/*  565 */     for (this._done_parsing = false; !this._done_parsing; )
/*      */     {
/*  568 */       if (this.cur_token.used_by_parser) {
/*  569 */         throw new Error("Symbol recycling detected (fix your scanner).");
/*      */       }
/*      */ 
/*  574 */       int act = get_action(((Symbol)this.stack.peek()).parse_state, this.cur_token.sym);
/*      */ 
/*  577 */       if (act > 0)
/*      */       {
/*  580 */         this.cur_token.parse_state = (act - 1);
/*  581 */         this.cur_token.used_by_parser = true;
/*  582 */         this.stack.push(this.cur_token);
/*  583 */         this.tos += 1;
/*      */ 
/*  586 */         this.cur_token = scan();
/*      */       }
/*  589 */       else if (act < 0)
/*      */       {
/*  592 */         lhs_sym = do_action(-act - 1, this, this.stack, this.tos);
/*      */ 
/*  595 */         short lhs_sym_num = this.production_tab[(-act - 1)][0];
/*  596 */         short handle_size = this.production_tab[(-act - 1)][1];
/*      */ 
/*  599 */         for (int i = 0; i < handle_size; i++)
/*      */         {
/*  601 */           this.stack.pop();
/*  602 */           this.tos -= 1;
/*      */         }
/*      */ 
/*  606 */         act = get_reduce(((Symbol)this.stack.peek()).parse_state, lhs_sym_num);
/*      */ 
/*  609 */         lhs_sym.parse_state = act;
/*  610 */         lhs_sym.used_by_parser = true;
/*  611 */         this.stack.push(lhs_sym);
/*  612 */         this.tos += 1;
/*      */       }
/*  615 */       else if (act == 0)
/*      */       {
/*  618 */         syntax_error(this.cur_token);
/*      */ 
/*  621 */         if (!error_recovery(false))
/*      */         {
/*  624 */           unrecovered_syntax_error(this.cur_token);
/*      */ 
/*  627 */           done_parsing();
/*      */         } else {
/*  629 */           lhs_sym = (Symbol)this.stack.peek();
/*      */         }
/*      */       }
/*      */     }
/*  633 */     return lhs_sym;
/*      */   }
/*      */ 
/*      */   public void debug_message(String mess)
/*      */   {
/*  645 */     System.err.println(mess);
/*      */   }
/*      */ 
/*      */   public void dump_stack()
/*      */   {
/*  653 */     if (this.stack == null)
/*      */     {
/*  655 */       debug_message("# Stack dump requested, but stack is null");
/*  656 */       return;
/*      */     }
/*      */ 
/*  659 */     debug_message("============ Parse Stack Dump ============");
/*      */ 
/*  662 */     for (int i = 0; i < this.stack.size(); i++)
/*      */     {
/*  664 */       debug_message("Symbol: " + ((Symbol)this.stack.elementAt(i)).sym + " State: " + ((Symbol)this.stack.elementAt(i)).parse_state);
/*      */     }
/*      */ 
/*  667 */     debug_message("==========================================");
/*      */   }
/*      */ 
/*      */   public void debug_reduce(int prod_num, int nt_num, int rhs_size)
/*      */   {
/*  680 */     debug_message("# Reduce with prod #" + prod_num + " [NT=" + nt_num + ", " + "SZ=" + rhs_size + "]");
/*      */   }
/*      */ 
/*      */   public void debug_shift(Symbol shift_tkn)
/*      */   {
/*  692 */     debug_message("# Shift under term #" + shift_tkn.sym + " to state #" + shift_tkn.parse_state);
/*      */   }
/*      */ 
/*      */   public void debug_stack()
/*      */   {
/*  701 */     StringBuffer sb = new StringBuffer("## STACK:");
/*  702 */     for (int i = 0; i < this.stack.size(); i++) {
/*  703 */       Symbol s = (Symbol)this.stack.elementAt(i);
/*  704 */       sb.append(" <state " + s.parse_state + ", sym " + s.sym + ">");
/*  705 */       if ((i % 3 == 2) || (i == this.stack.size() - 1)) {
/*  706 */         debug_message(sb.toString());
/*  707 */         sb = new StringBuffer("         ");
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public Symbol debug_parse()
/*      */     throws Exception
/*      */   {
/*  726 */     Symbol lhs_sym = null;
/*      */ 
/*  732 */     this.production_tab = production_table();
/*  733 */     this.action_tab = action_table();
/*  734 */     this.reduce_tab = reduce_table();
/*      */ 
/*  736 */     debug_message("# Initializing parser");
/*      */ 
/*  739 */     init_actions();
/*      */ 
/*  742 */     user_init();
/*      */ 
/*  745 */     this.cur_token = scan();
/*      */ 
/*  747 */     debug_message("# Current Symbol is #" + this.cur_token.sym);
/*      */ 
/*  750 */     this.stack.removeAllElements();
/*  751 */     this.stack.push(new Symbol(0, start_state()));
/*  752 */     this.tos = 0;
/*      */ 
/*  755 */     for (this._done_parsing = false; !this._done_parsing; )
/*      */     {
/*  758 */       if (this.cur_token.used_by_parser) {
/*  759 */         throw new Error("Symbol recycling detected (fix your scanner).");
/*      */       }
/*      */ 
/*  765 */       int act = get_action(((Symbol)this.stack.peek()).parse_state, this.cur_token.sym);
/*      */ 
/*  768 */       if (act > 0)
/*      */       {
/*  771 */         this.cur_token.parse_state = (act - 1);
/*  772 */         this.cur_token.used_by_parser = true;
/*  773 */         debug_shift(this.cur_token);
/*  774 */         this.stack.push(this.cur_token);
/*  775 */         this.tos += 1;
/*      */ 
/*  778 */         this.cur_token = scan();
/*  779 */         debug_message("# Current token is " + this.cur_token);
/*      */       }
/*  782 */       else if (act < 0)
/*      */       {
/*  785 */         lhs_sym = do_action(-act - 1, this, this.stack, this.tos);
/*      */ 
/*  788 */         short lhs_sym_num = this.production_tab[(-act - 1)][0];
/*  789 */         short handle_size = this.production_tab[(-act - 1)][1];
/*      */ 
/*  791 */         debug_reduce(-act - 1, lhs_sym_num, handle_size);
/*      */ 
/*  794 */         for (int i = 0; i < handle_size; i++)
/*      */         {
/*  796 */           this.stack.pop();
/*  797 */           this.tos -= 1;
/*      */         }
/*      */ 
/*  801 */         act = get_reduce(((Symbol)this.stack.peek()).parse_state, lhs_sym_num);
/*  802 */         debug_message("# Reduce rule: top state " + ((Symbol)this.stack.peek()).parse_state + ", lhs sym " + lhs_sym_num + " -> state " + act);
/*      */ 
/*  807 */         lhs_sym.parse_state = act;
/*  808 */         lhs_sym.used_by_parser = true;
/*  809 */         this.stack.push(lhs_sym);
/*  810 */         this.tos += 1;
/*      */ 
/*  812 */         debug_message("# Goto state #" + act);
/*      */       }
/*  815 */       else if (act == 0)
/*      */       {
/*  818 */         syntax_error(this.cur_token);
/*      */ 
/*  821 */         if (!error_recovery(true))
/*      */         {
/*  824 */           unrecovered_syntax_error(this.cur_token);
/*      */ 
/*  827 */           done_parsing();
/*      */         } else {
/*  829 */           lhs_sym = (Symbol)this.stack.peek();
/*      */         }
/*      */       }
/*      */     }
/*  833 */     return lhs_sym;
/*      */   }
/*      */ 
/*      */   protected boolean error_recovery(boolean debug)
/*      */     throws Exception
/*      */   {
/*  865 */     if (debug) debug_message("# Attempting error recovery");
/*      */ 
/*  869 */     if (!find_recovery_config(debug))
/*      */     {
/*  871 */       if (debug) debug_message("# Error recovery fails");
/*  872 */       return false;
/*      */     }
/*      */ 
/*  876 */     read_lookahead();
/*      */     while (true)
/*      */     {
/*  882 */       if (debug) debug_message("# Trying to parse ahead");
/*  883 */       if (try_parse_ahead(debug))
/*      */       {
/*      */         break;
/*      */       }
/*      */ 
/*  889 */       if (this.lookahead[0].sym == EOF_sym())
/*      */       {
/*  891 */         if (debug) debug_message("# Error recovery fails at EOF");
/*  892 */         return false;
/*      */       }
/*      */ 
/*  896 */       if (debug)
/*  897 */         debug_message("# Consuming Symbol #" + cur_err_token().sym);
/*  898 */       restart_lookahead();
/*      */     }
/*      */ 
/*  902 */     if (debug) debug_message("# Parse-ahead ok, going back to normal parse");
/*      */ 
/*  905 */     parse_lookahead(debug);
/*      */ 
/*  908 */     return true;
/*      */   }
/*      */ 
/*      */   protected boolean shift_under_error()
/*      */   {
/*  919 */     return get_action(((Symbol)this.stack.peek()).parse_state, error_sym()) > 0;
/*      */   }
/*      */ 
/*      */   protected boolean find_recovery_config(boolean debug)
/*      */   {
/*  936 */     if (debug) debug_message("# Finding recovery state on stack");
/*      */ 
/*  939 */     int right_pos = ((Symbol)this.stack.peek()).right;
/*  940 */     int left_pos = ((Symbol)this.stack.peek()).left;
/*      */ 
/*  943 */     while (!shift_under_error())
/*      */     {
/*  946 */       if (debug) {
/*  947 */         debug_message("# Pop stack by one, state was # " + ((Symbol)this.stack.peek()).parse_state);
/*      */       }
/*  949 */       left_pos = ((Symbol)this.stack.pop()).left;
/*  950 */       this.tos -= 1;
/*      */ 
/*  953 */       if (this.stack.empty())
/*      */       {
/*  955 */         if (debug) debug_message("# No recovery state found on stack");
/*  956 */         return false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  961 */     int act = get_action(((Symbol)this.stack.peek()).parse_state, error_sym());
/*  962 */     if (debug)
/*      */     {
/*  964 */       debug_message("# Recover state found (#" + ((Symbol)this.stack.peek()).parse_state + ")");
/*      */ 
/*  966 */       debug_message("# Shifting on error to state #" + (act - 1));
/*      */     }
/*      */ 
/*  970 */     Symbol error_token = new Symbol(error_sym(), left_pos, right_pos);
/*  971 */     error_token.parse_state = (act - 1);
/*  972 */     error_token.used_by_parser = true;
/*  973 */     this.stack.push(error_token);
/*  974 */     this.tos += 1;
/*      */ 
/*  976 */     return true;
/*      */   }
/*      */ 
/*      */   protected void read_lookahead()
/*      */     throws Exception
/*      */   {
/*  995 */     this.lookahead = new Symbol[error_sync_size()];
/*      */ 
/*  998 */     for (int i = 0; i < error_sync_size(); i++)
/*      */     {
/* 1000 */       this.lookahead[i] = this.cur_token;
/* 1001 */       this.cur_token = scan();
/*      */     }
/*      */ 
/* 1005 */     this.lookahead_pos = 0;
/*      */   }
/*      */ 
/*      */   protected Symbol cur_err_token()
/*      */   {
/* 1011 */     return this.lookahead[this.lookahead_pos];
/*      */   }
/*      */ 
/*      */   protected boolean advance_lookahead()
/*      */   {
/* 1021 */     this.lookahead_pos += 1;
/*      */ 
/* 1024 */     return this.lookahead_pos < error_sync_size();
/*      */   }
/*      */ 
/*      */   protected void restart_lookahead()
/*      */     throws Exception
/*      */   {
/* 1035 */     for (int i = 1; i < error_sync_size(); i++) {
/* 1036 */       this.lookahead[(i - 1)] = this.lookahead[i];
/*      */     }
/*      */ 
/* 1039 */     this.cur_token = scan();
/* 1040 */     this.lookahead[(error_sync_size() - 1)] = this.cur_token;
/*      */ 
/* 1043 */     this.lookahead_pos = 0;
/*      */   }
/*      */ 
/*      */   protected boolean try_parse_ahead(boolean debug)
/*      */     throws Exception
/*      */   {
/* 1064 */     virtual_parse_stack vstack = new virtual_parse_stack(this.stack);
/*      */     while (true)
/*      */     {
/* 1070 */       int act = get_action(vstack.top(), cur_err_token().sym);
/*      */ 
/* 1073 */       if (act == 0) return false;
/*      */ 
/* 1076 */       if (act > 0)
/*      */       {
/* 1079 */         vstack.push(act - 1);
/*      */ 
/* 1081 */         if (debug) debug_message("# Parse-ahead shifts Symbol #" + cur_err_token().sym + " into state #" + (act - 1));
/*      */ 
/* 1085 */         if (!advance_lookahead()) return true;
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1091 */         if (-act - 1 == start_production())
/*      */         {
/* 1093 */           if (debug) debug_message("# Parse-ahead accepts");
/* 1094 */           return true;
/*      */         }
/*      */ 
/* 1098 */         short lhs = this.production_tab[(-act - 1)][0];
/* 1099 */         short rhs_size = this.production_tab[(-act - 1)][1];
/*      */ 
/* 1102 */         for (int i = 0; i < rhs_size; i++) {
/* 1103 */           vstack.pop();
/*      */         }
/* 1105 */         if (debug) {
/* 1106 */           debug_message("# Parse-ahead reduces: handle size = " + rhs_size + " lhs = #" + lhs + " from state #" + vstack.top());
/*      */         }
/*      */ 
/* 1110 */         vstack.push(get_reduce(vstack.top(), lhs));
/* 1111 */         if (debug)
/* 1112 */           debug_message("# Goto state #" + vstack.top());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void parse_lookahead(boolean debug)
/*      */     throws Exception
/*      */   {
/* 1135 */     Symbol lhs_sym = null;
/*      */ 
/* 1141 */     this.lookahead_pos = 0;
/*      */ 
/* 1143 */     if (debug)
/*      */     {
/* 1145 */       debug_message("# Reparsing saved input with actions");
/* 1146 */       debug_message("# Current Symbol is #" + cur_err_token().sym);
/* 1147 */       debug_message("# Current state is #" + ((Symbol)this.stack.peek()).parse_state);
/*      */     }
/*      */ 
/* 1152 */     while (!this._done_parsing)
/*      */     {
/* 1157 */       int act = get_action(((Symbol)this.stack.peek()).parse_state, cur_err_token().sym);
/*      */ 
/* 1161 */       if (act > 0)
/*      */       {
/* 1164 */         cur_err_token().parse_state = (act - 1);
/* 1165 */         cur_err_token().used_by_parser = true;
/* 1166 */         if (debug) debug_shift(cur_err_token());
/* 1167 */         this.stack.push(cur_err_token());
/* 1168 */         this.tos += 1;
/*      */ 
/* 1171 */         if (!advance_lookahead())
/*      */         {
/* 1173 */           if (debug) debug_message("# Completed reparse");
/*      */ 
/* 1182 */           return;
/*      */         }
/*      */ 
/* 1185 */         if (debug) {
/* 1186 */           debug_message("# Current Symbol is #" + cur_err_token().sym);
/*      */         }
/*      */       }
/* 1189 */       else if (act < 0)
/*      */       {
/* 1192 */         lhs_sym = do_action(-act - 1, this, this.stack, this.tos);
/*      */ 
/* 1195 */         short lhs_sym_num = this.production_tab[(-act - 1)][0];
/* 1196 */         short handle_size = this.production_tab[(-act - 1)][1];
/*      */ 
/* 1198 */         if (debug) debug_reduce(-act - 1, lhs_sym_num, handle_size);
/*      */ 
/* 1201 */         for (int i = 0; i < handle_size; i++)
/*      */         {
/* 1203 */           this.stack.pop();
/* 1204 */           this.tos -= 1;
/*      */         }
/*      */ 
/* 1208 */         act = get_reduce(((Symbol)this.stack.peek()).parse_state, lhs_sym_num);
/*      */ 
/* 1211 */         lhs_sym.parse_state = act;
/* 1212 */         lhs_sym.used_by_parser = true;
/* 1213 */         this.stack.push(lhs_sym);
/* 1214 */         this.tos += 1;
/*      */ 
/* 1216 */         if (debug) debug_message("# Goto state #" + act);
/*      */ 
/*      */       }
/* 1221 */       else if (act == 0)
/*      */       {
/* 1223 */         report_fatal_error("Syntax error", lhs_sym);
/* 1224 */         return;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static short[][] unpackFromStrings(String[] sa)
/*      */   {
/* 1237 */     StringBuffer sb = new StringBuffer(sa[0]);
/* 1238 */     for (int i = 1; i < sa.length; i++)
/* 1239 */       sb.append(sa[i]);
/* 1240 */     int n = 0;
/* 1241 */     int size1 = sb.charAt(n) << '\020' | sb.charAt(n + 1); n += 2;
/* 1242 */     short[][] result = new short[size1][];
/* 1243 */     for (int i = 0; i < size1; i++) {
/* 1244 */       int size2 = sb.charAt(n) << '\020' | sb.charAt(n + 1); n += 2;
/* 1245 */       result[i] = new short[size2];
/* 1246 */       for (int j = 0; j < size2; j++)
/* 1247 */         result[i][j] = ((short)(sb.charAt(n++) - '\002'));
/*      */     }
/* 1249 */     return result;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java_cup.internal.runtime.lr_parser
 * JD-Core Version:    0.6.2
 */