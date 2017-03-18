package com.zgmz.ls.module.fp;

import com.android.charger.*;

public class PowerOperate {

	private static final int FINGERPRINT_MODULE_5V_PIN = 4;

	public void enableFingerprintModule_5Volt() {
		mtSetGPIOValue(FINGERPRINT_MODULE_5V_PIN, true);
	}

	public void disableFingerprintModule_5Volt() {
		mtSetGPIOValue(FINGERPRINT_MODULE_5V_PIN, false);
	}

	private void mtSetGPIOValue(int pin, boolean bHigh) {
		if (pin < 0) {
			return;
		}

		EmGpio.gpioInit();
		EmGpio.setGpioMode(pin);

		if (bHigh) {
			EmGpio.setGpioOutput(pin);
			EmGpio.setGpioDataHigh(pin);
		} else {
			EmGpio.setGpioOutput(pin);
			EmGpio.setGpioDataLow(pin);
		}
		EmGpio.gpioUnInit();
	}
}
