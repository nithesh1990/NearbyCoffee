package napps.com.nearbycoffee.Contract;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import napps.com.nearbycoffee.BasePresenter;
import napps.com.nearbycoffee.BaseView;

/**
 * Created by "nithesh" on 7/15/2017.
 */

public interface MapsContract {

    interface View extends BaseView<Presenter>{
        void setMapsLoadingIndicator();

        void showMapsLoaded();

        void showMapsUnAvailable();

        void showMapsLoadingError();

        void showLocationOnMapswithZoom(@NonNull  LatLng latLng, int zoom);

        void showMarkersOnMaps(@NonNull List<Marker> markers);
    }

    interface Presenter extends BasePresenter{

        void loadMaps();

        void updateLocationonMaps();


    }
}
