#include "HX711.h"
#include "WiFi.h"
#include "SPI.h"
#include "HTTPClient.h"
#include <Wire.h>
#include <LiquidCrystal_I2C.h>

#define LED 2
#define DOUT  26
#define CLK  25
LiquidCrystal_I2C lcd(0x27, 16,2);
HX711 scale;
int LedV = 23;
int LedR = 19;
int LedO = 18;
int Buzz = 5;
float calibration_factor = 120000.00 ;
float kabaweight ;
float nbre_pieces;

float load;
String quantity,fk_poste,fk_kaba;

// Replace with your network credentials
const char* ssid = "Hanchour";
const char* password =  "fati1954";

void readsensor () {
    scale.begin(26, 25);
    scale.set_scale(calibration_factor); //Adjust to this calibration factor
   
    float items_weight = scale.get_units() ; // variable contenant le weight scale 
    kabaweight = items_weight + 0.724;
    nbre_pieces = round((kabaweight- 0.493) / 0.038) ;
    if (nbre_pieces < 0 )
    { nbre_pieces = 0 ;}
    if (nbre_pieces < 6)
    { digitalWrite(LedR, HIGH);
      digitalWrite(LedV, LOW);
      digitalWrite(LedO, LOW);
      digitalWrite(Buzz, HIGH);
      delay (1000);
      digitalWrite(Buzz, LOW);
      delay (500);
      }
    if (nbre_pieces > 30)
    { digitalWrite(LedO, HIGH); 
      digitalWrite(LedR, LOW);
      digitalWrite(LedV, LOW);
      digitalWrite(Buzz, HIGH);
      delay (1000);
      digitalWrite(Buzz, LOW);
      delay (500);} 
    if ((nbre_pieces > 5) && (nbre_pieces <= 30))
    { digitalWrite(LedO, LOW); 
      digitalWrite(LedR, LOW);
      digitalWrite(LedV, HIGH);
      digitalWrite(Buzz, LOW);} 
    lcd.init();   
    lcd.backlight(); 
    lcd.setCursor(0,0); 
    lcd.print("Nbre restant :");
    lcd.setCursor(6,1);
    lcd.print(nbre_pieces);
    Serial.print ("weight is :"   ) ;
    Serial.println (kabaweight,3);
    Serial.println (nbre_pieces);
    Serial.println();
    kabaweight =scale.get_units() ;
}
void sendData(){
  if(WiFi.status()== WL_CONNECTED){
    HTTPClient http;
    String url = "http://lee.leocontact.tn/connected_kaba/esp_data.php?fk_poste=" + fk_poste
                          + "&fk_kaba=" + fk_kaba + "&quantity=" + quantity;
    
    // Your Domain name with URL path or IP address with path
    http.begin(url.c_str());
    int httpResponseCode = http.GET();
    
    if (httpResponseCode>0) {
      Serial.print("HTTP Response code: ");
      Serial.println(httpResponseCode);
    }
    else {
      Serial.print("Error code: ");
      Serial.println(httpResponseCode);
    }
    // Free resources
    http.end();
  }
  else {
    Serial.println("WiFi Disconnected");
  }
  //Send an HTTP POST request every 3 seconds
  delay(3000);   
}

void setup() {
  Serial.begin(9600);
 
  scale.set_scale();
  pinMode(5, OUTPUT);
  pinMode(18, OUTPUT);
  pinMode(19, OUTPUT);
  pinMode(21, OUTPUT);
  WiFi.begin(ssid, password);
  Serial.println("Connecting");
  while(WiFi.status() != WL_CONNECTED) { 
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to WiFi network with IP Address: ");
  Serial.println(WiFi.localIP());
  
}

void loop() {
  readsensor();
  quantity = nbre_pieces;
  fk_poste = "1";
  fk_kaba = "1";
  sendData();
}

