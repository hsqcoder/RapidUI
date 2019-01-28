package pers.like.framework.main.ui.dialog;

/**
 * @author like
 */
@SuppressWarnings("unused")
public class Mapping {
    private int id;
    private String name;

    public Mapping(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
