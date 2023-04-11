
	

<?php 

define('HOST','localhost');
define('USER','root');
define('PASS','');
define('DB','connected_kaba');

 
$con = mysqli_connect(HOST,USER,PASS,DB);
$fk_poste  = $_GET['fk_poste'];
$fk_kaba  = $_GET['fk_kaba'];

$sql = "select quantity,date,time from postekaba where (fk_poste like '%$fk_poste%' && fk_kaba like '%$fk_kaba%') ORDER BY id DESC LIMIT 1";
 
$res = mysqli_query($con,$sql);
 
$result = array();
 
while($row = mysqli_fetch_array($res)){
array_push($result,array('quantity'=>$row[0],
'date'=>$row[1],
'time'=>$row[2]

));
}
 
echo json_encode(array("result"=>$result));
 
mysqli_close($con);
?>