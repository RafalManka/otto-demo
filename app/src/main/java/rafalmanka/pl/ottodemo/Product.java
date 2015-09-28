package rafalmanka.pl.ottodemo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by rafal on 9/28/15.
 */
public class Product implements Serializable {

    private static int nextId;

    @Nullable
    private String name;
    @Nullable
    private String description;
    private int price;
    private int likes;
    @Nullable
    private User user;
    @Nullable
    private String imageUrl;
    private int id;

    public Product() {
        id = nextId++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @NonNull
    public User getUser() throws ProductException {
        if (user == null) {
            throw new ProductException("user is not set");
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @NonNull
    public String getImageUrl() throws ProductException {
        if (imageUrl == null) {
            throw new ProductException("main image is not set");
        }
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getDisplayPrice() {
        return price / 100;
    }

    public int getId() {
        return id;
    }

    public void setPrice(String s) throws ProductException {
        try {
            float displayprice = Float.valueOf(s);
            price = (int) (displayprice * 100);
        } catch (NumberFormatException e) {
            throw new ProductException("price in wrong format");
        }
    }

    public class ProductException extends Exception {
        public ProductException(String s) {
        }
    }
}
