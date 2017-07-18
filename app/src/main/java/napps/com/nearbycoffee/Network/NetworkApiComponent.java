package napps.com.nearbycoffee.Network;

import javax.inject.Singleton;

import dagger.Component;
import napps.com.nearbycoffee.MainActivity;

/**
 * Created by "nithesh" on 7/17/2017.
 */
@Singleton
@Component(modules = {NetworkApiModule.class})
public interface NetworkApiComponent {

    //This method is to tell dagger that start building dependency graph. Build the dependency network for @Inject instances specified in MainActivity so
    //that they can be used. It basically assigns singleton instances to fields in MainActivity
    void inject(MainActivity mainActivity);

    //This
    PlacesAPIInterface getPlacesAPIInterface();
}
