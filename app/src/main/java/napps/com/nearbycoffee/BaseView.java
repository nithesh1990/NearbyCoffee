package napps.com.nearbycoffee;

/**
 * Created by "nithesh" on 7/15/2017.
 */

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);
}
