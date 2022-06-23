package core.utils;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

/** Akka job helper method. */
public final class JobUtil {
  
  private JobUtil() {
  }
  
  /**
   * Return next execution in seconds.
   *
   * @param hour   Expected hour.
   * @param minute Expected minute.
   *
   * @return Seconds between now and expected time.
   */
  public static int nextExecutionInSeconds(int hour, int minute) {
    return Seconds.secondsBetween(DateTime.now(), nextExecution(hour, minute)).getSeconds();
  }
  
  /**
   * Return next execution as {@link DateTime}.
   * If the expected date time is in the past then a day will be increased.
   *
   * @param hour   Expected hour.
   * @param minute Expected minute.
   *
   * @return Date time.
   */
  private static DateTime nextExecution(int hour, int minute) {
    DateTime next = new DateTime()
        .withHourOfDay(hour)
        .withMinuteOfHour(minute)
        .withSecondOfMinute(0)
        .withMillisOfSecond(0);
    
    return (next.isBeforeNow()) ? next.plusHours(24) : next;
  }
  
}

