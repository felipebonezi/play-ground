package core.utils;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.time.LocalDateTime;

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
  public static long nextExecutionInSeconds(int hour, int minute) {
    return SECONDS.between(LocalDateTime.now(), nextExecution(hour, minute));
  }
  
  /**
   * Return next execution as {@link LocalDateTime}.
   * If the expected date time is in the past then a day will be increased.
   *
   * @param hour   Expected hour.
   * @param minute Expected minute.
   *
   * @return Date time.
   */
  private static LocalDateTime nextExecution(int hour, int minute) {
    LocalDateTime now  = LocalDateTime.now();
    LocalDateTime next = now.withHour(hour).withMinute(minute).withSecond(0).withNano(0);
    return next.isBefore(now) ? next.plusHours(24) : next;
  }
  
}

