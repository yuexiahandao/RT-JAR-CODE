/*    */ package sun.text.resources;
/*    */ 
/*    */ import java.util.ListResourceBundle;
/*    */ 
/*    */ public class FormatData extends ListResourceBundle
/*    */ {
/*    */   protected final Object[][] getContents()
/*    */   {
/* 53 */     return new Object[][] { { "MonthNames", { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December", "" } }, { "MonthAbbreviations", { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "" } }, { "DayNames", { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" } }, { "DayAbbreviations", { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" } }, { "AmPmMarkers", { "AM", "PM" } }, { "Eras", { "BC", "AD" } }, { "sun.util.BuddhistCalendar.Eras", { "BC", "B.E." } }, { "sun.util.BuddhistCalendar.short.Eras", { "BC", "B.E." } }, { "java.util.JapaneseImperialCalendar.Eras", { "", "Meiji", "Taisho", "Showa", "Heisei" } }, { "java.util.JapaneseImperialCalendar.short.Eras", { "", "M", "T", "S", "H" } }, { "java.util.JapaneseImperialCalendar.FirstYear", new String[0] }, { "NumberPatterns", { "#,##0.###;-#,##0.###", "¤ #,##0.00;-¤ #,##0.00", "#,##0%" } }, { "NumberElements", { ".", ",", ";", "%", "0", "#", "-", "E", "‰", "∞", "�" } }, { "DateTimePatterns", { "h:mm:ss a z", "h:mm:ss a z", "h:mm:ss a", "h:mm a", "EEEE, MMMM d, yyyy", "MMMM d, yyyy", "MMM d, yyyy", "M/d/yy", "{1} {0}" } }, { "sun.util.BuddhistCalendar.DateTimePatterns", { "H:mm:ss z", "H:mm:ss z", "H:mm:ss", "H:mm", "EEEE d MMMM G yyyy", "d MMMM yyyy", "d MMM yyyy", "d/M/yyyy", "{1}, {0}" } }, { "java.util.JapaneseImperialCalendar.DateTimePatterns", { "h:mm:ss a z", "h:mm:ss a z", "h:mm:ss a", "h:mm a", "GGGG yyyy MMMM d (EEEE)", "GGGG yyyy MMMM d", "GGGG yyyy MMM d", "Gy.MM.dd", "{1} {0}" } }, { "DateTimePatternChars", "GyMdkHmsSEDFwWahKzZ" } };
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.resources.FormatData
 * JD-Core Version:    0.6.2
 */