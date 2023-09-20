# Android MODBUS Relay Control

This Android project can control up to 64 individual 8 channel R421A08 relay boards using a IOIO Bluetooth interface.


## Hardware

The following hardware is required for this project:

* One or more R421A08 relay boards.
* IOIO OTG.

### R421A08 relay board

![R421A08 board](https://github.com/mmehdi/android-modbus-relays/blob/master/images/R421A08.png?raw=true)

* RS485 (binary) interface.
* 8 x 12V Relays: 10A 125VAC / 10A 28VDC.
* 8 status LED's.
* 6 DIP switches for 64 board addresses.
* Board power: 12V DC.
* Current board idle: ~11mA.
* Current one relay: ~26mA.
* Current all relays on: ~220mA.
* Length: 90mm, width: 60mm, height: 20mm.

**WARNING: DO NOT USE THIS RELAY BOARD WITH 230V AC!**  
The distance between relay traces on the PCB are < 2mm without holes for isolation. This is dangerous when using it with high voltages. See the picture above.


### IOIO OTG

IOIO OTG board provides I/O interface for Android via Bluetooth.

![IOIO OTG](https://github.com/mmehdi/android-modbus-relays/blob/master/images/IOIO-OTG.jpg?raw=true)


### Acknowledgements


[MODBUS Application Protocol Specification v1.1.b](http://www.modbus.org/docs/Modbus_Application_Protocol_V1_1b.pdf)
[IOIO OTG Project](https://github.com/ytai/ioio)
