package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * Класс - синглтон для работы с местами
 * @author Roman Yakimkin (r.yakimkin@yandex.ru)
 * @since 22.06.2020
 * @version 1.0
 */
public class DBCinema implements Cinema {

    private final BasicDataSource pool = new BasicDataSource();

    private final int rows;
    private final int seats;

    private DBCinema() {
        Properties config = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("cinema.properties")
        )) {
            config.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(config.getProperty("jdbc.driver"));
        pool.setUrl(config.getProperty("jdbc.url"));
        pool.setUsername(config.getProperty("jdbc.username"));
        pool.setPassword(config.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);

        rows = Integer.parseInt(config.getProperty("app.rows"));
        seats = Integer.parseInt(config.getProperty("app.seats"));
    }

    public static final class Lazy {
        private static final Cinema INST = new DBCinema();
    }

    public static Cinema instOf() {
        return Lazy.INST;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getSeatsInRow() {
        return seats;
    }

    @Override
    public void cleanOrder(int row, int seat) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("DELETE from seats where row = ? and seat = ?");
        ) {
            ps.setInt(1, row);
            ps.setInt(2, seat);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cleanOrders() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("DELETE from seats")
        ) {
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String bookSeat(int row, int seat, String orderName, String orderPhone) {
        String result = "ok";
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO seats(\"row\", seat, order_name, order_phone) VALUES (?, ?, ?, ?)")
        ) {
            ps.setInt(1, row);
            ps.setInt(2, seat);
            ps.setString(3, orderName);
            ps.setString(4, orderPhone);
            ps.execute();
        } catch (Exception e) {
            result = e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean isSeatFree(int row, int seat) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM seats WHERE row = ? and seat = ?")
        ) {
            ps.setInt(1, row);
            ps.setInt(2, seat);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Map<String, Boolean> getSeatInfo() {
        Map<String, Boolean> orderedSeats = new HashMap<>();
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= seats; j++) {
                orderedSeats.put(i + "-" + j, true);
            }
        }
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM seats")
        ) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orderedSeats.put(rs.getInt("row") + "-" + rs.getInt("seat"), false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderedSeats;
    }
}

