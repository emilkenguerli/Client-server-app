import org.joda.time.*;

public class LogMain {
  public static void main(String args[]){
    LocalTime time = new LocalTime(); //Need Joda Time package:https://github.com/JodaOrg/joda-time/releases
    LocalDate date = new LocalDate();
    String dateAdded = date.toString() + " " + time.toString();
    System.out.println(dateAdded);
  }
}
