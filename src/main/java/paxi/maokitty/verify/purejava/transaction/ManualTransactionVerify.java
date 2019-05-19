package paxi.maokitty.verify.purejava.transaction;

import paxi.maokitty.verify.purejava.transaction.service.DatabaseService;

/**
 * Created by maokitty on 19/5/19.
 */
public class ManualTransactionVerify {
    public static void main(String[] args) {
        DatabaseService databaseService = new DatabaseService();
        databaseService.insert();
    }
}
