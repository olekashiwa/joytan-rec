package jp.bellware.echo.main

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.net.Uri
import android.os.*
import android.os.Process
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

import jp.bellware.echo.data.QRecStorage
import java.io.File
import java.io.IOException

class ShareHandler(private val storage: QRecStorage) {
    private var track: AudioTrack? = null

    private var thread: Thread? = null

    val fStorage = FirebaseStorage.getInstance()
    // Create a storage reference from our app
    val fStorageRef = fStorage.reference

    fun onResume() {}

    fun share(fileName: String, onEndListener: () -> Unit) {
        if (storage.length == 0) {
            // If packet length zero, end immediately
            onEndListener()
            return
        }

        val outputFileName = Environment.getExternalStorageDirectory().absolutePath + "/output.wav"
        val outputFile = Uri.fromFile(File(outputFileName))
        val audioRef = fStorageRef.child(fileName)

        track = AudioTrack(AudioManager.STREAM_MUSIC,
                RecordHandler.SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                storage.packetSize * 2, AudioTrack.MODE_STREAM)

        thread = Thread(Runnable {
            // Without this, the first few packets is clipped...
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
            try {
                storage.save(outputFileName)

                audioRef.putFile(outputFile)
                        .addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot> {
                            override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot) {
                                // Get a URL to the uploaded content
                                Log.println(1, "ERR", "Upload Succeeded!!")
                            }
                        })
                        .addOnFailureListener(object : OnFailureListener {
                            override fun onFailure(exception: Exception) {
                                // Handle unsuccessful uploads
                                Log.println(1, "ERR", "Upload Failed!!")
                            }
                        })
            } catch (e: IOException) {
                e.printStackTrace()
            }

            try {
                Thread.sleep((storage.packetSize * 1000 / 44100).toLong())
            } catch (e: InterruptedException) {

            }

        })
        thread?.start()
    }

}