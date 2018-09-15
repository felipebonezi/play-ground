package core.utils;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

public final class JobUtil {

    /**
     * Método para obter o delay para a próxima hora e minuto informado - em segundos.
     *
     * @param hour - Hora desejada
     * @param minute - Minuto desejado
     * @return seconds
     */
    public static int nextExecutionInSeconds(int hour, int minute) {
        return Seconds.secondsBetween(DateTime.now(), nextExecution(hour, minute)).getSeconds();
    }

    /**
     * Método para obter o próximo DateTime da hora e minuto informado.
     * Caso esse horário já tenha passado, é adicionado um dia.
     *
     * @param hour - Hora desejada
     * @param minute - Minuto desejado
     * @return date
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

