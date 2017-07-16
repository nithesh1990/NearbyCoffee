package napps.com.nearbycoffee.Contract;

import android.support.annotation.NonNull;

import napps.com.nearbycoffee.BasePresenter;
import napps.com.nearbycoffee.BaseView;
import napps.com.nearbycoffee.Model.Result;

/**
 * Created by "nithesh" on 7/15/2017.
 */

public interface PlaceContract {

    interface View extends BaseView<Presenter>{

        void showPlace(@NonNull Result placeResult);

        void showplaceLoadingIndicator();

        void showPlaceError();
    }

    interface Presenter extends BasePresenter{

        void loadPlaceDetails(@NonNull String placeId);

    }
}
