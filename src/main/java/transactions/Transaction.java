package transactions;


import java.io.Serializable;

public interface Transaction extends Serializable {
    void perform() throws Exception;
    void reverse() throws Exception;
}
