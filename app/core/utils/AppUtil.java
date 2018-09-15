package core.utils;

import com.typesafe.config.Config;
import play.mvc.Call;

public final class AppUtil {

    private static boolean DEV_MODE;
    private static boolean STAG_MODE;
    private static boolean PROD_MODE;
    private static boolean COPY_PROD_MODE;
    public static String APP_HOST;

    public static void init(Config config) {
        APP_HOST = config.getString("play.app.host");

        String environment = config.getString("play.app.environment");
        DEV_MODE = environment.equalsIgnoreCase("development");
        STAG_MODE = environment.equalsIgnoreCase("staging");
        PROD_MODE = environment.equalsIgnoreCase("production");
        COPY_PROD_MODE = environment.equalsIgnoreCase("copy-production");
    }

    public static String redirectWithHost(Call route) {
        return redirectWithHost(route.url());
    }

    public static String redirectWithHost(String route) {
        return String.format("%s%s", APP_HOST, route);
    }

    public static boolean isDevMode() {
        return DEV_MODE;
    }

    public static boolean isStagMode() {
        return STAG_MODE;
    }

    public static boolean isProdMode() {
        return PROD_MODE;
    }

    public static boolean isCopyProdMode() {
        return COPY_PROD_MODE;
    }

}
