package transactions;

public interface Transaction {
    void perform() throws Exception;
    void reverse() throws Exception;
}
