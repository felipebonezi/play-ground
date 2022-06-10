package core;

import com.google.inject.AbstractModule;
import core.injections.AuthJWT;
import core.injections.DateFormattersProvider;
import java.time.Clock;
import play.data.format.Formatters;

public class CoreModules extends AbstractModule {
  
  @Override
  protected void configure() {
    // Use the system clock as the default implementation of Clock
    bind(Clock.class).toInstance(Clock.systemDefaultZone());
    bind(Formatters.class).toProvider(DateFormattersProvider.class);
    bind(AuthJWT.class).asEagerSingleton();
  }
  
}
