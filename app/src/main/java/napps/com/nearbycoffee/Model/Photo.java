package napps.com.nearbycoffee.Model;

/**
 * Created by "nithesh" on 7/15/2017.
 */

public final class Photo {
    private String photo_reference;

    private String html_attribution;

    private String height;

    private String width;

    public String getPhoto_reference ()
    {
        return photo_reference;
    }

    public void setPhoto_reference (String photo_reference)
    {
        this.photo_reference = photo_reference;
    }

    public String getHtml_attribution ()
    {
        return html_attribution;
    }

    public void setHtml_attribution (String html_attribution)
    {
        this.html_attribution = html_attribution;
    }

    public String getHeight ()
    {
        return height;
    }

    public void setHeight (String height)
    {
        this.height = height;
    }

    public String getWidth ()
    {
        return width;
    }

    public void setWidth (String width)
    {
        this.width = width;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [photo_reference = "+photo_reference+", html_attribution = "+html_attribution+", height = "+height+", width = "+width+"]";
    }

}
