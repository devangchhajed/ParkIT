# importing the requests library 
import requests 


def networkcall(carnumber):  
    API_ENDPOINT = "http://192.168.137.1/ParkIT/api/places/getplacefromid.php?pid=1"

    # data to be sent to api 
    data = {'carnum':carnumber} 
      
    # sending post request and saving response as response object 
    r = requests.post(url = API_ENDPOINT, data = data) 
      
    # extracting response text  
    pastebin_url = r.text 
    print("Response :%s"%pastebin_url) 
