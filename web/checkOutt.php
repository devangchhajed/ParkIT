<?php

include 'commonheader.php';
include 'include/dbconn.php';

$id = $_POST['uname'];
$pid = $_POST['pid'];
    $date = date('m/d/Y h:i:s a', time());

    $sql = "SELECT current_general FROM parking_lot_details WHERE parking_lot_id=".$pid;
    $res = $conn->query($sql);
    $row = mysqli_fetch_assoc($res);
    $tot_gen = (int)($row['current_general'])+1;

    $sql = "UPDATE bookings SET check_out_time = NOW() WHERE user_id = ".$id." AND parking_lot_id= ".$pid;
    $sql2 = "UPDATE parking_lot_details SET current_general = '".$tot_gen."'";
    if($conn->query($sql) && $conn->query($sql2)){
        echo "<script>window.location.href='index.php'</script>";
    }else{
        echo "else prt";
    }

?>