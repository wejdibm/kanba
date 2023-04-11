<?php

define('HOST','localhost');
define('USER','root');
define('PASS','');
define('DB','connected_kaba');

 
$con = mysqli_connect(HOST,USER,PASS,DB);
$fk_poste  = $_GET['fk_poste'];
$fk_kaba  = $_GET['fk_kaba'];
$date  = $_GET['date'];
 
$sql = "select * from postekaba where fk_poste like '%$fk_poste%' && fk_kaba like '%$fk_kaba%' && date like '%$date%'";
 
$res = mysqli_query($con,$sql);
 
$result = array();
 
while($row = mysqli_fetch_array($res)){
array_push($result,array('quantity'=>$row[3],
'time'=>$row[5]
));
}
 
echo json_encode(array("result"=>$result));
 
mysqli_close($con);
 
?>