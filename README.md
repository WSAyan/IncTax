# IncTax
WIP: NBR income tax calculation library 2020-21


## Usage

Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Add the dependency
```gradle
dependencies {
	        implementation 'com.github.WSAyan:IncTax:alpha-1.0.0'
	}
```

Calculate tax (female taxpayer, monthly gross salary = 55000, yearly extra benifits = 60000, allowable investment = 0)
```kotlin
int tax = IncTaxCore().calculateTaxByDefaultGross(
            IncTaxCore.GENDER_FEMALE,55000,60000,0)
```
