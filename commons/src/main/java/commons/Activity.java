package commons;

import java.util.Objects;
import javax.persistence.*;

@Entity
public class Activity {

    @Id
    public String id;
    public String image_path;
    public String title;

    public long consumption_in_wh;
    @Column(length = 511)
    public String source;

    public long index;

    public Activity(String id, String image_path, String title, long consumption_in_wh, String source) {
        this.id = id;
        this.image_path = image_path;
        this.title = title;
        this.consumption_in_wh = consumption_in_wh;
        this.source = source;
    }

    public Activity(String id, String image_path, String title, String consumption_in_wh, String source) {
        this.id = id;
        this.image_path = image_path;
        this.title = title;
        this.consumption_in_wh = Long.parseLong(consumption_in_wh);
        this.source = source;
    }

    public Activity() {

    }

    public Activity deepCopy() {
        Activity a = new Activity(id, image_path, title, consumption_in_wh, source);
        a.setIndex(index);
        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activity)) return false;
        Activity activity = (Activity) o;
        return consumption_in_wh == activity.consumption_in_wh && id.equals(activity.id) && image_path.equals(activity.image_path) && title.equals(activity.title) && source.equals(activity.source) && index == activity.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, image_path, title, consumption_in_wh, source, index);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id='" + id + '\'' +
                ", image_path='" + image_path + '\'' +
                ", title='" + title + '\'' +
                ", consumption_in_wh=" + consumption_in_wh +
                ", source='" + source + '\'' +
                ", index=" + index +
                '}';
    }
    public String toStringForInspection(){
        return "{\n" +
                "id='" + id + '\'' +
                ", \nimage_path='" + image_path + '\'' +
                ", \ntitle='" + title + '\'' +
                ", \nconsumption_in_wh=" + consumption_in_wh +
                ", \nsource='" + source + '\'' +
                ", \nindex=" + index +
                "\n}";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getConsumption_in_wh() {
        return consumption_in_wh;
    }

    public void setConsumption_in_wh(long consumption_in_wh) {
        this.consumption_in_wh = consumption_in_wh;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }
}

