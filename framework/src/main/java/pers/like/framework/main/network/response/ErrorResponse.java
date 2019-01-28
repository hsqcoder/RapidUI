package pers.like.framework.main.network.response;

/**
 * @author Like
 */
public class ErrorResponse<DATA> extends Response<DATA> {

    ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
