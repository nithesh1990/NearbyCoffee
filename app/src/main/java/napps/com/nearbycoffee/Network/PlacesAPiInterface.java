package napps.com.nearbycoffee.Network;

import java.util.Map;

import io.reactivex.Observable;
import napps.com.nearbycoffee.Model.PlaceSearchResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by "nithesh" on 7/15/2017.
 */

public interface PlacesAPIInterface {

    @GET("nearbysearch/{responseFormat}")
    Observable<PlaceSearchResponse> getNearbyPlaces(@Path("responseFormat") String responseFormat, @QueryMap Map<String, String> queryParams);
}
