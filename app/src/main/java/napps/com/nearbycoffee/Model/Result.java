package napps.com.nearbycoffee.Model;

/**
 * Created by "nithesh" on 7/15/2017.
 */

public final class Result {

    private String price_level;

    private String id;

    private String place_id;

    private String icon;

    private String vicinity;

    private String scope;

    private String name;

    private String rating;

    private String[] type;

    private Photo photo;

    private String reference;

    private OpeningHours opening_hours;

    private Geometry geometry;

    public String getPrice_level ()
    {
        return price_level;
    }

    public void setPrice_level (String price_level)
    {
        this.price_level = price_level;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getPlace_id ()
    {
        return place_id;
    }

    public void setPlace_id (String place_id)
    {
        this.place_id = place_id;
    }

    public String getIcon ()
    {
        return icon;
    }

    public void setIcon (String icon)
    {
        this.icon = icon;
    }

    public String getVicinity ()
    {
        return vicinity;
    }

    public void setVicinity (String vicinity)
    {
        this.vicinity = vicinity;
    }

    public String getScope ()
    {
        return scope;
    }

    public void setScope (String scope)
    {
        this.scope = scope;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getRating ()
    {
        return rating;
    }

    public void setRating (String rating)
    {
        this.rating = rating;
    }

    public String[] getType ()
    {
        return type;
    }

    public void setType (String[] type)
    {
        this.type = type;
    }

    public Photo getPhoto ()
    {
        return photo;
    }

    public void setPhoto (Photo photo)
    {
        this.photo = photo;
    }

    public String getReference ()
    {
        return reference;
    }

    public void setReference (String reference)
    {
        this.reference = reference;
    }

    public OpeningHours getOpening_hours ()
    {
        return opening_hours;
    }

    public void setOpening_hours (OpeningHours opening_hours)
    {
        this.opening_hours = opening_hours;
    }

    public Geometry getGeometry ()
    {
        return geometry;
    }

    public void setGeometry (Geometry geometry)
    {
        this.geometry = geometry;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [price_level = "+price_level+", id = "+id+", place_id = "+place_id+", icon = "+icon+", vicinity = "+vicinity+", scope = "+scope+", name = "+name+", rating = "+rating+", type = "+type+", photo = "+photo+", reference = "+reference+", opening_hours = "+opening_hours+", geometry = "+geometry+"]";
    }

}
