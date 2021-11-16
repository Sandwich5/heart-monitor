package ro.upt.sma.heart

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_heart_assistant.*
import ro.upt.sma.heart.model.HeartMeasurement
import ro.upt.sma.heart.presenters.assistant.HeartAssistantPresenter
import ro.upt.sma.heart.presenters.assistant.HeartAssistantView
import ro.upt.upt.sma.heart.injection.Injection
import java.text.MessageFormat

class HeartAssistantActivity : AppCompatActivity(), HeartAssistantView {

    private lateinit var presenter: HeartAssistantPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_heart_assistant)

        rv_heart_assistant_measurement_list.layoutManager = LinearLayoutManager(this)

        // FIXME: add specific WEAR device code
        val code = "wear"
        this.presenter = Injection.provideHeartAssistantPresenter(code)
    }

    override fun onResume() {
        super.onResume()

        presenter.bind(this)
    }

    override fun onPause() {
        super.onPause()

        presenter.unbind()
    }

    override fun showLast(heartMeasurement: HeartMeasurement) {
        Log.d(TAG, "showLast: $heartMeasurement")

        tv_heart_assistant_instant_value.text =
            MessageFormat.format("{0} bpm", heartMeasurement.value)
        tv_heart_assistant_instant_date!!.text =
            DateUtils.getRelativeTimeSpanString(
                heartMeasurement.timestamp,
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS
            )
    }

    override fun showList(heartMeasurementList: List<HeartMeasurement>) {
        Log.d(TAG, "showList: " + heartMeasurementList.size)

        rv_heart_assistant_measurement_list?.adapter = MeasurementsAdapter(heartMeasurementList)
    }

    companion object {
        private val TAG = HeartAssistantActivity::class.java.simpleName
    }

}
