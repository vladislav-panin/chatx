function _applayDynamicSettings ()
{
        drawMenuTriangle ();
}

function drawMenuTriangle ()
{
        var triangle = document.getElementById('forTriangle');
        if (!triangle)
            return;

        var topBodyBar = document.getElementById('topBar');
        var style = window.getComputedStyle(topBodyBar);
        var ctx = triangle.getContext('2d');

        ctx.beginPath();
        ctx.moveTo(10, 0);
        ctx.lineTo(90, 0);
        ctx.lineTo(50, 50);
        ctx.lineTo(10, 0);
        ctx.closePath();

        ctx.shadowColor = "rgba(0, 0, 0, 0.5)";
        ctx.shadowBlur = 15;
        ctx.shadowOffsetX = 0;
        ctx.shadowOffsetY = 4;
        ctx.fillStyle = style.getPropertyValue("background-color");
        ctx.fill();
}
// *********************************************************************************************************************
function getNickNPass ()
{
    var nick = $("#nick").val();
    var pass = $("#pass").val();
    
    var obj = {"nick" : nick, "pass" : pass};
    var jStr = JSON.stringify (obj);
   
    return jStr;
}
// *********************************************************************************************************************
function login (login_panel_node)
{
    console.log("JS: sending login is started ******************");
    
    var request = getNickNPass ();
    $.ajax
    (            
        {   url: 'ajax/do-login',  
            type: 'POST',
            xhr: function()
            {
                var myXhr = $.ajaxSettings.xhr();
                if(myXhr.upload)
                {
                    myXhr.upload.addEventListener('progress',progressHandlingFunction, false);
                }
                return myXhr;
            },
            
            beforeSend: beforeSendHandler,            
            success: completeHandler,
            error: errorHandler,            
            
            contentType:"application/json; charset=utf-8",
            data: request,
            cache: false,            
            processData: false
        }
    );    
    console.log("JS: sending login has been finished ******************");
}
// *********************************************************************************************************************
function progressHandlingFunction(e)
{
    if(e.lengthComputable)
    {
        // someday I'll do it. may be
    }
}
function beforeSendHandler(e)
{
    $("#result_ID").remove();

    $("#btn-waiting").css ("display", "inline-block");
}

function redirectAfterLogin()
{
    window.location.replace("admin-welcome");
}

function completeHandler(jObj)
{
        $("#btn-waiting").css ("display", "none");        
        
        var el = document.getElementById("msg-container");        
        var emd = document.createElement('div');
        
        $(emd).addClass ("result");
        $(emd).attr ("id", "result_ID");
        
        
        var ih =    '<br>' + 
                    '<font style="color:#03AD47; font-weight:bold;"> Сообщение сервера:</font>' +               
                    '<br><font style="color:#E040BB;">Response Code:</font> ' + jObj.responseCode + 
                    '<br><font style="color:#E040BB;">Header:</font> '        + jObj.header + 
                    '<br><font style="color:#E040BB;">Message:</font> '       + jObj.message;
        
        
        emd.innerHTML = ih;
        el.insertBefore(emd, el.firstChild);
        
        $(el).show(); 
        
        window.setTimeout(redirectAfterLogin, 1000);
}

function errorHandler(e, textStatus, errMess)
{
    
    $("#btn-waiting").css ("display", "none");
    
    if (e.status === 404)
    {
        console.log(e.responseText);
        var msg = e.responseText;
        var start = msg.indexOf("<body") + "<body".length + 1;
        var stop = msg.indexOf("</body>");
        
        msg = e.responseText.substring(start, stop);
        var 
        $_msg_cont = $("#msg-container").eq(0);        
        $_msg_cont. append (msg);
        $_msg_cont.show();
    }     
    else
    { 
        var txt = e.responseText;        
        var jObj = JSON.parse(txt);
        
        var el = document.getElementById("msg-container");
        var emd = document.createElement('div');
        
        $(emd).addClass ("result");        
        $(emd).attr ("id", "result_ID");
        
        var ih =    '<br>' +
                    '<font color=red>Ошибка:<br>HTTP response status: '  + e.status + '</font><br>' +
                    '<br><font style="color:#03AD47; font-weight:bold;"> Сообщение сервера:</font>' +               
                    '<br><font style="color:#E040BB;">Response Code:</font> ' + jObj.responseCode + 
                    '<br><font style="color:#E040BB;">Header:</font> '        + jObj.header + 
                    '<br><font style="color:#E040BB;">Message:</font> '       + jObj.message;
        
        emd.innerHTML = ih;
        el.insertBefore(emd, el.firstChild);
        
        $(el).show();        
    }
}
// *********************************************************************************************************************