let main = {
    init : function (){
        let _this = this;
        $('#btn-save').on('click',function (){
            _this.save();
        });

        $('#btn-update').on('click',function (){
            _this.update();
        })

        $('#btn-delete').on('click',function (){
            _this.delete();
        })
    },
    save : function (){
        let data = {
            title:$('#title').val(),
            author:$('#author').val(),
            content:$('#content').val()
        };

        $.ajax({
            type:'POST',
            url:'/api/v1/posts',
            data:JSON.stringify(data),
            dataType:'json',
            contentType:'application/json; charset=utf-8'
        }).done(function (){
            alert('글이 성공적으로 작성이 완료되었습니다.');
            window.location.href="/";
        }).fail(function (error){
            alert(JSON.stringify(error))
        })
    },
    update : function (){
        let data = {
            title:$('#title').val(),
            content: $('#content').val()
        }

        const id = $('#id').val();

        $.ajax({
            type: "PUT",
            url:`/api/v1/posts/${id}`,
            data:JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (){
            alert('수정이 성공적으로 완료되었습니다.');
            window.location.href = "/";
        }).fail(function (error){
            alert(JSON.stringify(error))
        })
    },
    delete : function (){
        const id = $('#id').val();

        $.ajax({
            type:'DELETE',
            url:`/api/v1/posts/${id}`
        }).done(function (){
            alert('삭제가 성공적으로 완료되었습니다.');
            window.location.href = "/";
        }).fail(function (error){
            alert(JSON.stringify(error))
        })
    }
}

main.init();