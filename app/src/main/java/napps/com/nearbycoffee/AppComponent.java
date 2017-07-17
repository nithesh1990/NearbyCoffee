package napps.com.nearbycoffee;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by "nithesh" on 7/16/2017.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    //Since this is no more a subcomponent, we won't be using inject directly here instead allowing it to subcomponent to create more abstraction
    //void inject(MainActivity mainActivity)

}
