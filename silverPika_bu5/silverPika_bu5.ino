#include <SoftwareSerial.h>

#define width 4
#define height 3
int currX;
int menu[width][4];  //first is the height for position in width the others are RGB
int heightColors[height][3];
int lastState[5];
int r = 9, g = 10, b = 11;
int button = 2, up = 4, down = 5, left = 6, right = 7, vib = 12;
int notifyFlag = 0;
int vibFlag = 0;
int vibStract[8];
int notifyColors[3];
int lastBeat , beat = 0, currBeat;
int firstVibBeat;
int isRinging = 0;
SoftwareSerial bt(3, 8);

void setup() {
  Serial.begin(9600);
  bt.begin(115200);
  delay(100);
  bt.print("U,9600,N");
  delay(100);
  bt.begin(9600);
  lastState[0] = 0;
  lastState[1] = 0;
  lastState[2] = 0;
  lastState[3] = 0;
  lastState[4] = 0;
  pinMode(button, INPUT);
  pinMode(up, INPUT);
  pinMode(down, INPUT);
  pinMode(left, INPUT);
  pinMode(right, INPUT);
  pinMode(r, OUTPUT);
  pinMode(g, OUTPUT);
  pinMode(b, OUTPUT);
  pinMode(vib, OUTPUT);
  newSettings();
  setColor();
  lastBeat = (int)millis();
}

void loop() {
  currBeat = (int)millis();
  if(currBeat - lastBeat >= 1000) {
    lastBeat = currBeat;
    if(vibFlag == 1) {
      if(vibStract[(beat - firstVibBeat + 8)%8] == 1) {
        digitalWrite(vib, HIGH);
//        Serial.print("1");
      } else {
        digitalWrite(vib, LOW);
//        Serial.print("0");
      }
    }
    if(notifyFlag == 1) {
      if(beat % 2 == 0) {
        analogWrite(r, notifyColors[0]);
        analogWrite(g, notifyColors[1]);
        analogWrite(b, notifyColors[2]);
      } else {
        analogWrite(r, 0);
        analogWrite(g, 0);
        analogWrite(b, 0);
      }
    }
    beat++;
    if(beat > 7) {
      beat = 0;
    }
    if((beat - firstVibBeat) % 8 == 0) {
//      Serial.println();
      vibFlag = 0;
      digitalWrite(vib, LOW);
//      Serial.println("--");
    }
  }
  if(bt.available()) {
    getData();
  }
  trackBallCheck();
  Serial.print("x = ");
  Serial.print(currX);
  Serial.print("\ty = ");
  Serial.println(menu[currX][0]);
}

void newSettings() {
  menu[0][1] = 255;
  menu[0][2] = 0;
  menu[0][3] = 0;
  
  menu[1][1] = 0;
  menu[1][2] = 255;
  menu[1][3] = 0;
  
  menu[2][1] = 0;
  menu[2][2] = 0;
  menu[2][3] = 255;
  
  menu[3][1] = 10;
  menu[3][2] = 100;
  menu[3][3] = 25;
  
  resetHeight();
//  
//  menu[0][3][0] = 80;
//  menu[0][3][1] = 80;
//  menu[0][3][2] = 80;

  heightColors[0][0] = 255;
  heightColors[0][1] = 0;
  heightColors[0][2] = 255;
  
  heightColors[1][0] = 255;
  heightColors[1][1] = 255;
  heightColors[1][2] = 0;
  
  heightColors[2][0] = 0;
  heightColors[2][1] = 255;
  heightColors[2][2] = 255;
}

void resetHeight() {
  menu[0][0] = 0;
  menu[1][0] = 0;
  menu[2][0] = 0;
  menu[3][0] = 0;
}

void getData() {
  int i, d;
//  int colors[3];
  int in[20];
  i = 0;
  for(i = 0; i < 20; i++) {
    while(!bt.available());
    d = bt.read();
    in[i] = d;
  }
  switch(in[0])
  {
    case ('c'):
      isRinging = 1;
    case ('o'):
      notifyColors[0] = 100*in[2] + 10*in[3] + 1*in[4];
      notifyColors[1] = 100*in[5] + 10*in[6] + 1*in[7];
      notifyColors[2] = 100*in[8] + 10*in[9] + 1*in[10];
      vibStract[0] = in[12] - '0';
      vibStract[1] = in[13] - '0';
      vibStract[2] = in[14] - '0';
      vibStract[3] = in[15] - '0';
      vibStract[4] = in[16] - '0';
      vibStract[5] = in[17] - '0';
      vibStract[6] = in[18] - '0';
      vibStract[7] = in[19] - '0';
      notifyFlag = 1;
      vibFlag = 1;
      firstVibBeat = beat;
      for(i = 0; i < 20; i++) {
//        Serial.print(in[i] - 48);
//        Serial.print(":");
      }
//      Serial.println();
      break;
    case ('s'):
      isRinging = 0;
      notifyFlag = 0;
      break;
    default:
      break;
  }
}

void sendData(char c) {
  char msg[4];
  if(isRinging == 1) {
    if(c == 'u') {
      msg[0] = 'a';
    } else if(c == 'd') {
      msg[0] = 's';
    }
  } else {
    msg[0] = c;
  }
  msg[1] = currX + '0';
  msg[2] = menu[currX][0] + '0';
  msg[3] = '\0';
//  bt.print(msg[0]);
//  bt.print(msg[1]);
//  bt.print(msg[2]);
  bt.write(msg);
}

int dr[5];
void trackBallCheck() {
  dr[0] = digitalRead(button);
  dr[1] = digitalRead(up);
  dr[2] = digitalRead(down);
  dr[3] = digitalRead(left);
  dr[4] = digitalRead(right);
  
//  Serial.print("button = ");
//  Serial.print(dr[0]);
//  Serial.print("\tup = ");
//  Serial.print(dr[1]);
//  Serial.print("\tdown = ");
//  Serial.print(dr[2]);
//  Serial.print("\tleft = ");
//  Serial.print(dr[3]);
//  Serial.print("\tright = ");
//  Serial.println(dr[4]);
 if(lastState[0] != dr[0]) {
   lastState[0] = dr[0];
   notifyFlag = 0;
   sendData('b');
 }
 if(lastState[1] != dr[1]) {
   lastState[1] = dr[1];
   notifyFlag = 0;
   currX+=3;
   currX %= width;
   resetHeight();
   sendData('r');
   setColor();
 }
 if(lastState[2] != dr[2]) {
   lastState[2] = dr[2];
   notifyFlag = 0;
     currX++;
     currX %= width;
     resetHeight();
     sendData('l');
     setColor();
 }
 if(lastState[3] != dr[3]) {
   lastState[3] = dr[3];
   notifyFlag = 0;
   if(menu[currX][0] > 1) {
     menu[currX][0]--;
     setColor();
   }
   sendData('d');
 }
 if(lastState[4] != dr[4]) {
   lastState[4] = dr[4];
   notifyFlag = 0;
   if(menu[currX][0] < width) {
     menu[currX][0]++;
     setColor();
   }
   sendData('u');
  }
}

void setColor() {
  if(menu[currX][0] == 0) {
    analogWrite(r, menu[currX][1]);
    analogWrite(g, menu[currX][2]);
    analogWrite(b, menu[currX][3]);
  } else {
    analogWrite(r, heightColors[menu[currX-1][0]][0]);
    analogWrite(g, heightColors[menu[currX-1][0]][1]);
    analogWrite(b, heightColors[menu[currX-1][0]][2]);
  }
}
