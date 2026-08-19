package tests.entities;

import entities.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tests.factory.AccountFactory;

public class AccountTests {

    @Test
    public void depositShouldIncreaseBalanceWhenPositiveAmount() {
        double amount = 200.0;
        double expectedValue = 196.0;
        Account account = AccountFactory.createAccount();

        account.deposit(amount);

        Assertions.assertEquals(expectedValue, account.getBalance());
    }

    @Test
    public void depositShouldDecreaseBalanceWhenNegativeAmount() {
        double expectedValue = 100.0;
        Account account = AccountFactory.createAccount(expectedValue);
        double amount = -200.0;

        account.deposit(amount);

        Assertions.assertEquals(expectedValue, account.getBalance());
    }

    @Test
    public void withdrawShouldDecreaseBalanceWhenSufficientBalance() {
        Account account = AccountFactory.createAccount(800.0);

        account.withdraw(500.0);

        Assertions.assertEquals(300.0, account.getBalance());
    }

    @Test
    public void withdrawShouldThrowExceptionWhenInsufficientBalance() {
        Account account = AccountFactory.createAccount(800.0);

        Assertions.assertThrows(IllegalArgumentException.class, () -> account.withdraw(900.0));
    }

    @Test
    public void fullWithdrawShouldClearBalanceAndReturnFullBalance() {
        double expectedValue = 0.0;
        double initialBalance = 800.0;
        Account account = AccountFactory.createAccount(initialBalance);

        double result = account.fullWithdraw();

        Assertions.assertEquals(expectedValue, account.getBalance());
        Assertions.assertEquals(initialBalance, result);
    }

}
