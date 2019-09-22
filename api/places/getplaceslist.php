<?php

include '../include/dbconn.php';


if(isset($_POST['lat'])&&isset($_POST['lng'])){
    $lat = $_REQUEST['lat'];
    $lng = $_REQUEST['lng'];
    $query = "SELECT *, 6371 * 2 * ASIN(SQRT(POWER(SIN((".$lat." - abs(parking_lot_latitude)) * pi()/180 / 2), 2)+ COS(".$lat." * pi()/180 ) * COS(abs(parking_lot_latitude) * pi()/180)* POWER(SIN((".$lng." - parking_lot_longitude) * pi()/180 / 2), 2) )) as  distance FROM parking_lot_details HAVING distance < 5 ORDER BY distance";
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