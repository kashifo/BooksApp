package cf.kashif.booksapp.pojo;

import java.util.Collections;
import java.util.List;

/**
 * Created by Kashif on 2/20/2018.
 */

public class VolumeInfo {
    private String title;
    private List<String> authors;
    private String publisher;
    private String previewLink;
    public ImageLinks imageLinks = new ImageLinks();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        if (authors != null && !authors.isEmpty())
            return authors;
        else
            return Collections.emptyList();
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }
}
