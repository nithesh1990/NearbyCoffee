package napps.com.nearbycoffee.Contract;

import napps.com.nearbycoffee.BasePresenter;
import napps.com.nearbycoffee.BaseView;

/**
 * Created by "nithesh" on 7/15/2017.
 */

public interface NetworkContract {

    interface View extends BaseView<Presenter>{
        void showNetworkError();
    }

    interface Presenter extends BasePresenter{
        void checkNetworkConnection();
    }
}
