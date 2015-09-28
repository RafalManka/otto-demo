package rafalmanka.pl.ottodemo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by rafal on 9/28/15.
 */
public class User implements Serializable {

    @Nullable
    private String name;
    @Nullable
    private String location;
    @Nullable
    private String avatar;

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

    @NonNull
    public String getAvatar() throws UserException {
        if (avatar == null) {
            throw new UserException("avatar is not set");
        }
        return avatar;
    }

    public void setAvatar(@Nullable String avatar) {
        this.avatar = avatar;
    }

    public class UserException extends Exception {
        public UserException(String s) {
        }
    }
}
