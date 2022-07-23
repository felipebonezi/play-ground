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

package core;

import com.google.inject.AbstractModule;
import core.auth.JwtValidateWithDb;
import core.injections.AuthJwt;
import core.injections.DateFormattersProvider;
import java.time.Clock;
import play.data.format.Formatters;

/** Basic core Play modules. */
public class CoreModules extends AbstractModule {
  
  @Override
  protected void configure() {
    // Use the system clock as the default implementation of Clock.
    bind(Clock.class).toInstance(Clock.systemDefaultZone());
    bind(Formatters.class).toProvider(DateFormattersProvider.class);
    bind(AuthJwt.class).asEagerSingleton();
    bind(JwtValidateWithDb.class).toInstance(session -> true);
  }
  
}
