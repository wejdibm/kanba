<?php

define('HOST','localhost');
define('USER','root');
define('PASS','');
define('DB','connected_kaba');

 
$con = mysqli_connect(HOST,USER,PASS,DB);
$user=$_GET['user'];
$mdpasse=sha1($_GET['mdpasse']);

$requser=$con->query("SELECT * FROM users WHERE pseudo='$user' AND motdepasse='$mdpasse'");
$userexist=$requser->num_rows;
$result = array();		
if($userexist==1){
	array_push($result,array('succes'=>'true'));
}
else{
	array_push($result,array('succes'=>'false'));
}	
echo json_encode(array("result"=>$result));
 
mysqli_close($con);
 
?>