$(document).ready(function () {
    let id = ''
    let userTable = $('#users').DataTable({
        ajax: {
            url: "/user/list",
            method: 'GET',
            dataSrc: ''
        },
        columns: [
            {title: "T/r", data: 'tr'},
            {title: "chatId", data: 'chatId'},
            {title: "firstName", data: 'firstName'},
            {title: "lastName", data: 'lastName'},
            {title: "phoneNumber", data: 'phoneNumber'},
            {title: "courses", data: 'course'}
        ]
    })

    $('#xabarYub').click(function () {
        let text = document.getElementById('comment').value;
        $.ajax({
            url: "/user/comment",
            method: 'POST',
            contentType: 'application/json',
            data: text
        });
        $('#commentModal').modal('toggle');
        window.location.reload();
    })
    //
    $(userTable.table().body).on('click', '#malumotKor', function () {
        let data = userTable.row($(this).parents('tr')).data();
        id = data.id;
        alert(id)
        console.log(data);
        $('#malumotlar').modal("toggle");
    })
    $.ajax({
        url: "user/" + id,
        method: "GET",
        success: function (des) {
            $.each(des, function (tartib, a) {
                $("#malumot").append(`<h1>${a.tgFirstName} ${tgLastName} ${tgUsername}</h1>`)
                console.log(a)
            })
        }
    })
})