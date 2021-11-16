package ro.upt.sma.heart.sensor


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import ro.upt.sma.heart.model.HeartSensorRepository
import java.util.*

class HeartSensorDataStore(context: Context) : HeartSensorRepository {

    // DONE ("Get sensor manager service")
    private val sensorManager: SensorManager? = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // DONE ("Get a handle for the sensor")
    private val heartRateSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_HEART_RATE)

    private val listeners = HashMap<HeartSensorRepository.HeartRateListener, SensorEventListener>()

    override fun registerHeartRateListener(listener: HeartSensorRepository.HeartRateListener) {
        if (listeners.containsKey(listener)) {
            return
        }

        val eventListener = object : SensorEventListener {
            override fun onSensorChanged(sensorEvent: SensorEvent) {
                listener.onValueChanged(sensorEvent.values[0].toInt())
            }

            override fun onAccuracyChanged(sensor: Sensor, i: Int) {}
        }

        // DONE("Register sensor event listener with the sensor manager API")
        sensorManager?.registerListener(eventListener, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL)

        listeners[listener] = eventListener
    }

    override fun unregisterHeartRateListener(listener: HeartSensorRepository.HeartRateListener) {
        if (listeners.containsKey(listener)) {
            val eventListener = listeners[listener]
            sensorManager?.unregisterListener(eventListener)
            listeners.remove(listener)
        }
    }

}
