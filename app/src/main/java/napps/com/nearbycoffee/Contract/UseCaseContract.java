package napps.com.nearbycoffee.Contract;

import android.app.Application;

import napps.com.nearbycoffee.BaseApplication;

/**
 * Created by "nithesh" on 7/18/2017.
 */

public interface UseCaseContract {

    interface Mediator {

        BaseApplication requestUserContext();
        void requestUser();
        void approvalSuccess();
        void approvalReject();
    }

    interface BusinessLogic {

        void onInitialize();
        void requestApproval();
    }

}
