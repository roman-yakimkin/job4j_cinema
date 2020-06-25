package ru.job4j.cinema.filters;

import ru.job4j.cinema.store.DBCinema;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Препроцессор для заглавной страницы
 * @author Roman Yakimkin (r.yakimkin@yandex.ru)
 * @since 24.06.2020
 * @version 1.0
 */
public class IndexFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        List<Integer> rows = new ArrayList<>();
        for (int i = 1; i <= DBCinema.instOf().getRows(); i++) {
            rows.add(i);
        };
        List<Integer> seatsInRow = new ArrayList<>();
        for (int i = 1; i <= DBCinema.instOf().getSeatsInRow(); i++) {
            seatsInRow.add(i);
        }
        req.setAttribute("rows", rows);
        req.setAttribute("seatsInRow", seatsInRow);
        req.setAttribute("seatInfo", DBCinema.instOf().getSeatInfo());

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
