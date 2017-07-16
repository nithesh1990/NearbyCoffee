package napps.com.nearbycoffee.Model;

/**
 * Created by "nithesh" on 7/15/2017.
 */

public final class OpeningHours {

    private String open_now;

    public String getOpen_now ()
    {
        return open_now;
    }

    public void setOpen_now (String open_now)
    {
        this.open_now = open_now;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [open_now = "+open_now+"]";
    }
}
