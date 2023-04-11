<?php
		$DB_USER='root'; 
		$DB_PASS=''; 
		$DB_HOST='localhost';
		$DB_NAME='connected_kaba';
		$mysqli = new mysqli($DB_HOST, $DB_USER, $DB_PASS, $DB_NAME);
		/* check connection */
		if (mysqli_connect_errno()) {
		printf("Connect failed: %s\n", mysqli_connect_error());
		exit();
		}		
		$mysqli->query("SET NAMES 'utf8'");
		$sql="SELECT * FROM kabas";
		$result=$mysqli->query($sql);
		while($e=mysqli_fetch_assoc($result)){
		$output[]=$e; 
		}	

		print(json_encode($output)); 
		$mysqli->close();	

?>