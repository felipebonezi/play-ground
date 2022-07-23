/*
 * Copyright 2022 Felipe Bonezi <https://about.me/felipebonezi>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

