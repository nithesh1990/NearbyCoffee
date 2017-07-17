package napps.com.nearbycoffee;

import android.app.Application;

/**
 * Created by "nithesh" on 7/16/2017.
 */

public class BaseApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        //DaggerAppComponent is the generated class during build time. For each component, dagger generates a Dagger{ComponentName} class.
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        appComponent.inject(this);
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }
}
