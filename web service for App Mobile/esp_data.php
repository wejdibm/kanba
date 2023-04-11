<!DOCTYPE html>
<html><body>
<?php



$servername = "localhost";
$dbname = "connected_kaba";       // REPLACE with your Database name
$username = "root";     // REPLACE with Database user
$password = "";         // REPLACE with Database user password
if($condition = (isset($_GET["fk_poste"])  && isset($_GET["fk_kaba"]) && isset($_GET["quantity"]))){
	$fk_poste = $_GET["fk_poste"];
	$fk_kaba = $_GET["fk_kaba"];
	$quantity = $_GET["quantity"];
}


// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
$con = mysqli_connect("localhost", "root", "", "connected_kaba");

// Check connection
if ($con->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 
if ($condition){
$sql1 = "INSERT INTO postekaba (fk_poste, fk_kaba, quantity)
        VALUES (".$fk_poste." ,'".$fk_kaba."' ,". $quantity.")";
		mysqli_query($con, $sql1);
}
		
$sql = "SELECT * FROM poste1kaba1 ";
echo '<table cellspacing="5" cellpadding="5">
      <tr> 
	    <td>id</td>
		<td>fk_poste</td>
        <td>fk_kaba</td> 
        <td>quantity</td>
        <td>Date</td> 
        <td>Time</td> 
      </tr>';
if ($result = mysqli_query($con, $sql)) {
	
    while ($row = $result->fetch_assoc()) {
        $row_id = $row["id"];
		
		$row_fk_poste = $row["fk_poste"];
        $row_fk_kaba = $row["fk_kaba"];
        $row_quantity = $row["quantity"];
		$row_date = $row["date"]; 
        $row_time = $row["time"]; 
        
        
      
        echo '<tr> 
                <td>' . $row_id . '</td>
				<td>' . $row_fk_poste . '</td> 
                <td>' . $row_fk_kaba . '</td> 
                <td>' . $row_quantity . '</td>
				<td>' . $row_date . '</td> 
                <td>' . $row_time . '</td> 
              </tr>';
    }
    $result->free();
}

$conn->close();
?> 
</table>
</body>
</html>