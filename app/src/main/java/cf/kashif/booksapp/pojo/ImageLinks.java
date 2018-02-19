package cf.kashif.booksapp.pojo;

/**
 * Created by Kashif on 2/20/2018.
 */

public class ImageLinks {

    private String smallThumbnail;
    private String thumbnail;

    public String getThumbnail() {

        if (thumbnail != null)
            return thumbnail;
        else
            return "";

    }

}
