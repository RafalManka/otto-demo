package rafalmanka.pl.ottodemo;

import java.io.Serializable;

/**
 * Created by rafal on 9/28/15.
 */
public class User implements Serializable {

    private String name;
    private String location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
