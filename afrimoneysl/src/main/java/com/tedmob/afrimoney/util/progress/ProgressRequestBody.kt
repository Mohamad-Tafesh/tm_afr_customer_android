package com.tedmob.afrimoney.util.progress

import com.tedmob.afrimoney.BuildConfig
import io.reactivex.Emitter
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.internal.closeQuietly
import okio.BufferedSink
import okio.source
import java.io.File
import kotlin.math.min

class ProgressFileRequestBody(
    private val file: File,
    private val contentType: MediaType?,
    private val progressEmitter: Emitter<ProgressStatus>,
    private val firstNumberOfWriteToCallsToIgnore: Int = if (BuildConfig.DEBUG) 1 else 0
) : RequestBody() {

    companion object {
        private const val SEGMENT_SIZE = 8192L // okio.Segment.SIZE
    }


    private var currentNumWriteToCalls = 0


    override fun contentType(): MediaType? = contentType

    override fun contentLength(): Long = file.length()

    override fun writeTo(sink: BufferedSink) {
        currentNumWriteToCalls++

        file.source().let {
            try {
                val total = contentLength()
                var remaining = total
                var size = min(SEGMENT_SIZE, remaining)

                var progress = (100 - remaining.toDouble() / total) * 100
                if (currentNumWriteToCalls > firstNumberOfWriteToCallsToIgnore) {
                    progressEmitter.onNext(ProgressStatus(progress, remaining, total, total >= 100))
                }

                while (remaining > 0) {
                    sink.write(it, size)
                    remaining -= size
                    size = min(SEGMENT_SIZE, remaining)

                    progress = (100 - remaining.toDouble() / total) * 100
                    if (currentNumWriteToCalls > firstNumberOfWriteToCallsToIgnore) {
                        progressEmitter.onNext(ProgressStatus(progress, remaining, total, total >= 100))
                    }
                }
            } catch (e: Exception) {
                progressEmitter.onError(e)
                throw e
            } finally {
                it.closeQuietly()
            }
        }

        /*//prevent publishing too many updates, which slows upload, by checking if the upload has progressed by at least 1 percent
                    if (progress - lastProgressPercentUpdate > 1 || progress == 100f) {
                        // publish progress
                        getProgressSubject.onNext(progress)
                        lastProgressPercentUpdate = progress
                    }*/
    }
}

class ProgressStatus(val progress: Double, val remaining: Long, val total: Long, val isDone: Boolean)


//TODO test below scenario, and find a way to make it easier to use
/*private fun test(file: File, apiCall: (body: RequestBody) -> Single<Unit>) {
    val liveData = MutableLiveData<Resource<Double>>()

    val resultObs = Observable.create<ProgressStatus> { emitter ->
        val body = ProgressFileRequestBody(
            file, "video/mp4".toMediaTypeOrNull(), emitter
        )
        val disposable = apiCall(body).subscribe(
            {
                emitter.onComplete()
            },
            {
                emitter.onError(it)
            }
        )
        emitter.setDisposable(disposable)
    }
    val disposable = resultObs.subscribeWith(
        object : DefaultObserver<ProgressStatus>() {
            override fun onNext(t: ProgressStatus) {
                liveData.value = Resource.Loading(t.progress)
            }

            override fun onError(e: Throwable) {
                liveData.value = Resource.Error(e.localizedMessage.orEmpty())
            }

            override fun onComplete() {
                liveData.value = Resource.Success(100.0)
            }
        }
    )
}*/
