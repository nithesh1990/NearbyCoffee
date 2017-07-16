package napps.com.nearbycoffee.Contract;

import napps.com.nearbycoffee.BasePresenter;
import napps.com.nearbycoffee.BaseView;

/**
 * Created by "nithesh" on 7/15/2017.
 */

public interface LocationContract {

    interface View extends BaseView<Presenter>{

        void showLoadingLocation();

        void showRequestLocationAccess();

        void showRequestLocationSettings();

        void showRequestLocationSuccesful();
    }

    interface Presenter extends BasePresenter {

        void locationAccessResult(int resultCode);

        void locationSettingsResult(int resultCode);

        void requestLocation();
    }
}
