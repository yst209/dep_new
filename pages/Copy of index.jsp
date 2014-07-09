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


<div id="myElement" style="border:2px solid red;">Hello</div>

This is Hello World by HTML${pageContext.request.contextPath}
<a href="http://jquery.com/">jQuery link</a>
 
<div id="msgid"/>


<!-- <script src="/dep/pages/resource/jquery-1.10.2.min.js"></script> ${pageContext.request.contextPath} = /dep  http://code.jquery.com/jquery-1.11.0.min.js -->
<script src="/dep/pages/resource/jquery-1.10.2.min.js"></script>
<script>
$( "a" ).addClass( "test" );

$( "a" ).click(function( event ) {
	 
 	event.preventDefault();
    $( this ).fadeTo(1000, 0.2 );
 
});

$("#myElement").animate(  
	    {  
	        opacity: .3,  
	        width: "200px",  
	        height: "100px"  
	    }, 5000, function() {  
	        // optional callback after animation completes    
	    }  
	);  
 
 
$(document).ready(function(){
 $("#msgid").html("<H1>This is Hello World by JQuery</H1>");
});
 
</script>
 

</body>
</html>