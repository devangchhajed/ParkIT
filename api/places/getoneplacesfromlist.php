<?php

include '../include/dbconn.php';

if(isset($_GET['lat'])&&isset($_GET['lng'])){
    $lat = $_REQUEST['lat'];
    $lng = $_REQUEST['lng'];
    $query = "SELECT *, 6371 * 2 * ASIN(SQRT(POWER(SIN((".$lat." - abs(parking_lot_latitude)) * pi()/180 / 2), 2)+ COS(".$lat." * pi()/180 ) * COS(abs(parking_lot_latitude) * pi()/180)* POWER(SIN((".$lng." - parking_lot_longitude) * pi()/180 / 2), 2) )) as  distance FROM parking_lot_details HAVING distance < 5 ORDER BY distance";
    $result = $conn->query($query);
    if($result->num_rows>0){
        while($row = $result->fetch_assoc()) {
            $row['status']=1;
            $row['totaln']=$result->num_rows;
            echo json_encode($row);
            break;
        }

    }else{
        $response['status']=2;
        $response['totaln']=0;
        $response['error']="No Result Found";
        echo json_encode($response);
    }

}