<?php 
require_once("database.php");
$email=$_REQUEST["email"];
$password=$_REQUEST["password"];
$sql="select usertype from logindata where email='$email' AND password='$password'";
$result=mysql_query($sql,$cn);
$n=mysql_num_rows($result);
if($n>0)
{ 
$rw=mysql_fetch_array($result);
$utype=$rw["usertype"];
echo $utype;
}
else
{
	echo "Failure";
}
?>