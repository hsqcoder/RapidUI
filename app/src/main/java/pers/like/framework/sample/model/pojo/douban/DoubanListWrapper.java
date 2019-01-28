package pers.like.framework.sample.model.pojo.douban;

import java.util.List;

/**
 * @author Like
 */
public class DoubanListWrapper<T> {

    private Integer count;
    private Integer start;
    private Integer total;
    private List<T> subjects;
    private String title;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<T> subjects) {
        this.subjects = subjects;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
