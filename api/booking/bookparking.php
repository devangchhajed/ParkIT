<?php

include '../include/dbconn.php';


if(isset($_POST['uid'])&&isset($_POST['pid'])){
    $pid = $_REQUEST['pid'];
    $uid = $_REQUEST['uid'];

    $query = "INSERT INTO bookings (booking_id, user_id, vehicle_id, parking_lot_id, app_check_in, app_check_out, check_in_time, check_out_time, booking_status, payment_status, booking_date) VALUES (NULL, '".$uid."', '1', '".$pid."', NOW(), NULL, NULL, NULL, 'Booked', 'Cash', NULL);";
    $response = array();
    if($conn->query($query)){
        $response['status']=1;
		$response['bookingid'] = $conn->insert_id;
        echo json_encode($response);
    }else{
        $response['status']=2;
        echo json_encode($response);

    }

}