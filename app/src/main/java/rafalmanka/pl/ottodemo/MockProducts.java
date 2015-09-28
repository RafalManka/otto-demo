package rafalmanka.pl.ottodemo;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by rafal on 9/28/15.
 */
public class MockProducts {
    @NonNull
    public static ArrayList<Product> createMockProducts() {
        ArrayList<Product> products = new ArrayList<>();

        User user = new User();
        user.setAvatar("http://img.technospot.net/Create-your-Own-Avatar-which-is-lookalike-of-official-Android-Mascot.jpg");
        user.setName("John Doe");
        user.setLocation("Lives in Dubai Marina");

        //-- pants
        Product pants = new Product();
        pants.setName("pants");
        pants.setDescription("Very nice pants that fit. I bought them last week and wore them only once.");
        pants.setImageUrl("http://i.stpost.com/carhartt-washed-twill-work-pants-for-men-in-dark-khaki~p~3657w_03~1500.3.jpg");
        pants.setLikes(12);
        pants.setPrice(25000);
        pants.setUser(user);
        products.add(pants);

        //-- jacket
        Product jacket = new Product();
        jacket.setName("Jacket");
        jacket.setDescription("Black leather jacket");
        jacket.setImageUrl("http://images.asos-media.com/inv/media/7/5/3/1/3711357/black/image1xl.jpg");
        jacket.setLikes(2);
        jacket.setPrice(32000);
        jacket.setUser(user);
        products.add(jacket);

        //-- cat
        Product cat = new Product();
        cat.setName("Cat");
        cat.setDescription("Barely used, great companion.");
        cat.setImageUrl("http://www.cats.org.uk/uploads/images/pages/photo_latest14.jpg");
        cat.setLikes(1000);
        cat.setPrice(1500);
        cat.setUser(user);
        products.add(cat);

        return products;
    }
}
