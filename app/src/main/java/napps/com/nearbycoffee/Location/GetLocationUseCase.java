package napps.com.nearbycoffee.Location;

import android.app.Application;

import com.google.android.gms.location.FusedLocationProviderClient;

import javax.inject.Inject;

import napps.com.nearbycoffee.AppModule;
import napps.com.nearbycoffee.BaseApplication;
import napps.com.nearbycoffee.Contract.UseCaseContract;

/**
 * Created by "nithesh" on 7/18/2017.
 */

public final class GetLocationUseCase implements UseCaseContract.BusinessLogic {


    UseCaseContract.Mediator mediator;

    @Inject
    FusedLocationProviderClient fusedLocationProviderClientl;

    @Override
    public void onInitialize() {

        BaseApplication application = mediator.requestUserContext();

        DaggerLocationComponent.builder().appModule(new AppModule(application)).locationModule(new LocationModule()).build().inject(this);

    }

    @Override
    public void requestApproval() {

    }
}
