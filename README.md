# FlexiCalculator

FlexiCalculator is a beautifully designed calculator and unit converter app for Android that takes care of your
mathematical calculation and unit conversion needs.

## Calculate Module

![Calculate Module](misc/device_tab1_framed.png?raw=true)

It supports the following -

- Basic Addition, Subtraction, Multiplication and Division functions
- Power, Factorial, Percentage and Modulus calculations
- Trigonometric Sine, Cosine and Tangent calculations
- Logarithmic base 10 and base e calculations
- Programmer functions including Left Shift, Right Shift, AND, OR, XOR
- Choice of angular unit - Degree or Radian
- Choice of numerical base - Hexadecimal, Decimal, Octal or Binary
- Precision of up to 9 decimal digits and no loss of precision due to usage of arbitrary precision
data types

## Convert Module

![Convert Module](misc/device_tab2_framed.png?raw=true)

It supports the following -

- Conversion among many different units of Volume, Weight, Length, Area, Fuel Economy, Temperature, 
Pressure, Energy, Power, Torque, Speed, Time, Data and Angle.
- Precision of up to 9 decimal digits and no loss of precision due to usage of arbitrary precision
data types

## Speak Module

![Speak Module](misc/device_tab3_framed.png?raw=true)

It supports the following -

- All calculations supported by the Calculate Module can be done by just using your voice. For eg -
    - "one plus two" will result in "1+2=3"
    - "five percent of one thousand" will result in "5%×1000=50"
    - "one plus open bracket three plus five close bracket" will result in "1+(3+5)=9"
- All unit conversions supported by the Convert Module can be done by just using your voice. For eg -
    - "hundred ml to liter" will result in "100 MILLILITERS is 0.1 LITERS"
    - "hundred kph to mph" will result in "100 KILOMETERS_PER_HOUR is 62.137119224 MILES_PER_HOUR"
- Usage of Google Speech Recognition API results in remarkable recognition accuracy.
- Both offline as well as online Speech Recognition is possible. However, online Speech Recognition
will result in better voice recognition accuracy.

## Developer Notes

This app uses Crashlytics crash reporting and real time analytics. To build successfully, valid
apiKey and apiSecret must be present in the `app/fabric.properties` file. More details
[here](https://docs.fabric.io/android/fabric/settings/api-keys.html),
[here](https://docs.fabric.io/android/fabric/settings/working-in-teams.html#android-projects) and
[here](https://docs.fabric.io/android/fabric/manage-your-settings/organization.html#managing-credentials).
