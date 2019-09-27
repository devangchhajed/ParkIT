<?php include "commonheader.php";
  include "include/dbconn.php";

  



  $sql = "SELECT * FROM parking_lot_details where user_id=".$_SESSION['user_id'];
  $result = $conn->query($sql);
  if($result->num_rows>0){
    $row = mysqli_fetch_row($result);
    $name = $row[1];
    $addr = $row[2];
    $tot_gen = $row[7];
    $tot_da = $row[8];
    $price = $row[11];
    $available = $row[12];
    $valet = $row[13];
    $cctv = $row[14];
    $ins = FALSE;
  }else{
    $name = "Parking Lot Name";
  $addr = "Address";
  $tot_gen = "100";
  $tot_da = "10";
  $price = "10";
  $available="False";
  $valet = "False";
  $cctv = "False";
  $ins = TRUE;
  }

  ?>
    <div class="container-fluid mt--7">
      <div class="row">
        <div class="col-xl-12 order-xl-1">
          <div class="card bg-secondary shadow">
            <div class="card-header bg-white border-0">
              <div class="row align-items-center">
                <div class="col-8">
                  <h3 class="mb-0">Parking Lot Details</h3>
                </div>
              </div>
            </div>
            <div class="card-body">
              <form method="POST" action="parkinglotdetails.php">
                  <h6 class="heading-small text-muted mb-4">Parking Detail Info</h6>
                  <div class="pl-lg-4">
                    <div class="row">
                          <div class="col-md-12">
                              <div class="form-group">
                                  <label class="form-control-label" for="input-name">Name</label>
                                  <input id="input-name" class="form-control form-control-alternative" placeholder="<?php echo $name?>" type="text" name="pname">
                              </div>
                          </div>
                      </div>
                      <div class="row">
                          <div class="col-md-12">
                              <div class="form-group">
                                  <label class="form-control-label" for="input-address">Address</label>
                                  <input id="input-address" class="form-control form-control-alternative" placeholder="<?php echo $addr?>" type="text" name="addr">
                              </div>
                          </div>
                      </div>
                      <div class = "row" style="height=100px;">
                          <div class="col-md-12">
                      <div id="gmap" style="width:100%; height:500px; position:relative; overflow:hidden;"></div>
                        <!-- REFERENCES -->

                        <!--lat:<span id='lat'></span>
                        lon:<span id='lon'></span>-->
                        <input type="hidden" id="lat" name="lat">
                        <input type="hidden" id="long" name="long">
                          </div>
                        </div>
                      <div class="row">
                          <div class="col-lg-4">
                              <div class="form-group">
                                  <label class="form-control-label" for="input-city">Total General</label>
                                  <input type="text" id="input-city" class="form-control form-control-alternative" placeholder="<?php echo $tot_gen?>" name="tot_gen">
                              </div>
                          </div>
                          <div class="col-lg-4">
                              <div class="form-group">
                                  <label class="form-control-label" for="input-country">Total Differently Abled</label>
                                  <input type="text" id="input-country" class="form-control form-control-alternative" placeholder="<?php echo $tot_da?>" name="tot_da">
                              </div>
                          </div>
                          <div class="col-lg-4">
                              <div class="form-group">
                                  <label class="form-control-label" for="input-country">Price per hour</label>
                                  <input type="number" id="input-postal-code" class="form-control form-control-alternative" placeholder="<?php echo $price?>" name="cost">
                              </div>
                          </div>
                      </div>
                      <div class="row">
                          <div class="col-lg-4">
                              <div class="form-group">
                                  <label class="form-control-label" for="input-country">Available</label>

                                  <div id="toggles-component" class="tab-pane tab-example-result fade show active" role="tabpanel" aria-labelledby="toggles-component-tab">
                                      <label class="custom-toggle">
                                          <input type="checkbox" checked="" name="available">
                                          <span class="custom-toggle-slider rounded-circle"></span>
                                      </label>
                                  </div>
                              </div>
                          </div>
                          <div class="col-lg-4">
                              <div class="form-group">
                                  <label class="form-control-label" for="input-country">Valet</label>

                                  <div id="toggles-component" class="tab-pane tab-example-result fade show active" role="tabpanel" aria-labelledby="toggles-component-tab">
                                      <label class="custom-toggle">
                                          <input type="checkbox" checked="" name="valet">
                                          <span class="custom-toggle-slider rounded-circle"></span>
                                      </label>
                                  </div>
                              </div>
                          </div>
                          <div class="col-lg-4">
                              <div class="form-group">
                                  <label class="form-control-label" for="input-country">CCTV</label>

                                  <div id="toggles-component" class="tab-pane tab-example-result fade show active" role="tabpanel" aria-labelledby="toggles-component-tab">
                                      <label class="custom-toggle">
                                          <input type="checkbox" checked="" name="cctv">
                                          <span class="custom-toggle-slider rounded-circle"></span>
                                      </label>
                                  </div>
                              </div>
                          </div>
                      </div>
                  </div>

                  
                  <div class="col-4 text-right">
                      <input type="submit" class="btn btn-sm btn-primary" value="Save"/>
                  </div>

              </form>
            </div>
          </div>
        </div>
      </div>
    </div>

    <script type="text/javascript"
src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDCwz7wCUas1bn-eEKTBaElcEYh3r7xf0E">


</script>
<script type="text/javascript">

    function initialize() {
        var myLatlng = new google.maps.LatLng(24.18061975930,79.36565089010);
        var myOptions = {
            zoom:7,
            center: myLatlng,
            mapTypeId: google.maps.MapTypeId.ROADMAP
        }
        map = new google.maps.Map(document.getElementById("gmap"), myOptions);
        // marker refers to a global variable
        marker = new google.maps.Marker({
            position: myLatlng,
            map: map
        });
        // if center changed then update lat and lon document objects
        google.maps.event.addListener(map, 'center_changed', function () {
            var location = map.getCenter();
            var lt = location.lat();
            var lg = location.lng();
            document.getElementById("lat").value = lt;           

            document.getElementById("long").value = lg;
            // call function to reposition marker location
            
            placeMarker(location);
        });
        // if zoom changed, then update document object with new info
        google.maps.event.addListener(map, 'zoom_changed', function () {
            zoomLevel = map.getZoom();
            document.getElementById("zoom_level").innerHTML = zoomLevel;
        });
        // double click on the marker changes zoom level
        google.maps.event.addListener(marker, 'dblclick', function () {
            zoomLevel = map.getZoom() + 1;
            if (zoomLevel == 20) {
                zoomLevel = 10;
            }
            document.getElementById("zoom_level").innerHTML = zoomLevel;
            map.setZoom(zoomLevel);
        });

        function placeMarker(location) {
            var clickedLocation = new google.maps.LatLng(location);
            marker.setPosition(location);
        }
    }
    window.onload = function () { initialize(); };
</script>


  <?php
    if(isset($_POST['pname'])){
      $name = $_REQUEST['pname'];
      $addr = $_REQUEST['addr'];
      $tot_gen = $_REQUEST['tot_gen'];
      $tot_da = $_REQUEST['tot_da'];
      $price = $_REQUEST['cost'];
      $available = $_REQUEST['available'];
      if($available=="on"){
        $available="True";
      }else{
        $available="False";
      }
      $valet = $_REQUEST['valet'];
      if($valet=="on"){
        $valet="True";
      }else{
        $valet="False";
      }
      $cctv = $_REQUEST['cctv'];
      if($cctv=="on"){
        $cctv="True";
      }else{
        $cctv="False";
      }
      $lat = $_REQUEST['lat'];
      $long = $_REQUEST['long'];

          
      // function to get  the address
function get_lat_long($address) {
  $address = str_replace(" ", "+", $address);
  $array = array();
  //$geo = file_get_contents('http://maps.googleapis.com/maps/api/geocode/json?address='.urlencode($address).'&sensor=false&key=AIzaSyDCwz7wCUas1bn-eEKTBaElcEYh3r7xf0E');
  $geo = file_get_contents('https://api.mapbox.com/geocoding/v5/mapbox.places/'.$address.'.json?access_token=pk.eyJ1IjoidGFpeWVlYmEiLCJhIjoiY2p1M3U0NGo4MHIzaDQzcnYybmM2eHRxcSJ9.aWb_sgnJUyJpwWpfUP38cQ');

  // We convert the JSON to an array
  $geo = json_decode($geo, true);

  // If everything is cool
  if ($geo['status'] = 'OK') {
     $latitude = $geo['results'][0]['geometry']['location']['lat'];
     $longitude = $geo['results'][0]['geometry']['location']['lng'];
     $array = array('lat'=> $latitude ,'lng'=>$longitude);
  }
  return $array;
}
//get_lat_long("Mehrauli-Gurgaon Rd, Indian Airlines Pilots Society, Sushant Lok Phase I, Sector 28, Gurugram, Haryana 122002");

    //get_lat_long("18/BAqsaCHSAmrutNagarJogeshwari(w)Mumbai");
      
      if($ins){
        $sql = "INSERT INTO parking_lot_details (parking_lot_name, parking_lot_address, parking_lot_latitude, parking_lot_longitude, total_general, total_differently_abled, current_general, current_differently_abled, price_per_hour, available, valet_available, cctv_available, user_id) 
        VALUES('".$name."', '".$addr."', '".$lat."', '".$long."', '".$tot_gen."', '".$tot_da."', '".$tot_gen."', '".$tot_da."', '".$price."', '".$available."', '".$valet."', '".$cctv."', ".$_SESSION['user_id'].")";
      }else{
        $sql = "UPDATE parking_lot_details SET parking_lot_name='".$name."' , parking_lot_address = '".$addr."' , parking_lot_latitude = '".$lat."' ,parking_lot_longitude = '".$long."' ,total_general = '".$tot_gen."', total_differently_abled = '".$tot_da."', current_general = '".$tot_gen."', current_differently_abled = '".$tot_da."' , price_per_hour = '".$price."', available = '".$available."' , valet_available = '".$valet."', cctv_available = '".$cctv."' , user_id = ".$_SESSION['user_id'];
      }
      //echo $sql;
      if($conn->query($sql)){
        echo "<script>alert(\"Success...!!!\");
        window.location.href='index.php'</script>";
      }else{
        echo mysqli_error($conn);
      }
    }
  ?>

<?php include "commonfooter.php"; ?>