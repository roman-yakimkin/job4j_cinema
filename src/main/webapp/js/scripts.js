(function ($) {

    function redrawSeat(row, seat) {
        let args = {
            "mode" : "is_seat_free",
            "row" : row,
            "seat" : seat
        };
        $.get("http://localhost:8080/cinema/seat.do", args)
            .done(function(data){
                let $container = $("#seat-" + row + "-" + seat);
                if (data.free === false) {
                    $container.html("<div class='seat-ordered'>Занят</div>")
                } else {
                    $container.html("<input type='radio' name='place' value='" + row + "-" + seat +"'>р. " + row + ", м." + seat);
                }
            })
            .fail(function(){
               console.log("fail");
            });
    }

    function redrawAllSeats(){
        let args = {
            "mode" : "seat_info",
        };
        $.get("http://localhost:8080/cinema/seat.do", args)
            .done(function(data){
                console.log(data);
                for (let key in data) {
                    let rsArray = key.split("-");
                    let r = rsArray[0], s = rsArray[1];
                    let $container = $("#seat-" + r + "-" + s);
                    if (data[key] === false) {
                        $container.html("<div class='seat-ordered'>Занят</div>")
                    } else {
                        if ($("input[name=\"place\"]", $container).length == 0) {
                            $container.html("<input type='radio' name='place' value='" + r + "-" + s + "'>р." + r + ", м." + s);
                        }
                    }
                }
            })
            .fail(function(){
                console.log("fail");
            });
    }

    function outputDataResult(msgText) {
        $("#dlg-message")
            .css("display", "block")
            .html(msgText);
        setTimeout(function(){
            $("#dlg-message")
                .css("display", "none");
        }, 5000);
    }

    $(document).ready(function() {
        var row = 0, seat = 0;
        let dialog, form;
        let name = $("#order-name");
        let phone = $("#order-phone");
        let allFields = $([]).add(name).add(phone);

        dialog = $("#dialog-form").dialog({
            autoOpen: false,
            height: 400,
            width: 500,
            modal: true,
            buttons: {
                "Забронировать": function() {
                    $.ajax({
                        type: 'POST',
                        url: 'http://localhost:8080/cinema/seat.do',
                        data: JSON.stringify({
                            "row" : row,
                            "seat" : seat,
                            "order_name" : name.val(),
                            "order_phone" : phone.val()
                        }),
                        contentType: "application/json",
                        dataType: 'json',
                        processData: false,
                        success: function(data) {
                            if (data.result === "ok") {
                               redrawSeat(row, seat);
                                dialog.dialog("close");

                            } else if (data.result === "seat_is_busy") {
                                outputDataResult("Место уже занято");
                            } else {
                                outputDataResult(data.result);
                            }
                        },
                        error: function(jqXhr, textStatus, errorThrown) {
                            console.log(errorThrown);
                            dialog.dialog("close");
                        },
                    });
                },
                "Отмена": function() {
                    dialog.dialog("close");
                }
            },
            open: function() {
                let selectedSeat = $("input[name=\"place\"]:checked");
                let rowAndSeat = selectedSeat[0].value;
                let rsArray = rowAndSeat.toString().split("-");
                row = rsArray[0];
                seat = rsArray[1];
                $("#order-row").html(row);
                $("#order-seat").html(seat);
                $("#dlg-message")
                    .css("display", "none")
                    .html("");
                let buttons = $(".ui-dialog-buttonset button");
                buttons.addClass("btn");
                buttons.filter(":first").addClass("btn-success");
            },
            close: function() {
                form[0].reset();
                allFields.removeClass( "ui-state-error" );
            }
        });

        form = dialog.find("form").on("submit", function( event ) {
            event.preventDefault();
        });

        $("#btn-order").on("click", function(){
            dialog.dialog("open");
        });

        redrawAllSeats();
        setInterval(redrawAllSeats, 10000);
    });
})(jQuery);