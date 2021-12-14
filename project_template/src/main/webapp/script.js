$(document).ready(function () { // When the DOM of this page is constructed
    $("#auth-submit").click(function () {// on click of go button do...
        var account = document.getElementById("account").value;
        var password = document.getElementById("password").value;
        $.post("auth-servlet", {account: account, password: password}, // POST to NewServlet
            function (data) {       // callBack function
                //alert(data);
                $("#auth-result").html(data); // Select element
                //(by ID) and insert dynamic content (data) into it
            });
    });
});

$(document).ready(function (){
    $("#insert-submit").click(function (){
        var name = document.getElementById("name").value;
        var surname = document.getElementById("surname").value;
        $.post("insert-servlet", {name: name, surname: surname},
            function (data) {
               $("#insert-result").html(data);
            });
    });
});

$(document).ready(function (){
   $("#show-submit").click(function () {
      var docente = document.getElementById("docente").value;
      $.get("show-servlet", {docente: docente},
          function (data) {
             $("#show-result").html(data);
          });
   });
});