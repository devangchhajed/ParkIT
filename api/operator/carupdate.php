<?php

include '../include/dbconn.php';


if(isset($_POST['carnum'])){
    $carnum = $_POST['carnum'];


    $query = "SELECT * FROM  user WHERE carnumber = '".$carnum."'";
    $result = $conn->query($query);
    $response = array();
	$userid=0;
    if($result->num_rows>0){
        $row = mysqli_fetch_row($result);

        $userid=$row[0];
    }


	$query="SELECT * FROM `bookings` WHERE `user_id` = ".$userid;
	
    $result = $conn->query($query);
	$bookingid=0;
	$parkinglotid = 0;
	$checkin="";
	$checkout="";
    if($result->num_rows>0){
        while($row = $result->fetch_assoc()) {
            $bookingid=$row["booking_id"];
			$parkinglotid = $row["parking_lot_id"];
			$checkin = $row["check_in_time"];
			$checkout = $row["check_out_time"];
        }
		
		if($checkin==null){
			    $query = "UPDATE `bookings` SET `check_in_time`=NOW() WHERE `booking_id`=".$bookingid;
				$response = array();
				if($conn->query($query)){
					
					    $sql = "SELECT current_general FROM parking_lot_details WHERE parking_lot_id=".$parkinglotid;
						$res = $conn->query($sql);
						$row = mysqli_fetch_assoc($res);
						$tot_gen = (int)($row['current_general'])-1;
						$sql2 = "UPDATE parking_lot_details SET current_general = '".$tot_gen."'";
						if($conn->query($sql2)){
							echo "Car Checked In";
						}else{
							echo "Error Flag 1 ";
						}					
				}else{
					echo "Error in : ".$query;					
				}

		}else{
			$query = "UPDATE `bookings` SET `check_out_time`=NOW() WHERE `booking_id`=".$bookingid;
			$response = array();
			if($conn->query($query)){
				
				
				
					    $sql = "SELECT current_general FROM parking_lot_details WHERE parking_lot_id=".$parkinglotid;
					$res = $conn->query($sql);
					$row = mysqli_fetch_assoc($res);
					$tot_gen = (int)($row['current_general'])+1;

					$sql2 = "UPDATE parking_lot_details SET current_general = '".$tot_gen."'";
						if($conn->query($sql2)){
							echo "Car Checked Out";
						}else{
							echo "Error Flag 1 ";
						}					

				
			}
						else{
							echo "Error Flag 2 ";
						}					
		}

    }else{
        echo "No car Found";
    }
	
	
	



}