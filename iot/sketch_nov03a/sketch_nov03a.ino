#include<ESP8266WiFi.h>
#include <SPI.h>
#include <Ethernet.h>
#include <PubSubClient.h>


//D0=NC, D1= TRIGGER, D2= ECHO, VCC= 5V
int long duration ;
int dist ;
const int trig=D1;
const int echo=D2;

const char* mqtt_server = "192.168.137.1";

const char* ssid="Devang-LT";
const char* password="google567";

WiFiClient espClient;
PubSubClient client(espClient);

void connectwifi(){
  WiFi.begin(ssid, password);
 
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi..");
  }
  
  Serial.println("Connected to the WiFi network");

  
}

void connectMqtt(){
  client.setServer(mqtt_server, 1883);
  
}


void setup()
{
  pinMode(trig,OUTPUT);
  pinMode(echo,INPUT);
  Serial.begin(9600);
  connectwifi();
  delay(3000);                          
  connectMqtt();

}
void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect("ESP8266Client")) {
      Serial.println("connected");
      // Subscribe
      client.subscribe("esp32/output");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

void loop()
{
  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  
 digitalWrite(trig,LOW);
 delay(2);
 digitalWrite(trig,HIGH);

 delay(1000);
 digitalWrite(trig,LOW);
 duration=pulseIn(echo,HIGH);
 dist=duration*0.034/2;
 String s = "Distance : "+String(dist);
 Serial.println(s);

 if(dist<50){
 client.publish("ParkIT", "1");
 }else{
 client.publish("ParkIT", "0");
  
 }
 
}
