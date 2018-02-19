package cf.kashif.booksapp.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kashif on 2/20/2018.
 */
public class Book implements Serializable {

    //POJO class (encapsulation) that holds data and processes data to construct pictures url

    private String id;
    private String selfLink;
    public VolumeInfo volumeInfo = new VolumeInfo();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

}
