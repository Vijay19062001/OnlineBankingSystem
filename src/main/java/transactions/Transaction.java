package transactions;



import java.io.Serializable;

public interface Transaction extends Serializable {
    void perform() throws Exception;
    void reverse() throws Exception;
    String getAccountNumber() throws Exception;  // New method to get account number
    String getDetails()throws Exception;

}
