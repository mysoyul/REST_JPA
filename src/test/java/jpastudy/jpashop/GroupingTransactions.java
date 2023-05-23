package jpastudy.jpashop;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

public class GroupingTransactions {

    public static List<Transaction> transactions = Arrays.asList( new Transaction(Currency.EUR, 1500.0),
                                                                  new Transaction(Currency.USD, 2300.0),
                                                                  new Transaction(Currency.GBP, 9900.0),
                                                                  new Transaction(Currency.EUR, 1100.0),
                                                                  new Transaction(Currency.JPY, 7800.0),
                                                                  new Transaction(Currency.CHF, 6700.0),
                                                                  new Transaction(Currency.EUR, 5600.0),
                                                                  new Transaction(Currency.USD, 4500.0),
                                                                  new Transaction(Currency.CHF, 3400.0),
                                                                  new Transaction(Currency.GBP, 3200.0),
                                                                  new Transaction(Currency.USD, 4600.0),
                                                                  new Transaction(Currency.JPY, 5700.0),
                                                                  new Transaction(Currency.EUR, 6800.0) );
//    public static void main(String ... args) {
//        groupImperatively();
//        groupFunctionally();
//        groupByCurrencySum();
//        groupByCurrencyPartition();
//    }

    @Test
    public void groupImperatively() {
        Map<Currency, List<Transaction>> transactionsByCurrencies = new HashMap<>();
        for (Transaction transaction : transactions) {
            Currency currency = transaction.getCurrency();
            List<Transaction> transactionsForCurrency = transactionsByCurrencies.get(currency);
            if (transactionsForCurrency == null) {
                    transactionsForCurrency = new ArrayList<>();
                transactionsByCurrencies.put(currency, transactionsForCurrency);
            }
            transactionsForCurrency.add(transaction);
        }

        System.out.println(transactionsByCurrencies);
    }

    //Java8 groupingBy 사용
    @Test
    public void groupFunctionally() {
         //Map<Currency, List<Transaction>>
        Map<Currency, List<Transaction>> currencyListMap = transactions.stream()
                .collect(groupingBy(tx -> tx.getCurrency()));
        System.out.println("currencyListMap = " + currencyListMap);
    }

    @Test
    public void groupByCurrencySum() {
        //각 트랜잭션을 통화별로 그룹화 한 다음에 해당 통화의 모든 트랜잭션 합계
        Map<Currency, Double> currencyDoubleMap = transactions.stream()
                .collect(groupingBy(Transaction::getCurrency,
                        summingDouble(Transaction::getValue)));
        System.out.println("currencyDoubleMap = " + currencyDoubleMap);
    }

    @Test
    public void groupByCurrencyPartition() {
        //각 트랜잭션을 통화별로 그룹화 한 뒤 각 트랜잭션이 5000 이상일 경우를 구분하여 리스트로 반환
        Map<Currency, Map<Boolean, List<Transaction>>> currencyMap = transactions.stream()
                .collect(groupingBy(Transaction::getCurrency,
                        Collectors.partitioningBy(tx -> tx.getValue() > 5000)));
        System.out.println("currencyMap = " + currencyMap);
    }

    public static class Transaction {
        private final Currency currency;
        private final double value;

        public Transaction(Currency currency, double value) {
            this.currency = currency;
            this.value = value;
        }

        public Currency getCurrency() {
            return currency;
        }

        public double getValue() {
            return value;
        }

        @Override
        public String toString() {
            return currency + " " + value;
        }
    }

    public enum Currency {
        EUR, USD, JPY, GBP, CHF
    }
}
