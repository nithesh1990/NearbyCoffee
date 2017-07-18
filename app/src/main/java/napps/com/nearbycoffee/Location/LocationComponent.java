package napps.com.nearbycoffee.Location;

import javax.inject.Singleton;

import dagger.Component;
import napps.com.nearbycoffee.AppModule;

/**
 * Created by "nithesh" on 7/16/2017.
 */
@Singleton
@Component(modules = {LocationModule.class, AppModule.class})
public interface LocationComponent {
}
