package paxi.maokitty.verify;


import paxi.maokitty.verify.service.DatabaseService;

/**
 * Created by maokitty on 19/5/19.
 */
public class ManualTransactionVerify {
    public static void main(String[] args) {
        DatabaseService databaseService = new DatabaseService();
        databaseService.insert();
    }
}
