<?php

include '../include/dbconn.php';
if(isset($_GET['uid'])){
    $uid = $_GET['uid'];
    $query = "SELECT *, (((check_out_time-check_in_time)/3600)+1)*(Select price_per_hour from parking_lot_details where parking_lot_id = b.parking_lot_id) as cost from bookings b where user_id = ".$uid;
    $result = $conn->query($query);
    $res = array();
    if($result->num_rows>0){
        $response = array();
        while($row = $result->fetch_assoc()) {
            array_push($res, $row);
        }

        echo json_encode($res);
    }else{
        $response['status']=2;
        $response['error']="No Result Found";
        echo json_encode($response);
    }



}