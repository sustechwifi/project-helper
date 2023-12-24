package sustech.ooad.websocketserver.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T>{
    int code;
    String message;
    T data;
    public static <T> Result<T> ok(T data){
        return new Result<>(0,"success",data);
    }

    public static Result<?> err(int code,String message) {
        return new Result<>(code,message,null);
    }
}
