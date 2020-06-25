<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

    <title>Hello, world!</title>
</head>
<body>
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<!--<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script> -->
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
<script src="<c:url value="/js/scripts.js" />"></script>

<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<div class="container">
    <div class="row pt-3">
        <h4>
            Бронирование мест на сеанс
        </h4>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th style="width: 120px;">Ряд / Место</th>
                <c:forEach items="${seatsInRow}" var="seat">
                    <th><c:out value="${seat}" /></th>
                </c:forEach>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${rows}" var="row">
                <tr>
                    <th><c:out value="${row}"/></th>
                    <c:forEach items="${seatsInRow}" var="seat">
                        <td id="seat-<c:out value="${row}"/>-<c:out value="${seat}"/>">
                            <c:set var="idx" value="${row}-${seat}" />
                            <input type="radio" name="place" value="<c:out value="${row}"/>-<c:out value="${seat}"/>">р.<c:out value="${row}"/>, м.<c:out value="${seat}"/>
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="row float-right">
        <button type="button" id="btn-order" class="btn btn-success">Оплатить</button>
    </div>
</div>
<div id="dialog-form" title="Забронировать место">
    <p class="validateTips">Вы хотите забронировать <span id="order-row"></span> ряд, <span id="order-seat"></span> место</p>
    <form>
        <fieldset>
            <div class="form-group">
                <label for="order-name">ФИО</label>
                <input type="text" name="name" id="order-name" placeholder="ФИО" class="form-control" />
            </div>
            <div class="form-group">
                <label for="order-phone">Телефон</label>
                <input type="text" name="phone" id="order-phone" placeholder="Телефон" class="form-control" />
            </div>
            <div class="alert alert-danger" id="dlg-message"></div>
            <input type="submit" tabindex="-1" style="position:absolute; top:-1000px" class="btn btn-primary">
        </fieldset>
    </form>
</div>
</body>
</html>
