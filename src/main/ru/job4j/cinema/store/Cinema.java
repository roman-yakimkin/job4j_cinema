package ru.job4j.cinema.store;

import java.util.Collection;
import java.util.Map;

/**
 * Интерфейс для методов кинозала
 * @author Roman Yakimkin (r.yakimkin@yandex.ru)
 * @since 22.06.2020
 * @version 1.0
 */
public interface Cinema {
    public int getRows();
    public int getSeatsInRow();
    public Map<String, Boolean> getSeatInfo();
    public void cleanOrder(int row, int seat);
    public void cleanOrders();
    public void bookSeat(int row, int seat, String orderName, String orderPhone);
    public boolean isSeatFree(int row, int seat);
}
