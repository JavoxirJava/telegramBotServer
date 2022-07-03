$(document).ready(function () {
    let id = ''
    let groupTable = $('#starts').DataTable({
        ajax:{
            url:"/start/list",
            method:'GET',
            dataSrc:''
        },
        columns:[
            {title:"T/r", data:'tr'},
            {title:"chatId", data:'chatId'},
            {title:"firstName", data:'firstName'},
            {title:"username", data:'username'},
            // {title:"Action",data:'id'}
        ]})
    $('#xabarYubor').click(function (){
        let text=document.getElementById('comment').value;
        let rasm=document.getElementById('rasm').value;
        let obj = {text, rasm}
        $.ajax({
            url: "/start/comment",
            method: 'POST',
            contentType: 'application/json',
            data:JSON.stringify(obj)
        });
        $('#commentModal').modal('toggle');
        window.location.reload();
    })
})