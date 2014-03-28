<html>
<head>
    <meta charset="utf-8" />
    <title>Demo</title>
</head>
<body>
<style>
a.test {
    font-weight: bold;
}
</style>
    <a href="http://jquery.com/">jQuery</a>
    <!-- <script src="pages/resource/jquery-1.10.2.min.js"></script> -->
    <script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
    <script>
    $( "a" ).addClass( "test" );
    
    $( "a" ).click(function( event ) {
    	 
        event.preventDefault();
     
        $( this ).hide( "slow" );
     
    });
    
 
    </script>
    
    <script type="text/javascript">
 
$(document).ready(function(){
 $("#msgid").html("This is Hello World by JQuery");
});
 
</script>
 
This is Hello World by HTML
 
<div id="msgid">
</body>
</html>