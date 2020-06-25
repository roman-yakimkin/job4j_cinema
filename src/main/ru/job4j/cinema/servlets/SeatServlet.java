package ru.job4j.cinema.servlets;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.job4j.cinema.store.DBCinema;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Сервлет для бронировани места
 * @author Roman Yakimkin (r.yakimkin@yandex.ru)
 * @since 23.06.2020
 * @version 1.0
 */
public class SeatServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        DBCinema.instOf().cleanOrders();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            String mode = req.getParameter("mode");
            if (mode.equals("is_seat_free")) {
                int row = Integer.parseInt(req.getParameter("row"));
                int seat = Integer.parseInt(req.getParameter("seat"));
                boolean isFree = DBCinema.instOf().isSeatFree(row, seat);
                String jsonOutput = JSONObject.toJSONString(Map.of("free", isFree));
                resp.getWriter().write(jsonOutput);
            } else if (mode.equals("seat_info")) {
                String jsonOutput = JSONObject.toJSONString(DBCinema.instOf().getSeatInfo());
                resp.getWriter().write(jsonOutput);
            }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(req.getReader());
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            int row = Integer.parseInt(jsonObject.get("row").toString());
            int seat = Integer.parseInt(jsonObject.get("seat").toString());
            String orderName = jsonObject.get("order_name").toString();
            String orderPhone = jsonObject.get("order_phone").toString();
            Map<String, String> result = Map.of("result", "seat_is_busy");
            synchronized (this) {
                if (DBCinema.instOf().isSeatFree(row, seat)) {
                    DBCinema.instOf().bookSeat(row, seat, orderName, orderPhone);
                    result = Map.of("result", "ok");
                }
            }
            resp.getWriter().write(JSONObject.toJSONString(result));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
