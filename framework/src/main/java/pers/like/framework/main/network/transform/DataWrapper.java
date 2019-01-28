package pers.like.framework.main.network.transform;

/**
 * @author like
 */
public class DataWrapper {

    private int code;
    private String message;
    private Object data;
    private boolean successful;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccessful() {
        return successful ;
    }

    public DataWrapper setSuccessful(boolean successful) {
        this.successful = successful;
        return this;
    }
}
