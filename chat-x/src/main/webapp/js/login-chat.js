// *********************************************************************************************************************
function OnDocumentReady ()
{
    $('#formLoginId').keypress(function(event){
        
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == '13'){
            
            $('#formLoginId').submit() ();
        }
    });
}
// *********************************************************************************************************************

