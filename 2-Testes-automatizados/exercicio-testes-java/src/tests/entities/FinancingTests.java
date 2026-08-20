package tests.entities;

import entities.Financing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tests.factory.FinancingFactory;

public class FinancingTests {

    @Test
    public void factoryShouldCreateFinancingObjectWhenValidData() {
        Financing financing = FinancingFactory.createFinancing(100000.0, 2000.0, 80);

        Assertions.assertEquals(100000.0, financing.getTotalAmount());
        Assertions.assertEquals(2000.0, financing.getIncome());
        Assertions.assertEquals(80, financing.getMonths());
    }

    @Test
    public void factoryShouldThrowIllegalArgumentExceptionWhenInvalidData() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> FinancingFactory.createFinancing(100000.0, 2000.0,20));
    }

    @Test
    public void setTotalAmountShouldSetDataWhenValidData() {
        Financing financing = FinancingFactory.createFinancing(100000.0, 2000.0, 80);

        financing.setTotalAmount(50000.0);

        Assertions.assertEquals(50000.0, financing.getTotalAmount());
    }

    @Test
    public void setTotalAmountShouldThrowIllegalArgumentExceptionWhenInvalidData() {
        Financing financing = FinancingFactory.createFinancing(100000.0, 2000.0, 80);

        Assertions.assertThrows(IllegalArgumentException.class, () -> financing.setTotalAmount(150000.0));
    }

    @Test
    public void setIncomeShouldSetDataWhenValidData() {
        Financing financing = FinancingFactory.createFinancing(100000.0, 2000.0, 80);

        financing.setIncome(2500.0);

        Assertions.assertEquals(2500.0, financing.getIncome());
    }

    @Test
    public void setIncomeShouldThrowIllegalArgumentExceptionWhenInvalidData() {
        Financing financing = FinancingFactory.createFinancing(100000.0, 2000.0, 80);

        Assertions.assertThrows(IllegalArgumentException.class, () -> financing.setIncome(1500.0));
    }

    @Test
    public void setMonthsShouldSetDataWhenValidData() {
        Financing financing = FinancingFactory.createFinancing(100000.0, 2000.0, 80);

        financing.setMonths(100);

        Assertions.assertEquals(100, financing.getMonths());
    }

    @Test
    public void setMonthsShouldThrowIllegalArgumentExceptionWhenInvalidData() {
        Financing financing = FinancingFactory.createFinancing(100000.0, 2000.0, 80);

        Assertions.assertThrows(IllegalArgumentException.class, () -> financing.setMonths(60));
    }

    @Test
    public void entryShouldCalculateCorrectly() {
        Financing financing = FinancingFactory.createFinancing(100000.0, 2000.0, 80);

        Assertions.assertEquals(20000.0, financing.entry());
    }

    @Test
    public void quotaShouldCalculateCorrectly() {
        Financing financing = FinancingFactory.createFinancing(100000.0, 2000.0, 80);

        Assertions.assertEquals(1000.0, financing.quota());
    }
}
