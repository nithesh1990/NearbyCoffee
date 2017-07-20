package napps.com.nearbycoffee.Location;

import napps.com.nearbycoffee.BaseApplication;
import napps.com.nearbycoffee.Contract.LocationContract;
import napps.com.nearbycoffee.Contract.UseCaseContract;

/**
 * Created by "nithesh" on 7/18/2017.
 */

public final class LocationPresenter implements LocationContract.Presenter, UseCaseContract.Mediator {


    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void locationAccessResult(int resultCode) {

    }

    @Override
    public void locationSettingsResult(int resultCode) {

    }

    @Override
    public void requestLocation() {

    }

    @Override
    public BaseApplication requestUserContext() {
        return null;
    }

    @Override
    public void requestUser() {

    }

    @Override
    public void approvalSuccess() {

    }

    @Override
    public void approvalReject() {

    }
}
