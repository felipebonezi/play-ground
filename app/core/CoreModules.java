package core;

import com.google.inject.AbstractModule;
import core.injections.AuthJwt;
import core.injections.DateFormattersProvider;
import java.time.Clock;
import play.data.format.Formatters;

/** Basic core Play modules. */
public class CoreModules extends AbstractModule {
  
  @Override
  protected void configure() {
    // Use the system clock as the default implementation of Clock
    bind(Clock.class).toInstance(Clock.systemDefaultZone());
    bind(Formatters.class).toProvider(DateFormattersProvider.class);
    bind(AuthJwt.class).asEagerSingleton();
  }
  
}
