package napps.com.nearbycoffee.Model;

/**
 * Created by "nithesh" on 7/15/2017.
 */

public final class PlaceSearchResponse {
    private Result[] result;

    private String next_page_token;

    private String status;

    public Result[] getResult ()
    {
        return result;
    }

    public void setResult (Result[] result)
    {
        this.result = result;
    }

    public String getNext_page_token ()
    {
        return next_page_token;
    }

    public void setNext_page_token (String next_page_token)
    {
        this.next_page_token = next_page_token;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [result = "+result+", next_page_token = "+next_page_token+", status = "+status+"]";
    }
}
