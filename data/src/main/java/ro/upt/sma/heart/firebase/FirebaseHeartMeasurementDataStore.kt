package ro.upt.sma.heart.firebase

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList
import ro.upt.sma.heart.model.HeartMeasurement
import ro.upt.sma.heart.model.HeartMeasurementRepository

class FirebaseHeartMeasurementDataStore(userId: String) : HeartMeasurementRepository {

    private val reference: DatabaseReference = FirebaseDatabase.getInstance().reference.child(userId)

    override fun post(heartMeasurement: HeartMeasurement) {
        val timestamp = heartMeasurement.timestamp.toString();
        val heartRate = heartMeasurement.value.toString();
        reference.child(timestamp).setValue(heartRate)
    }

    override fun observe(listener: HeartMeasurementRepository.HeartChangedListener) {
        // DONE("Add a child event listener and pass the last value to the listener")
        val addChildEventListener = reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                listener.onHeartChanged(toHeartMeasurement(snapshot))
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun retrieveAll(listener: HeartMeasurementRepository.HeartListLoadedListener) {
        // DONE("Retrieve all measurements and pass them to the listener")
        reference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listener.onHeartListLoaded(toHeartMeasurements(snapshot.children))
                reference.removeEventListener(this)
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }


    private fun toHeartMeasurements(children: Iterable<DataSnapshot>): List<HeartMeasurement> {
        val heartMeasurementList = ArrayList<HeartMeasurement>()

        for (child in children) {
            heartMeasurementList.add(toHeartMeasurement(child))
        }

        return heartMeasurementList
    }

    private fun toHeartMeasurement(dataSnapshot: DataSnapshot): HeartMeasurement {
        return HeartMeasurement(
                java.lang.Long.valueOf(dataSnapshot.key),
                dataSnapshot.getValue(Int::class.java)!!)
    }

}
